package com.islandempires.resourcesservice.service.privates;

import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.dto.request.LootingResourcesRequestDTO;
import com.islandempires.resourcesservice.dto.request.ResourceAllocationRequestDTO;
import com.islandempires.resourcesservice.enums.BidderAcceptorEnum;
import com.islandempires.resourcesservice.enums.IslandResourceEnum;
import com.islandempires.resourcesservice.enums.PlunderedRaidingEnum;
import com.islandempires.resourcesservice.enums.TransportResourceStatusEnum;
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
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import com.islandempires.resourcesservice.rabbitmq.RabbitMqConfig;
import com.islandempires.resourcesservice.outbox.KafkaProducerService;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class IslandResourceServiceImplPrivateService implements IslandResourceQueryPrivateService, IslandResourceModificationPrivateService, IslandResourceInteractionPrivateService {
    @Autowired
    private IslandResourceRepository islandResourceRepository;

    @Autowired
    private IslandResourceCalculatorService islandResourceCalculatorService;

    @Autowired
    private IslandMetricsCalculatorService islandMetricsCalculatorService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RabbitmqService rabbitmqService;

    @Autowired
    private ResourceTransportRepository resourceTransportRepository;

    @Autowired
    private WhoAmIClient whoAmIClient;

    @Autowired
    private ResourceOfferRepository resourceOfferRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Override
    public Flux<IslandResourceDTO> getAll() {
        return islandResourceRepository.findAll().map(islandResource -> modelMapper.map(islandResource, IslandResourceDTO.class));
    }

    @Override
    public Mono<IslandResourceDTO> get(String islandId) {
        return this.islandResourceRepository.findByIslandId(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .map(islandResource -> {
                    return modelMapper.map(islandResource, IslandResourceDTO.class);
                });
    }

    @Override
    public Flux<IslandResourceDTO> getByUserId(Long userId, String serverId) {
        return islandResourceRepository.findByUserIdAndServerId(userId, serverId)
                .map(islandResource -> modelMapper.map(islandResource, IslandResourceDTO.class));
    }

    @Override
    public Mono<Void> delete(String islandId) {
        return islandResourceRepository.deleteByIslandId(islandId)
                .doOnError(error -> {
                    throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
                });
    }

    @Override
    public Mono<IslandResourceDTO> initializeIslandResource(String serverId, String islandId, IslandResourceDTO islandResourceDTO, Long userId) {
        return islandResourceRepository.findByIslandId(islandId).switchIfEmpty(Mono.defer(() -> {
            IslandResource islandResource = modelMapper.map(islandResourceDTO, IslandResource.class);
            islandResource.setLastCalculatedTimestamp(System.currentTimeMillis());
            islandResource.setCreatedDate(Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime());
            islandResource.setUserId(userId);
            islandResource.setServerId(serverId);
            islandResource.setIslandId(islandId);
            return islandResourceRepository.save(islandResource);
        })).flatMap(savedIslandResource -> Mono.just(modelMapper.map(savedIslandResource, IslandResourceDTO.class)));
    }

    @Override
    public Mono<Boolean> validateResourceAllocationForIsland(String islandId, ResourceAllocationRequestDTO resourceAllocationRequestDTO){
        return islandResourceRepository.findByIslandId(islandId)
                .map(islandResource -> {
                    return islandResourceCalculatorService.checkResourceAllocation(islandResource, resourceAllocationRequestDTO);
                }).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)));
    }

    @Override
    @Transactional
    public Mono<IslandResourceDTO> assignResources(String islandId, ResourceAllocationRequestDTO resourceAllocationRequestDTO) {
        return islandResourceRepository.findByIslandId(islandId)
                .map(islandResource -> modelMapper.map(
                        islandResourceCalculatorService.calculateResourceAllocation(
                                islandResource, resourceAllocationRequestDTO),
                        IslandResource.class))
                .flatMap(islandResourceRepository::save)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .flatMap(savedIslandResource -> kafkaProducerService.publishPopulationChange(savedIslandResource.getIslandId())
                        .thenReturn(savedIslandResource))
                .map(islandResource -> modelMapper.map(islandResource, IslandResourceDTO.class))
                .doOnError(Mono::error);
    }

    @Override
    @Transactional
    public Mono<IslandResourceDTO> refundResources(String islandId, ResourceAllocationRequestDTO resourceAllocationRequestDTO){
        return islandResourceRepository.findByIslandId(islandId)
                .map(islandResource -> {
                    return modelMapper.map(
                            islandResourceCalculatorService.refundResources(
                                    islandResource,resourceAllocationRequestDTO),
                            IslandResource.class);
                }).flatMap(islandResourceRepository::save).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .map(islandResource -> modelMapper.map(islandResource, IslandResourceDTO.class))
                .doOnError(Mono::error);
    }

    @Override
    public Mono<Tuple2<IslandResource, IslandResource>> looting(String plunderedIslandId, String raidingIslandId, LootingResourcesRequestDTO lootingResourcesRequestDTO){
        Mono<IslandResource> plunderedIslandResourceMono = islandResourceRepository.findByIslandId(plunderedIslandId).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)));
        Mono<IslandResource> raidingIslandResourceMono = islandResourceRepository.findByIslandId(raidingIslandId).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)));

        return Mono.zip(plunderedIslandResourceMono, raidingIslandResourceMono).flatMap(tuple -> {
            IslandResource plunderedIslandResource = tuple.getT1();
            IslandResource raidingIslandResource = tuple.getT2();

            IslandResourceDTO senderIslandResourceDTO =  modelMapper.map(plunderedIslandResource, IslandResourceDTO.class);
            IslandResourceDTO receiverIslandResourceDTO =  modelMapper.map(raidingIslandResource, IslandResourceDTO.class);

            Map<PlunderedRaidingEnum,IslandResourceDTO> senderAndReceiverIslandResourcesDTO
                    = islandResourceCalculatorService.calculateLooting(senderIslandResourceDTO, receiverIslandResourceDTO, lootingResourcesRequestDTO);

            return Mono.zip(
                    islandResourceRepository.save(modelMapper.map(senderAndReceiverIslandResourcesDTO.get(PlunderedRaidingEnum.PLUNDERED), IslandResource.class)),
                    islandResourceRepository.save(modelMapper.map(senderAndReceiverIslandResourcesDTO.get(PlunderedRaidingEnum.RAIDING), IslandResource.class))
            );
        });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Mono<Tuple2<IslandResourceDTO, IslandResourceDTO>> mutualTrading(String island1Id, String island2Id, RawMaterialsDTO island1TradingRawMaterials, RawMaterialsDTO island2TradingRawMaterials) {
        Mono<IslandResource> island1IslandResourceMono = islandResourceRepository.findByIslandId(island1Id).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)));
        Mono<IslandResource> island2IslandResourceMono = islandResourceRepository.findByIslandId(island2Id).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)));

        return Mono.zip(island1IslandResourceMono, island2IslandResourceMono).flatMap(tuple -> {
            IslandResource senderIslandResource = tuple.getT1();
            IslandResource receiverIslandResource = tuple.getT2();

            if(!senderIslandResource.checkRawMaterialsAllocation(island1TradingRawMaterials) ||
                !receiverIslandResource.checkRawMaterialsAllocation(island2TradingRawMaterials)) {
                throw new CustomRunTimeException(ExceptionE.INSUFFICIENT_RESOURCES);
            }

            senderIslandResource.diminishingResource(island1TradingRawMaterials);
            receiverIslandResource.diminishingResource(island2TradingRawMaterials);

            /*
            return Mono.zip(
                    islandResourceRepository.save(modelMapper.map(senderAndReceiverIslandResourcesDTO.get(MutualTradingEnum.ISLAND1), IslandResource.class))
                            .map(savedIslandResource -> modelMapper.map(savedIslandResource, IslandResourceDTO.class)),
                    islandResourceRepository.save(modelMapper.map(senderAndReceiverIslandResourcesDTO.get(MutualTradingEnum.ISLAND2), IslandResource.class))
                            .map(savedIslandResource -> modelMapper.map(savedIslandResource, IslandResourceDTO.class))
            );*/
            return Mono.zip(Mono.empty(), Mono.empty());
        });
    }

    @Override
    public Mono<IslandResourceDTO> updateIslandResourceField(String islandId, IslandResourceEnum islandResourceEnum, Number newValue) {
        return islandResourceRepository.findByIslandId(islandId).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND))).map(islandResource -> {
                    switch (islandResourceEnum) {
                        case WOOD_HOURLY_PRODUCTION -> islandResource.setWoodHourlyProduction(newValue.intValue());
                        case IRON_HOURLY_PRODUCTION -> islandResource.setIronHourlyProduction(newValue.intValue());
                        case CLAY_HOURLY_PRODUCTION -> islandResource.setClayHourlyProduction(newValue.intValue());
                        case RAW_MATERIAL_STORAGE_SIZE -> islandResource.setRawMaterialStorageSize(newValue.intValue());
                        case MEAT_HOURLY_PRODUCTION -> {
                            islandResource.setMeatHourlyProduction(newValue.intValue());
                            islandResource = islandMetricsCalculatorService.calculateIslandResourceFields(islandResource);
                        }
                        case FISH_HOURLY_PRODUCTION -> {
                            islandResource.setFishHourlyProduction(newValue.intValue());
                            islandResource = islandMetricsCalculatorService.calculateIslandResourceFields(islandResource);
                        }
                        case WHEAT_HOURLY_PRODUCTION -> {
                            islandResource.setWheatHourlyProduction(newValue.intValue());
                            islandResource = islandMetricsCalculatorService.calculateIslandResourceFields(islandResource);
                        }
                        case ADDITIONAL_HAPPINESS_SCORE -> {
                            islandResource.setAdditionalHappinessScore(newValue.doubleValue());
                            islandResource = islandMetricsCalculatorService.calculateIslandResourceFields(islandResource);
                        }
                        case CLAY_HOURL_PRODUCTION_MULTIPLY -> {
                            int totalClayHourlyProductionMultiply = islandResource.getClayHourlyProductionMultiply() + newValue.intValue();
                            totalClayHourlyProductionMultiply = Math.max(totalClayHourlyProductionMultiply, 0);
                            islandResource.setClayHourlyProductionMultiply(totalClayHourlyProductionMultiply);
                        }
                        case IRON_HOURL_PRODUCTION_MULTIPLY -> {
                            int totalIronHourlyProductionMultiply = islandResource.getIronHourlyProduction() + newValue.intValue();
                            totalIronHourlyProductionMultiply = Math.max(totalIronHourlyProductionMultiply, 0);
                            islandResource.setIronHourlyProductionMultiply(totalIronHourlyProductionMultiply);
                        }
                        default -> throw new CustomRunTimeException(ExceptionE.ENUM_NOT_FOUND);
                    }
            return islandResource;})
                .flatMap(updatedResource -> islandResourceRepository.save(updatedResource))
                .map(islandResource -> modelMapper.map(islandResource, IslandResourceDTO.class));
    }

    @Override
    public Mono<IslandResourceDTO> increaseOrDecreaseIslandResourceField(String islandId, IslandResourceEnum islandResourceEnum, Number value) {
        return islandResourceRepository.findByIslandId(islandId).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND))).map(islandResource -> {
                    switch (islandResourceEnum) {
                        case WOOD_HOURLY_PRODUCTION -> {
                            Integer totalWoodProduction = islandResource.getWoodHourlyProduction() + value.intValue();
                            totalWoodProduction = totalWoodProduction < 0 ? 0 : totalWoodProduction;
                            islandResource.setWoodHourlyProduction(totalWoodProduction);
                        }
                        case IRON_HOURLY_PRODUCTION -> {
                            Integer totalIronProduction = islandResource.getIronHourlyProduction() + value.intValue();
                            totalIronProduction = totalIronProduction < 0 ? 0 : totalIronProduction;
                            islandResource.setIronHourlyProduction(totalIronProduction);
                        }
                        case IRON_HOURL_PRODUCTION_MULTIPLY -> {
                            Integer totalIronHourlyProductionMultiply = islandResource.getIronHourlyProductionMultiply() + value.intValue();
                            totalIronHourlyProductionMultiply = totalIronHourlyProductionMultiply < 0 ? 0 : totalIronHourlyProductionMultiply;
                            islandResource.setIronHourlyProductionMultiply(totalIronHourlyProductionMultiply);
                        }
                        case CLAY_HOURLY_PRODUCTION -> {
                            Integer totalClayProduction = islandResource.getClayHourlyProduction() + value.intValue();
                            totalClayProduction = totalClayProduction < 0 ? 0 : totalClayProduction;
                            islandResource.setClayHourlyProduction(totalClayProduction);
                        }
                        case CLAY_HOURL_PRODUCTION_MULTIPLY -> {
                            Integer totalClayHourlyProductionMultiply = islandResource.getClayHourlyProductionMultiply() + value.intValue();
                            totalClayHourlyProductionMultiply = totalClayHourlyProductionMultiply < 0 ? 0 : totalClayHourlyProductionMultiply;
                            islandResource.setClayHourlyProductionMultiply(totalClayHourlyProductionMultiply);
                        }
                        case RAW_MATERIAL_STORAGE_SIZE -> {
                            Integer totalRawMaterialStorageSize = islandResource.getRawMaterialStorageSize() + value.intValue();
                            totalRawMaterialStorageSize = totalRawMaterialStorageSize < 0 ? 0 : totalRawMaterialStorageSize;
                            islandResource.setRawMaterialStorageSize(totalRawMaterialStorageSize);
                        }
                        case POPULATION -> {
                            Integer totalPopulation = islandResource.getPopulation() + value.intValue();
                            islandResource.setPopulation(totalPopulation < 0 ? 0 : totalPopulation);
                            islandResource = islandMetricsCalculatorService.calculateIslandResourceFields(islandResource);
                        }
                        case TEMPORARY_POPULATION -> {
                            Integer totalTemporaryPopulation = islandResource.getTemporaryPopulation() + value.intValue();
                            islandResource.setTemporaryPopulation(totalTemporaryPopulation);
                            islandResource = islandMetricsCalculatorService.calculateIslandResourceFields(islandResource);
                        }
                        case WHEAT_HOURLY_PRODUCTION -> {
                            Integer wheatHourlyProduction = islandResource.getWheatHourlyProduction() + value.intValue();
                            wheatHourlyProduction = wheatHourlyProduction < 0 ? 0 : wheatHourlyProduction;
                            islandResource.setWheatHourlyProduction(wheatHourlyProduction);
                            islandResource = islandMetricsCalculatorService.calculateIslandResourceFields(islandResource);
                        }
                        case FISH_HOURLY_PRODUCTION -> {
                            Integer fishHourlyProduction = islandResource.getFishHourlyProduction() + value.intValue();
                            fishHourlyProduction = fishHourlyProduction < 0 ? 0 : fishHourlyProduction;
                            islandResource.setFishHourlyProduction(fishHourlyProduction);
                            islandResource = islandMetricsCalculatorService.calculateIslandResourceFields(islandResource);
                        }
                        case MEAT_HOURLY_PRODUCTION -> {
                            Integer meatHourlyProduction = islandResource.getMeatHourlyProduction() + value.intValue();
                            meatHourlyProduction = meatHourlyProduction < 0 ? 0 : meatHourlyProduction;
                            islandResource.setMeatHourlyProduction(meatHourlyProduction);
                            islandResource = islandMetricsCalculatorService.calculateIslandResourceFields(islandResource);
                        }
                        case ADDITIONAL_HAPPINESS_SCORE -> {
                            Double additionalHappinessScore = islandResource.getAdditionalHappinessScore() + value.doubleValue();
                            additionalHappinessScore = additionalHappinessScore < 0 ? 0 : additionalHappinessScore;
                            islandResource.setAdditionalHappinessScore(additionalHappinessScore);
                            islandResource = islandMetricsCalculatorService.calculateHappinessScore(islandResource);
                        }
                        default -> throw new CustomRunTimeException(ExceptionE.ENUM_NOT_FOUND);
                    }
                    return islandResource;})
                .flatMap(updatedResource -> islandResourceRepository.save(updatedResource))
                .map(islandResource -> modelMapper.map(islandResource, IslandResourceDTO.class));
    }

    @Override
    public Mono<IslandResourceDTO> updateIslandTrading(String islandId, int totalShipNumber, long shipCapacity, int timeReductionPercentage) {
        return islandResourceRepository.findByIslandId(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .flatMap(islandResource -> {
                    islandResource.setShipNumber(totalShipNumber);
                    islandResource.setShipCapacity(shipCapacity);
                    islandResource.setTradingTimeReductionPercentage(timeReductionPercentage);
                    return islandResourceRepository.save(islandResource);
                })
                .map(islandResource -> modelMapper.map(islandResource, IslandResourceDTO.class));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Mono<Void> cancelTransportResources(String transportId, Long userId) {
        return resourceTransportRepository.findById(transportId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .flatMap(resourceTransport -> {
                    if (resourceTransport.getState() != null && !resourceTransport.getState().equals(TransportResourceStatusEnum.GOING)) {
                        return Mono.error(new CustomRunTimeException(ExceptionE.CANCEL_TRANSPORT_ERROR));
                    }
                    return islandResourceRepository.findById(resourceTransport.getSenderIslandId())
                            .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                            .flatMap(islandResource -> {
                                if (!islandResource.getUserId().equals(userId)) {
                                    return Mono.error(new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES));
                                }
                                Duration duration = Duration.between(LocalDateTime.now(), resourceTransport.getLocalDateTime());
                                resourceTransport.addState(new ResourceTransportState(TransportResourceStatusEnum.RETURNING, duration));
                                rabbitmqService.sendTradingReturningMessage(
                                        resourceTransport.getId(),
                                        duration.toMillis()
                                );
                                return resourceTransportRepository.save(resourceTransport)
                                        .onErrorResume(e -> Mono.error(new CustomRunTimeException(ExceptionE.SERVER_ERROR)))
                                        .then();
                            });
                })
                .onErrorResume(Mono::error);
    }


    @RabbitListener(queues = RabbitMqConfig.RESOURCE_TRANSPORT_QUEUE_NAME)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Mono<Void> transportResourcesGoing(String resourceTransportId) {
        return resourceTransportRepository.findById(removeFirstAndLast(resourceTransportId))
                .flatMap(resourceTransport -> {
                    if(resourceTransport.getState() != null && !resourceTransport.getState().equals(TransportResourceStatusEnum.GOING)) {
                        return Mono.empty();
                    }
                    return islandResourceRepository.findByIslandId(resourceTransport.getReceiverIslandId())
                            .flatMap(receiverIslandResource -> {
                                receiverIslandResource.addResource(resourceTransport.getRawMaterials());
                                return islandResourceRepository.save(receiverIslandResource)
                                        .flatMap(recordedReceiverIslandResource -> {
                                            ResourceTransportState resourceTransportState = new ResourceTransportState(
                                                    TransportResourceStatusEnum.RETURNING,
                                                    resourceTransport.getDuration()
                                            );
                                            resourceTransport.addState(resourceTransportState);
                                            return resourceTransportRepository.save(resourceTransport)
                                                    .flatMap(recordedResourceTransport -> {
                                                        rabbitmqService.sendTradingReturningMessage(
                                                                recordedResourceTransport.getId(),
                                                                recordedResourceTransport.getDuration().toMillis()
                                                        );
                                                        return Mono.empty();
                                                    })
                                                    .onErrorResume(e -> Mono.empty());
                                        })
                                        .onErrorResume(e -> Mono.empty());
                            })
                            .switchIfEmpty(Mono.empty())
                            .onErrorResume(e -> {
                                return Mono.empty();
                            });
                })
                .switchIfEmpty(Mono.empty())
                .onErrorResume(e -> {
                    return Mono.empty();
                })
                .then();
    }

    public String removeFirstAndLast(String str) {
        if (str == null || str.length() < 2) {
            return "";
        }
        return str.substring(1, str.length() - 1);
    }

    @RabbitListener(queues = RabbitMqConfig.RESOURCE_TRANSPORT_RETURNING_QUEUE_NAME)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Mono<Void> transportResourcesDone(String resourceTransportId) {
        return resourceTransportRepository.findById(removeFirstAndLast(resourceTransportId))
                .switchIfEmpty(Mono.empty())
                .flatMap(resourceTransport -> {
                    if(resourceTransport.isCanceled()) {
                        return Mono.empty();
                    }
                    return islandResourceRepository.findByIslandId(resourceTransport.getSenderIslandId())
                                            .switchIfEmpty(Mono.empty())
                                            .flatMap(islandResource -> {
                                                islandResource.addShips(resourceTransport.getShipNumber());
                                                return islandResourceRepository.save(islandResource)
                                                        .onErrorResume(e -> Mono.empty())
                                                        .flatMap(islandResourceRecorded -> {
                                                            resourceTransport.addState(new ResourceTransportState(TransportResourceStatusEnum.DONE, null));
                                                            return resourceTransportRepository.save(resourceTransport)
                                                                    .onErrorResume(e -> Mono.empty());
                                                        });
                                            });
                })
                .onErrorResume(e -> Mono.empty())
                .then();
    }

    @RabbitListener(queues = RabbitMqConfig.RESOURCE_TRANSPORT_CANCELED_QUEUE_NAME)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Mono<Void> cancelTransportResourcesDone(String canceledTransportId) {
        return resourceTransportRepository.findById(removeFirstAndLast(canceledTransportId))
                .switchIfEmpty(Mono.empty())
                .flatMap(resourceTransport -> {
                    if(!resourceTransport.isCanceled()) {
                        return Mono.empty();
                    }

                    return islandResourceRepository.findByIslandId(resourceTransport.getSenderIslandId())
                                    .switchIfEmpty(Mono.empty())
                                    .flatMap(senderIslandResource -> {
                                        senderIslandResource.addResource(resourceTransport.getRawMaterials());
                                        senderIslandResource.addShips(resourceTransport.getShipNumber());
                                        return islandResourceRepository.save(senderIslandResource)
                                                .onErrorResume(e -> Mono.empty())
                                                .flatMap(islandResourceRecorded -> {
                                                    resourceTransport.addState(new ResourceTransportState(TransportResourceStatusEnum.DONE, null));
                                                    return resourceTransportRepository.save(resourceTransport)
                                                            .onErrorResume(e -> Mono.empty());
                                                });
                                    });

                })
                .onErrorResume(e -> Mono.empty())
                .then();
    }

    /*
    @RabbitListener(queues = RabbitMqConfig.OFFER_RESOURCE_TRANSPORT_QUEUE_NAME)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Mono<Void> transportOfferResourcesGoing(String offerId) {
        String[] parts = removeFirstAndLast(offerId).split(":");
        if (parts.length != 2) {
            return Mono.empty();
        }

        String id = parts[0];
        BidderAcceptorEnum role;

        try {
            role = BidderAcceptorEnum.valueOf(parts[1]);
        } catch (IllegalArgumentException e) {
            return Mono.empty();
        }


        return resourceOfferRepository.findById(id)
                .flatMap(resourceOffer -> {
                    ResourceTransport resourceTransport;
                    if(role.equals(BidderAcceptorEnum.ACCEPTOR)) {
                        resourceTransport = resourceOffer.getAcceptorResourceTransport();
                    } else {
                        resourceTransport = resourceOffer.getBidderResourceTransport();
                    }

                    if(resourceTransport.getState() != null && !resourceTransport.getState().equals(TransportResourceStatusEnum.GOING)) {
                        return Mono.empty();
                    }
                    return islandResourceRepository.findByIslandId(resourceTransport.getReceiverIslandId())
                            .flatMap(receiverIslandResource -> {
                                receiverIslandResource.addResource(resourceTransport.getRawMaterials());
                                return islandResourceRepository.save(receiverIslandResource)
                                        .flatMap(recordedReceiverIslandResource -> {
                                            ResourceTransportState resourceTransportState = new ResourceTransportState(
                                                    TransportResourceStatusEnum.RETURNING,
                                                    resourceTransport.getDuration()
                                            );
                                            resourceTransport.addState(resourceTransportState);
                                            return resourceOfferRepository.save(resourceOffer)
                                                    .flatMap(recordedResourceTransport -> {
                                                        rabbitmqService.sendOfferResourceTransportReturningMessage(
                                                                recordedResourceTransport.getId(),
                                                                role,
                                                                resourceTransport.getDuration().toMillis()
                                                        );
                                                        return Mono.empty();
                                                    })
                                                    .onErrorResume(e -> Mono.empty());
                                        })
                                        .onErrorResume(e -> Mono.empty());
                            })
                            .switchIfEmpty(Mono.empty())
                            .onErrorResume(e -> {
                                return Mono.empty();
                            });
                })
                .switchIfEmpty(Mono.empty())
                .onErrorResume(e -> {
                    return Mono.empty();
                })
                .then();
    }

    @RabbitListener(queues = RabbitMqConfig.OFFER_RESOURCE_TRANSPORT_RETURNING_QUEUE_NAME)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Mono<Void> transportOfferResourcesDone(String resourceTransportId) {
        String[] parts = removeFirstAndLast(resourceTransportId).split(":");
        if (parts.length != 2) {
            return Mono.empty();
        }

        String id = parts[0];
        BidderAcceptorEnum role;

        try {
            role = BidderAcceptorEnum.valueOf(parts[1]);
        } catch (IllegalArgumentException e) {
            return Mono.empty();
        }

        return resourceOfferRepository.findById(id)
                .switchIfEmpty(Mono.empty())
                .flatMap(resourceOffer -> {
                    ResourceTransport resourceTransport;
                    if(role.equals(BidderAcceptorEnum.ACCEPTOR)) {
                        resourceTransport = resourceOffer.getAcceptorResourceTransport();
                    } else {
                        resourceTransport = resourceOffer.getBidderResourceTransport();
                    }
                    return islandResourceRepository.findByIslandId(resourceTransport.getSenderIslandId())
                            .switchIfEmpty(Mono.empty())
                            .flatMap(islandResource -> {
                                islandResource.addShips(resourceTransport.getShipNumber());
                                return islandResourceRepository.save(islandResource)
                                        .onErrorResume(e -> Mono.empty())
                                        .flatMap(islandResourceRecorded -> {
                                            resourceTransport.addState(new ResourceTransportState(TransportResourceStatusEnum.DONE, null));
                                            return resourceOfferRepository.save(resourceOffer)
                                                    .onErrorResume(e -> Mono.empty());
                                        });
                            });
                })
                .onErrorResume(e -> Mono.empty())
                .then();
    }
*/
    @Override
    public Flux<IslandResource> addTest() {
        List<IslandResource> islands = new ArrayList<>();

        Random random = new Random();
        LocalDateTime timestampNow = LocalDateTime.now();


        for(int i=0; i<10000; i++) {
            IslandResource island1 = new IslandResource();
            island1.setIslandId("test".concat(i+""));
            island1.setWood(Double.valueOf(random.nextInt(200)));
            island1.setIron(Double.valueOf(random.nextInt(200)));
            island1.setClay(Double.valueOf(random.nextInt(200)));
            island1.setRawMaterialStorageSize(random.nextInt(500 - 201) + 201);
            island1.setWoodHourlyProduction(random.nextInt(100 - 70) + 70);
            island1.setIronHourlyProduction(random.nextInt(100 - 60) + 60);
            island1.setClayHourlyProduction(random.nextInt(100 - 60) + 60);

            island1.setMeatFoodCoefficient(200.0);
            island1.setMeatHourlyProduction(150);
            island1.setFishFoodCoefficient(200.0);
            island1.setFishHourlyProduction(150);
            island1.setWheatFoodCoefficient(200.0);
            island1.setWheatHourlyProduction(150);

            island1.setPopulation(random.nextInt(100 - 60) + 60);
            island1.setTemporaryPopulation(random.nextInt(3 - 0) + 0);
            island1.setPopulationLimit(random.nextInt(200 - 63) + 63);

            island1.setAdditionalHappinessScore(0.0);

            island1.setLastCalculatedTimestamp(System.currentTimeMillis());
            island1.setCreatedDate(timestampNow);

            island1 = islandMetricsCalculatorService.calculateIslandResourceFields(island1);
            islands.add(island1);
        }
        return this.islandResourceRepository.saveAll(islands);
    }

    @Override
    public Flux<IslandResourceDTO> getPopulations(String serverId, Long userId) {
        return islandResourceRepository.findByUserIdAndServerId(userId, serverId)
                .flatMap(islandResource -> Mono.just(modelMapper.map(islandResource, IslandResourceDTO.class)));
    }
}
