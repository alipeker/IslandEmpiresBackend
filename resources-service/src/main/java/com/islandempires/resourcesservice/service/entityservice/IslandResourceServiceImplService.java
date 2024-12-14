package com.islandempires.resourcesservice.service.entityservice;

import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.dto.request.ResourceAllocationRequestDTO;
import com.islandempires.resourcesservice.dto.returndto.ResourceOfferDTO;
import com.islandempires.resourcesservice.enums.BidderAcceptorEnum;
import com.islandempires.resourcesservice.enums.TransportResourceStatusEnum;
import com.islandempires.resourcesservice.enums.TransportType;
import com.islandempires.resourcesservice.exception.CustomRunTimeException;
import com.islandempires.resourcesservice.exception.ExceptionE;
import com.islandempires.resourcesservice.filter.client.WhoAmIClient;
import com.islandempires.resourcesservice.model.IslandResource;
import com.islandempires.resourcesservice.model.ResourceOffer;
import com.islandempires.resourcesservice.model.ResourceTransport;
import com.islandempires.resourcesservice.model.ResourceTransportState;
import com.islandempires.resourcesservice.rabbitmq.RabbitmqService;
import com.islandempires.resourcesservice.repository.IslandResourceRepository;
import com.islandempires.resourcesservice.repository.ResourceOfferRepository;
import com.islandempires.resourcesservice.repository.ResourceTransportRepository;
import com.islandempires.resourcesservice.service.IslandMetricsCalculatorService;
import com.islandempires.resourcesservice.service.IslandResourceCalculatorService;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class IslandResourceServiceImplService implements IslandResourceQueryService, IslandResourceModificationService {
    @Autowired
    private IslandResourceRepository islandResourceRepository;

    @Autowired
    private IslandResourceCalculatorService islandResourceCalculatorService;

    @Autowired
    private IslandMetricsCalculatorService islandMetricsCalculatorService;

    @Autowired
    private ResourceTransportRepository resourceTransportRepository;

    @Autowired
    private ResourceOfferRepository resourceOfferRepository;

    @Autowired
    private WhoAmIClient whoAmIClient;

    @Autowired
    private RabbitmqService rabbitmqService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Mono<IslandResourceDTO> get(String islandId, Long userId) {
        return this.islandResourceRepository.findByIslandId(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .map(islandResource -> {
                    if(!userId.equals(islandResource.getUserId())) {
                        throw new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES);
                    }
                    return modelMapper.map(islandResource, IslandResourceDTO.class);
                });
    }


    @Override
    public Mono<Boolean> validateResourceAllocationForIsland(String islandId, ResourceAllocationRequestDTO resourceAllocationRequestDTO, Long userId){
        return islandResourceRepository.findByIslandId(islandId)
                .map(islandResource -> {
                    if(userId.equals(islandResource.getUserId())) {
                        throw new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES);
                    }
                    return islandResourceCalculatorService.checkResourceAllocation(islandResource, resourceAllocationRequestDTO);
                }).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Mono<IslandResourceDTO> transportResources(String senderIslandId, String receiverIslandId, RawMaterialsDTO rawMaterialsDTO, Long userId) {
        return islandResourceRepository.findByIslandId(senderIslandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .flatMap(islandResource -> {
                    if (!islandResource.checkRawMaterialsAllocation(rawMaterialsDTO)) {
                        return Mono.error(new CustomRunTimeException(ExceptionE.INSUFFICIENT_RESOURCES));
                    }
                    int necessaryShipNumber = islandResource.diminishingResource(rawMaterialsDTO);
                    return whoAmIClient.getDistanceBetweenIslands(senderIslandId, receiverIslandId)
                            .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                            .flatMap(distance -> {
                                long duration = (long) (1000 * distance * ((double) islandResource.getTradingTimeReductionPercentage() / 100));
                                ResourceTransport resourceTransport = new ResourceTransport(
                                        senderIslandId,
                                        receiverIslandId,
                                        rawMaterialsDTO,
                                        LocalDateTime.now(),
                                        new ResourceTransportState(TransportResourceStatusEnum.GOING, Duration.ofMillis(duration)),
                                        TransportType.TRANSPORT
                                );
                                resourceTransport.setShipNumber(necessaryShipNumber);
                                return resourceTransportRepository.save(resourceTransport)
                                        .flatMap(resourceTransportRecorded -> {
                                            return islandResourceRepository.save(islandResource)
                                                    .flatMap(saved -> {
                                                        rabbitmqService.sendTradingMessage(resourceTransportRecorded.getId(), duration);
                                                        return Mono.just(saved);
                                                    })
                                                    .map(islandResourceSaved -> modelMapper.map(islandResourceSaved, IslandResourceDTO.class));
                                        })
                                        .onErrorResume(e -> {
                                            return Mono.error(new CustomRunTimeException(ExceptionE.SERVER_ERROR));
                                        });
                            })
                            .onErrorResume(e -> {
                                return Mono.error(new CustomRunTimeException(ExceptionE.SERVER_ERROR));
                            });
                })
                .onErrorResume(e -> {
                    return Mono.error(new CustomRunTimeException(ExceptionE.SERVER_ERROR));
                });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Mono<Void> cancelTransportResources(String transportId, Long userId) {
        return resourceTransportRepository.findById(transportId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .flatMap(resourceTransport -> {
                    if (resourceTransport.getTransportType().equals(TransportType.OFFER) ||
                            resourceTransport.getState() == null || !resourceTransport.getState().equals(TransportResourceStatusEnum.GOING)) {
                        return Mono.error(new CustomRunTimeException(ExceptionE.CANCEL_TRANSPORT_ERROR));
                    }

                    return islandResourceRepository.findByIslandId(resourceTransport.getSenderIslandId())
                            .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                            .flatMap(islandResource -> {
                                if (!islandResource.getUserId().equals(userId)) {
                                    return Mono.error(new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES));
                                }
                                Duration duration = Duration.between(resourceTransport.getLocalDateTime(), LocalDateTime.now());
                                resourceTransport.addState(new ResourceTransportState(TransportResourceStatusEnum.CANCEL, duration));
                                resourceTransport.setCanceled(true);
                                return resourceTransportRepository.save(resourceTransport)
                                        .onErrorResume(e -> Mono.error(new CustomRunTimeException(ExceptionE.SERVER_ERROR)))
                                        .flatMap(resource -> {
                                            rabbitmqService.sendTradingCanceledMessage(
                                                    resourceTransport.getId(),
                                                    duration.toMillis()
                                            );
                                            return Mono.empty();
                                        })
                                        .then();
                            });
                })
                .onErrorResume(Mono::error);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Mono<IslandResourceDTO> createOffer(String islandId, RawMaterialsDTO offeringRawMaterialsDTO, RawMaterialsDTO wantedRawMaterials, Long userId) {
        return islandResourceRepository.findByIslandId(islandId)
                .switchIfEmpty(Mono.empty())
                .flatMap(islandResource -> {
                    if(!islandResource.getUserId().equals(userId)) {
                        throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
                    }
                    if(!islandResource.checkRawMaterialsAllocation(offeringRawMaterialsDTO)) {
                        throw new CustomRunTimeException(ExceptionE.INSUFFICIENT_RESOURCES);
                    }

                    return resourceOfferRepository.findByBidderIslandId(islandId)
                            .collectList()
                            .flatMap(resourceOfferList -> {
                                if(resourceOfferList.size() > 2) {
                                    throw new CustomRunTimeException(ExceptionE.SERVER_ERROR);
                                }
                                int shipNumber = islandResource.diminishingResource(offeringRawMaterialsDTO);
                                return islandResourceRepository.save(islandResource)
                                        .flatMap(islandResourceRecorded -> {
                                            ResourceOffer resourceOffer = new ResourceOffer(islandResource.getIslandId(), offeringRawMaterialsDTO, wantedRawMaterials, shipNumber,
                                                    LocalDateTime.now(), islandResourceRecorded.getServerId());
                                            return resourceOfferRepository.save(resourceOffer)
                                                    .then(Mono.empty());
                                        })
                                        .map(islandResourceRecorded -> modelMapper.map(islandResourceRecorded, IslandResourceDTO.class));
                            });
                })
                .onErrorResume(e -> Mono.error(e));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Flux<ResourceOfferDTO> getAllResourceOffer(String serverId, int pageNumber, int pageSize) {
        int skip = pageNumber * pageSize;

        Mono<Long> totalCount = resourceOfferRepository.countByServerId(serverId);

        Flux<ResourceOffer> paginatedResults = resourceOfferRepository.findByServerId(serverId)
                .skip(skip)
                .take(pageSize);

        return totalCount.flatMapMany(count ->
                paginatedResults.map(resourceOffer -> new ResourceOfferDTO(resourceOffer, pageNumber))
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Mono<IslandResourceDTO> acceptOffer(String offerId, String islandId, Long userId) {
        return resourceOfferRepository.findById(offerId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .flatMap(resourceOffer -> {
                    Mono<IslandResource> bidderIslandResourceMono = islandResourceRepository.findByIslandId(resourceOffer.getBidderIslandId());
                    Mono<IslandResource> acceptorIslandResourceMono = islandResourceRepository.findByIslandId(islandId);
                    return Mono.zip(bidderIslandResourceMono, acceptorIslandResourceMono)
                            .flatMap(Tuple -> {
                                IslandResource bidderIslandResource = Tuple.getT1();
                                IslandResource acceptorIslandResource = Tuple.getT2();
                                if(!bidderIslandResource.getServerId().equals(acceptorIslandResource.getServerId())) {
                                    throw new CustomRunTimeException(ExceptionE.SERVER_ERROR);
                                }
                                int acceptorShipNumber = acceptorIslandResource.diminishingResource(resourceOffer.getAcceptorRawMaterials());
                                return islandResourceRepository.save(acceptorIslandResource)
                                        .flatMap(acceptorIslandResourceRecorded -> {
                                            return whoAmIClient.getDistanceBetweenIslands(bidderIslandResource.getIslandId(), acceptorIslandResource.getIslandId())
                                                    .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                                                    .flatMap(distance -> {
                                                        long bidderDuration = (long) (1000 * distance * ((double) bidderIslandResource.getTradingTimeReductionPercentage() / 100));
                                                        long acceptorDuration = (long) (1000 * distance * ((double) acceptorIslandResource.getTradingTimeReductionPercentage() / 100));

                                                        long duration = (bidderDuration + acceptorDuration) / 2;

                                                        ResourceTransport bidderResourceTransport = new ResourceTransport(
                                                                bidderIslandResource.getIslandId(),
                                                                acceptorIslandResource.getIslandId(),
                                                                resourceOffer.getBidderRawMaterials(),
                                                                LocalDateTime.now(),
                                                                new ResourceTransportState(TransportResourceStatusEnum.GOING, Duration.ofMillis(duration)),
                                                                TransportType.TRANSPORT
                                                        );

                                                        ResourceTransport acceptorResourceTransport = new ResourceTransport(
                                                                acceptorIslandResource.getIslandId(),
                                                                bidderIslandResource.getIslandId(),
                                                                resourceOffer.getAcceptorRawMaterials(),
                                                                LocalDateTime.now(),
                                                                new ResourceTransportState(TransportResourceStatusEnum.GOING, Duration.ofMillis(duration)),
                                                                TransportType.TRANSPORT
                                                        );

                                                        acceptorResourceTransport.setShipNumber(acceptorShipNumber);
                                                        bidderResourceTransport.setShipNumber(resourceOffer.getBidderShipNumber());

                                                        Mono<ResourceTransport> saveBidderTransport = resourceTransportRepository.save(bidderResourceTransport);
                                                        Mono<ResourceTransport> saveAcceptorTransport = resourceTransportRepository.save(acceptorResourceTransport);

                                                        return Mono.zip(saveBidderTransport, saveAcceptorTransport)
                                                                .flatMap(transportTuple -> {
                                                                    ResourceTransport bidderTransport = transportTuple.getT1();
                                                                    ResourceTransport acceptorTransport = transportTuple.getT2();

                                                                    resourceOffer.setAcceptorIslandId(acceptorIslandResource.getId());

                                                                    resourceOffer.setBidderResourceTransportId(bidderTransport.getId());
                                                                    resourceOffer.setAcceptorResourceTransportId(acceptorTransport.getId());

                                                                    return resourceOfferRepository.save(resourceOffer)
                                                                            .flatMap(resourceOffer1 -> {
                                                                                rabbitmqService.sendTradingMessage(bidderTransport.getId(), bidderTransport.getDuration().toMillis());

                                                                                rabbitmqService.sendTradingMessage(acceptorTransport.getId(), acceptorTransport.getDuration().toMillis());
                                                                                return Mono.empty();
                                                                            })
                                                                            .map(acceptorIslandResource2 -> modelMapper.map(acceptorIslandResource, IslandResourceDTO.class));
                                                                });
                                                    });
                                        });
                            });
                });
    }

}
