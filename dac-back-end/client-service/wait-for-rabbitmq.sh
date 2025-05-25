#!/bin/sh

# Aguarda até conseguir conectar na porta 5672 do rabbitmq
until nc -z rabbitmq 5672; do
  echo "Waiting for RabbitMQ..."
  sleep 2
done

echo "RabbitMQ is up, starting app..."
exec "$@"
