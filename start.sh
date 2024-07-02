#!/bin/bash

projects=(
    "/Users/alipeker/Desktop/my/IslandEmpiresBackend/eurekaserver"
    "/Users/alipeker/Desktop/my/IslandEmpiresBackend/auth-service"
    "/Users/alipeker/Desktop/my/IslandEmpiresBackend/gateway2",
    "/Users/alipeker/Desktop/my/IslandEmpiresBackend/island-service",
    "/Users/alipeker/Desktop/my/IslandEmpiresBackend/resources-service"
)

# Proje PID'lerini saklamak için bir dizi oluştur
pids=()

# Her bir proje dizininde Gradle bootRun komutunu arka planda çalıştır
for project_dir in "${projects[@]}"
do
    echo "Proje dizinine gidiliyor: $project_dir"
    cd "$project_dir" || { echo "Dizin değiştirilemedi: $project_dir"; exit 1; }
    echo "Gradle projesi bootRun ile arka planda çalıştırılıyor: $project_dir"
    ./gradlew bootRun &
    pid=$!
    pids+=($pid)
    echo "Gradle bootRun başlatıldı (PID: $pid): $project_dir"
done
