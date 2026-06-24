MSAVION:

json post avión: 
{
    "matricula": "CC-GUE",
    "marca": "Lockheed",
    "modelo": "F-16",
    "tipo": { "id_tipo": 3 },
    "fabricante": { "id_fabricante": 3 },
    "origen": { "id_origen": 3 },
    "alcance_km": 3200.0,
    "envergadura_metros": 9.45,
    "capacidad_combustible": 3162.0
}
--------------------
json post origen:
{
    "pais_origen": "Alemania"
}
--------------------
json post fabricante:
{
    "nombre_fabricante": "Lockheed Martin"
}
--------------------
json post tipo:
{
    "tipo": "GUERRA"
}
LINK SWAGGER: http://localhost:8082/doc/swagger-ui/index.html

URLS:
http://localhost:8082/api/v1/aviones
http://localhost:8082/api/v1/fabricantes
http://localhost:8082/api/v1/origenes
http://localhost:8082/api/v1/tipos
-----------------------------------------------------------------------
MSPILOTO:

json post curso:
{
    "nombre_curso": "Navegación Aérea Avanzada"
}
--------------------
json post piloto:
{
    "rut": "9.876.543-2",
    "nombre": "María",
    "apellido": "González",
    "fecha_nacimiento": "1985-11-22",
    "horas_vuelo": 850
}
--------------------
json post cursos:
{
    "piloto": { "ID_piloto": 3 },
    "curso": { "ID_curso": 3 }
}
LINK SWAGGER: http://localhost:8083/doc/swagger-ui/index.html

URLS:
http://localhost:8083/api/v1/pilotos
http://localhost:8083/api/v1/cursos
http://localhost:8083/api/v1/cursos_piloto
-----------------------------------------------------------------------
MSUBICACION:
json post región:
{
    "region": "Región de magallanes"
}
--------------------
json post comuna:
{
    "comuna": "Punta Arenas",
    "region": { "ID_region": 3 }
}
--------------------
json post aeródromo:
{
    "nombre_aerodromo": "Aeródromo Punta Arenas",
    "comuna": { "ID_comuna": 3 }
}
LINK SWAGGER: http://localhost:8081/doc/swagger-ui/index.html

URLS:
http://localhost:8081/api/v1/regiones
http://localhost:8081/api/v1/comunas
http://localhost:8081/api/v1/aerodromos
-----------------------------------------------------------------------
MSVUELO:
json post avionPiloto:
{
    "idPiloto": 3,
    "idAvion": 3
}
--------------------
json post vuelo:
{
    "numero_vuelo": "GU001",
    "hora_inicio_vuelo": "2026-07-15T08:00:00",
    "tipo_vuelo": "Vuelo Militar",
    "destino": "Base Aerea Punta Arenas",
    "idPiloto": 3,
    "idAvion": 3,
    "idAerodromo": 3
}
LINK SWAGGER: http://localhost:8084/doc/swagger-ui/index.html

URLS:
http://localhost:8084/api/v1/vuelos
http://localhost:8084/api/v1/avionesPiloto

----------------------------------------------------------------------

SWAGGER GENERAL GATEWAY: http://localhost:8080/swagger-ui/index.html

EUREKA: http://localhost:8761

----------------------------------------------------------------------
                                AVANCES
----------------------------------------------------------------------

Cristian = Hateoas, Swagger, Eureka, Gateway, Tests de: msavion, mspiloto, msubicacion
Martin = msvuelo, Tests, Services(msvuelo), Logs(msavion, ,mspiloto, msubicacion, msvuelo)
Diego =  Faker, test, msvuelo, log(ayude), sirvice test(piloto y avion) 

