package com.islandempires.islandservice.service.privates.impl;

import com.islandempires.islandservice.dto.Coord;
import com.islandempires.islandservice.enums.CardinalDirectionsEnum;
import com.islandempires.islandservice.exception.CustomRunTimeException;
import com.islandempires.islandservice.exception.ExceptionE;
import com.islandempires.islandservice.model.CoordinateGenerator;
import com.islandempires.islandservice.model.CoordinateValues;
import com.islandempires.islandservice.repository.CoordinateGeneratorRepository;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class IslandCoordinateGenerator {
    @Autowired
    private CoordinateGeneratorRepository coordinateGeneratorRepository;

    private List<Integer> random = Arrays.asList(2, 3, 4);

    public Mono<CoordinateGenerator> get(String serverId) {
        return coordinateGeneratorRepository.findByServerId(serverId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)));
    }

    public Mono<Coord> calculateIslandCoordinates(String serverId, CardinalDirectionsEnum cardinalDirectionsEnum) {
        return get(serverId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .flatMap(coordinateGenerator ->  {
                    CoordinateValues coordinateValues = getValues(coordinateGenerator, cardinalDirectionsEnum);
                    int t = coordinateValues.getT();
                    int lastActiveX = coordinateValues.getLastActiveX();
                    int lastActiveY = coordinateValues.getLastActiveY();
                    int lastX = coordinateValues.getLastX();
                    int lastY = coordinateValues.getLastY();
                    int c = coordinateValues.getC();

                    if(cardinalDirectionsEnum.equals(CardinalDirectionsEnum.NORTH_WEST)) {
                        for (int i = 0; i < getRandomElement(); i++) {
                            t++;
                            if (lastActiveY == lastX) {
                                lastActiveX -= 1;
                                lastY = lastActiveY;
                                lastX = lastActiveX;

                                t = 0;
                                c += 1;

                            } else {
                                if (lastY == lastActiveY - c) {
                                    lastX += 1;
                                } else {
                                    lastY -= 1;
                                }
                            }
                        }
                    } else if(cardinalDirectionsEnum.equals(CardinalDirectionsEnum.NORTH_EAST)) {
                        for (int i = 0; i < getRandomElement(); i++) {
                            t++;
                            if (lastActiveY + 1 == lastX) {
                                lastActiveX += 1;
                                lastY = lastActiveY;
                                lastX = lastActiveX;

                                t = 0;
                                c += 1;

                            } else {
                                if (lastY == lastActiveY - c) {
                                    lastX -= 1;
                                } else {
                                    lastY -= 1;
                                }
                            }
                        }
                    } else if(cardinalDirectionsEnum.equals(CardinalDirectionsEnum.SOUTH_WEST)) {
                        for (int i = 0; i < getRandomElement(); i++) {
                            t++;
                            if (lastActiveX + 1 == lastY) {
                                lastActiveY += 1;
                                lastY = lastActiveY;
                                lastX = lastActiveX;

                                t = 0;
                                c += 1;

                            } else {
                                if (lastX == lastActiveX - c) {
                                    lastY -= 1;
                                } else {
                                    lastX -= 1;
                                }
                            }
                        }
                    } else if(cardinalDirectionsEnum.equals(CardinalDirectionsEnum.SOUTH_EAST)) {
                        for (int i = 0; i < getRandomElement(); i++) {
                            t++;
                            if (lastActiveX == lastY) {
                                lastActiveY += 1;
                                lastY = lastActiveY;
                                lastX = lastActiveX;

                                t = 0;
                                c += 1;

                            } else {
                                if (lastX == lastActiveX + c) {
                                    lastY -= 1;
                                } else {
                                    lastX += 1;
                                }
                            }
                        }
                    }


                    coordinateValues.setLastActiveY(lastActiveY);
                    coordinateValues.setLastX(lastX);
                    coordinateValues.setLastY(lastY);
                    coordinateValues.setT(t);
                    coordinateValues.setC(c);

                    return coordinateGeneratorRepository.save(coordinateGenerator)
                            .thenReturn(new Coord(lastX, lastY));
                });
    }

    private CoordinateValues getValues(CoordinateGenerator coordinateGenerator, CardinalDirectionsEnum cardinalDirectionsEnum) {
        if(cardinalDirectionsEnum.equals(CardinalDirectionsEnum.NORTH_WEST)) {
            return coordinateGenerator.getNorthWest();
        } else if(cardinalDirectionsEnum.equals(CardinalDirectionsEnum.NORTH_EAST)) {
            return coordinateGenerator.getNorthEast();
        } else if(cardinalDirectionsEnum.equals(CardinalDirectionsEnum.SOUTH_WEST)) {
            return coordinateGenerator.getSouthWest();
        } else if(cardinalDirectionsEnum.equals(CardinalDirectionsEnum.SOUTH_EAST)) {
            return coordinateGenerator.getSouthEast();
        } else {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }
    }

    private int getRandomElement() {
        int randomIndex = new Random().nextInt(random.size());
        return random.get(randomIndex);
    }
}
