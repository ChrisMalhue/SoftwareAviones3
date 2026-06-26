#!/bin/bash

echo "Iniciando Servidor de Descubrimiento Eureka (Puerto 8761)..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/eureka\" && ./mvnw spring-boot:run"'

echo "Esperando 12 segundos a que Eureka se estabilice..."
sleep 12

echo "Iniciando API Gateway..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/gateway\" && ./mvnw spring-boot:run"'

echo "Iniciando Microservicio Avion..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/msavion/msavion\" && ./mvnw spring-boot:run"'

echo "Iniciando Microservicio Piloto..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/mspiloto/mspiloto\" && ./mvnw spring-boot:run"'

echo "Iniciando Microservicio Ubicacion..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/msubicacion/msubicacion\" && ./mvnw spring-boot:run"'

echo "Iniciando Microservicio Vuelo..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/msvuelo/msvuelo\" && ./mvnw spring-boot:run"'

echo "Ecosistema lanzado. Dashboard disponible en http://localhost:8761"