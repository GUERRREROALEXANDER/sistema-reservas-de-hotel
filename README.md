# Sistema de Reservas de Hotel

Backend REST para la gestión de reservas de un hotel, construido con **Spring Boot 3.5.6**, **Java 21** y **Maven**. El sistema permite consultar disponibilidad, crear reservas, añadir servicios adicionales, realizar check-in/check-out y generar la factura final.

No utiliza base de datos: la información se guarda en memoria con repositorios basados en `ConcurrentHashMap`, por lo que los datos se reinician al volver a arrancar la aplicación.

## Patrón Facade

El proyecto está diseñado alrededor del patrón **Facade**. El controlador HTTP no conoce los servicios internos: depende únicamente de `HotelFacade`, que expone una interfaz simple y uniforme (crear reserva, consultar disponibilidad, añadir servicios, check-in, check-out y obtener reserva) y coordina a los 5 servicios del subsistema.

```
                         ┌──────────────────┐
                         │  HotelController │   /api/hotel
                         └────────┬─────────┘
                                  │
                                  ▼
                         ┌──────────────────┐
                         │   HotelFacade    │   única puerta de entrada
                         └────────┬─────────┘
        ┌────────────┬────────────┼────────────┬────────────┐
        ▼            ▼            ▼            ▼            ▼
 ┌────────────┐ ┌──────────┐ ┌───────────┐ ┌──────────┐ ┌────────────┐
 │ RoomService│ │Pricing   │ │Additional │ │ Billing  │ │ Access     │
 │            │ │Service   │ │Service    │ │ Service  │ │ Service    │
 └────────────┘ └──────────┘ └───────────┘ └──────────┘ └────────────┘
  habitaciones  precios/      catálogo    facturas     llave
  y reservas    temporadas    de servicios               digital
```

**Ventajas:** el cliente (controlador) no necesita conocer la complejidad interna, se evita el acoplamiento, y cualquier cambio en un servicio solo afecta a la fachada.

## Estructura del proyecto

```
src/main/java/com/hotel/reservation
├── domain/model      Room (abstracto), SingleRoom, DoubleRoom, SuiteRoom, RoomType, RoomStatus,
│                     Reservation, ReservationStatus, AdditionalService, SpaService,
│                     BreakfastService, AirportTransportService, ServiceType, Invoice, InvoiceLine, Season
├── dto               ReservationDTO, ReservationResponseDTO, RoomDTO, AdditionalServiceRequestDTO,
│                     AdditionalServiceDTO, CheckInResponseDTO, InvoiceDTO, InvoiceLineDTO, ErrorResponseDTO
├── exception         ResourceNotFoundException, RoomNotAvailableException,
│                     InvalidReservationStateException, InvalidRequestException, GlobalExceptionHandler
├── repository        RoomRepository, ReservationRepository (interfaces)
│   └── memory        InMemoryRoomRepository, InMemoryReservationRepository
├── config            DataInitializer (siembra 15 habitaciones), PricingProperties, AppConfig
├── service           RoomService, PricingService, AdditionalServiceService, BillingService, AccessService
│   └── impl          RoomServiceImpl, PricingServiceImpl, AdditionalServiceServiceImpl,
│                     BillingServiceImpl, AccessServiceImpl
├── facade            HotelFacade
└── controller        HotelController
```

## Ejecución local

Requisitos: JDK 21 y Maven 3.9+.

```bash
mvn spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`. El puerto también se puede cambiar con la variable de entorno `PORT`.

Verificación rápida del estado:

```bash
curl http://localhost:8080/actuator/health
```

## Endpoints

