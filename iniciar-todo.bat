@echo off

echo Iniciando Servidor de Descubrimiento Eureka (Puerto 8761)...
cd eureka
start cmd /k "mvnw spring-boot:run"

echo Esperando 12 segundos a que Eureka se estabilice...
timeout /t 12 /nobreak > null

echo Iniciando API Gateway...
cd ../gateway
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Avion...
cd ../msavion/msavion
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Piloto...
cd ../../mspiloto/mspiloto
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Ubicacion...
cd ../../msubicacion/msubicacion
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Vuelo...
cd ../../msvuelo/msvuelo
start cmd /k "mvnw spring-boot:run"

echo Ecosistema lanzado. Dashboard disponible en http://localhost:8761