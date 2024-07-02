#!/bin/bash

# Öldürmek istediğiniz portlar
ports=(1001 9000 2002 2000)

# Her bir port için işlemleri bul ve öldür
for port in "${ports[@]}"
do
    echo "Port $port üzerinde çalışan işlemler bulunuyor..."
    # Lsof kullanarak port üzerinde çalışan işlemin PID'sini al
    pids=$(lsof -ti :$port)

    if [ -z "$pids" ]; then
        echo "Port $port üzerinde çalışan işlem bulunamadı."
    else
        for pid in $pids
        do
            echo "Process (PID: $pid) öldürülüyor..."
            kill -9 $pid
            echo "Process (PID: $pid) öldürüldü."
        done
    fi
done

echo "Belirtilen portlardaki tüm işlemler öldürüldü."
