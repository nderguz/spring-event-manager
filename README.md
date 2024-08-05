Запуск приложения в контейнерах:

docker-compose-up для первого запуска

docker-compose up --build  для пересборки образов контейнеров

Если нужно собрать только один сервис event-manager:

docker build -t event-manager:latest .

docker run -d --name event-manager -p 8080:8080 --network="host" event-manager:latest

Настройка kafka:

https://github.com/conduktor/kafka-stack-docker-compose