| Método | Ruta | Descripción | Código |
|--------|------|-------------|--------|
| POST | `/api/hotel/reservar` | Crear una reserva | 201 |
| GET | `/api/hotel/disponibilidad` | Consultar habitaciones disponibles | 200 |
| POST | `/api/hotel/servicios/{reservationId}` | Añadir un servicio a la reserva | 200 |
| PUT | `/api/hotel/checkin/{reservationId}` | Check-in y llave digital | 200 |
| PUT | `/api/hotel/checkout/{reservationId}` | Check-out y factura | 200 |
| GET | `/api/hotel/reserva/{reservationId}` | Obtener una reserva | 200 |

### 1. Crear reserva

```bash
curl -X POST http://localhost:8080/api/hotel/reservar \
  -H "Content-Type: application/json" \
  -d '{
    "guestName": "Alexander Guerrero",
    "guestEmail": "alexander@example.com",
    "roomType": "SUITE",
    "checkInDate": "2026-12-20",
    "checkOutDate": "2026-12-24"
  }'
```

Campos: `guestName`, `guestEmail`, `roomType` (`SINGLE`, `DOUBLE` o `SUITE`), `checkInDate`, `checkOutDate` (formato `YYYY-MM-DD`).

### 2. Consultar disponibilidad

```bash
curl "http://localhost:8080/api/hotel/disponibilidad?checkIn=2026-12-20&checkOut=2026-12-24&roomType=SUITE"
```

Parámetros de consulta: `checkIn`, `checkOut` (obligatorios) y `roomType` (opcional; si se omite se devuelven todos los tipos).

### 3. Añadir servicio adicional

```bash
curl -X POST http://localhost:8080/api/hotel/servicios/{reservationId} \
  -H "Content-Type: application/json" \
  -d '{"serviceType": "SPA"}'
```

Valores de `serviceType`: `SPA`, `BREAKFAST`, `AIRPORT_TRANSPORT`.

### 4. Check-in

```bash
curl -X PUT http://localhost:8080/api/hotel/checkin/{reservationId}
```

Devuelve la llave digital (`digitalKey`) de la habitación.

### 5. Check-out

```bash
curl -X PUT http://localhost:8080/api/hotel/checkout/{reservationId}
```

Devuelve la factura (`InvoiceDTO`) con el desglose completo.

### 6. Obtener reserva

```bash
curl http://localhost:8080/api/hotel/reserva/{reservationId}
```

## Reglas de precios

**Temporada alta:** meses **6, 7 y 12** (junio, julio y diciembre). Cada noche que cae en un mes de temporada alta cuesta `precio base x 1.5`; las demás noches cuestan el precio base.

**Precio base por noche:**

| Tipo de habitación | Capacidad | Precio base |
|--------------------|-----------|-------------|
| Single | 1 | $80.00 |
| Double | 2 | $120.00 |
| Suite | 4 | $250.00 |

**Servicios adicionales:**

| Servicio | Costo |
|----------|-------|
| Spa (`SPA`) | $50.00 fijo |
| Desayuno (`BREAKFAST`) | $15.00 por noche |
| Transporte aeropuerto (`AIRPORT_TRANSPORT`) | $30.00 fijo |

El costo total estimado de una reserva es: `costo de habitación (noche a noche) + suma de servicios adicionales`.

## Despliegue en Railway

1. Sube el proyecto a un repositorio de **GitHub**.
2. En [Railway](https://railway.app), crea un **New Project**.
3. Selecciona **Deploy from GitHub repo** y elige el repositorio del proyecto.
4. Railway detecta el `Dockerfile` de la raíz y construye la imagen automáticamente (el `railway.json` configura el builder `DOCKERFILE`, el healthcheck `/actuator/health` y el reinicio `ON_FAILURE`).
5. La variable `PORT` se inyecta automáticamente; la aplicación la lee para escuchar en ese puerto (en local usa `8080` como valor por defecto).
6. Una vez desplegado, pulsa **Generate Domain** para obtener una URL pública.
7. Prueba el endpoint de salud: `https://<tu-dominio>.up.railway.app/actuator/health`.

## Equipo

- GUERRREROALEXANDER
- 0ValentinaArt
- MatUCC
