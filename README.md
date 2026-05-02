# Movie Service - Sistema de Reservas de Cine

<p align="center">
  <a href="https://spring.io/projects/spring-boot">
    <img src="https://img.shields.io/badge/Spring_Boot-3.5.11-6DB33F?style=flat&logo=spring" alt="Spring Boot"/>
  </a>
  <a href="https://www.java.com/">
    <img src="https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=openjdk&logoColor=white" alt="Java 21"/>
  </a>
  <a href="https://www.postgresql.org/">
    <img src="https://img.shields.io/badge/PostgreSQL-15-336791?style=flat&logo=postgresql&logoColor=white" alt="PostgreSQL"/>
  </a>
  <a href="https://jwt.io/">
    <img src="https://img.shields.io/badge/JWT-000000?style=flat&logo=JSON%20Web%20Tokens&logoColor=white" alt="JWT"/>
  </a>
  <a href="https://swagger.io/">
    <img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=swagger&logoColor=white" alt="Swagger"/>
  </a>
  <a href="https://docker.com">
    <img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white" alt="Docker"/>
  </a>
  <a href="https://projectlombok.org/">
    <img src="https://img.shields.io/badge/Lombok-FF0000?style=flat&logo=lombok&logoColor=white" alt="Lombok"/>
  </a>
</p>

---

## Tabla de Contenidos

1. [Funcionamiento](#funcionamiento)
2. [Tecnologías Empleadas](#tecnologías-empleadas)
3. [Arquitectura del Sistema](#arquitectura-del-sistema)
4. [Modelos de Datos](#modelos-de-datos)
5. [Endpoints Principales](#endpoints-principales)
6. [Motivación](#motivación)
7. [Manual de Instalación y Deployment](#manual-de-instalación-y-deployment)
8. [Ejemplos de Uso](#ejemplos-de-uso)
9. [Roadmap](#roadmap)

---

## Funcionamiento

Movie Service es una **API REST** para un sistema completo de reservas de cine. El sistema permite a los usuarios registrados reservar asientos para funciones de películas, mientras que los administradores pueden gestionar el catálogo de películas, salas y funciones.

### Funcionalidades Principales

#### Autenticación y Usuarios
- **Registro de usuarios**: Creación de nuevas cuentas con validación de email y username
- **Login**: Autenticación con credenciales seguras
- **Tokens JWT**: Seguridad stateless con JSON Web Tokens
- **Roles**: Sistema de permisos con roles USER y ADMIN

#### Gestión de Películas
- Crear, leer, actualizar y eliminar películas
- Filtrado por género
- Búsqueda por título
- Paginación de resultados

#### Gestión de Salas y Funciones
- CRUD completo de salas de cine (theaters)
- Creación de funciones (showtimes) asociadas a películas y salas
- Validación de horario para evitar overlap de funciones
- Configuración de asientos por sala

#### Sistema de Reservas
- Reserva de asientos disponibles para una función específica
- Cálculo automático del precio total basado en el tipo de asiento
- Cancelación de reservas
- Historial de reservas propias
- Validación de asientos ya ocupados

#### Panel de Administración
- Vista de todas las reservas del sistema
- Estadísticas de reservas (total, ingresos, cancelaciones)
- Filtrado por rango de fechas
- Gestión completa de recursos

---

## Tecnologías Empleadas

### Backend
| Tecnología | Descripción |
|-------------|-------------|
| **Spring Boot 3.5.11** | Framework principal de la aplicación |
| **Java 21** | Lenguaje de programación |
| **Maven** | Herramienta de gestión de dependencias y build |

### Dependencias Principales
| Dependencia | Propósito |
|-------------|-----------|
| Spring Boot Starter Web | Creación de API REST |
| Spring Boot Starter Security | Seguridad y autenticación |
| Spring Data JPA | Acceso a datos y ORM |
| Spring Boot Starter Validation | Validación de datos |
| Springdoc OpenAPI | Documentación Swagger |
| Lombok | Reducción de código repetitivo |
| JJWT | Generación y validación de tokens JWT |
| PostgreSQL Driver | Conexión a base de datos |
| H2 Database | Base de datos en memoria para tests |
| Spring Docker Compose | Orquestación de contenedores |

### Infraestructura
- **Base de Datos**: PostgreSQL 15
- **Contenedores**: Docker y Docker Compose

---

## Arquitectura del Sistema

El proyecto sigue el patrón **Service-Repository** con una clara separación de responsabilidades:

```
┌─────────────────────────────────────────────────────────────┐
│                      PRESENTATION LAYER                      │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │ AuthController│  │MovieController│  │ReservationController│  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                       BUSINESS LAYER                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │ UserService  │  │ MovieService │  │ReservationService │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                        DATA LAYER                             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │MovieRepository│  │UserRepository │  │ReservationRepository│  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      PERSISTENCE LAYER                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │    Movie    │  │    User    │  │   Reservation       │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Características de la Arquitectura

- **Separación de responsabilidades**: Cada capa tiene una función específica y bien definida
- **Inyección de dependencias**: Uso de constructor injection para las dependencias
- **Transacciones**: Gestión de transacciones a nivel de servicio con `@Transactional`
- **Excepciones centralizadas**: GlobalExceptionHandler para manejo uniforme de errores
- **HATEOAS**: Links en las respuestas para navegación REST
- **Seguridad**: Filtros JWT y control de acceso basado en roles (RBAC)

---

## Modelos de Datos

### User
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long | Identificador único |
| username | String | Nombre de usuario (único, alfanumérico) |
| email | String | Correo electrónico (único) |
| password | String | Contraseña encriptada (BCrypt) |
| role | Role | Rol del usuario (USER, ADMIN) |

### Movie
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long | Identificador único |
| title | String | Título de la película |
| description | String | Descripción de la trama |
| posterUrl | String | URL del póster de la película |
| genre | Genre | Género de la película |

### Theater
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long | Identificador único |
| name | String | Nombre de la sala |
| location | String | Ubicación de la sala |

### Showtime
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long | Identificador único |
| movie | Movie | Película de la función |
| theater | Theater | Sala de la función |
| startTime | LocalDateTime | Hora de inicio |
| endTime | LocalDateTime | Hora de fin |

### Seat
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long | Identificador único |
| seatNumber | int | Número de asiento |
| row | int | Fila del asiento |
| theater | Theater | Sala a la que pertenece |

### Reservation
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long | Identificador único |
| user | User | Usuario que realiza la reserva |
| reservationDate | LocalDateTime | Fecha de la reserva |
| totalAmount | BigDecimal | Monto total de la reserva |
| status | ReservationStatus | Estado de la reserva |
| reservedSeats | List<ShowtimeSeat> | Asientos reservados |

### Enum: Genre
- ACTION, COMEDY, DRAMA, HORROR, ROMANCE, SCIENTIFIC_FANTASY

### Enum: ReservationStatus
- PENDING, CONFIRMED, CANCELLED, COMPLETED

### Enum: Role
- USER, ADMIN

---

## Endpoints Principales

### Autenticación

| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|--------|
| POST | `/api/auth/register` | Registrar nuevo usuario | Público |
| POST | `/api/auth/login` | Iniciar sesión | Público |

### Películas

| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|--------|
| GET | `/api/movies` | Listar películas (paginado) | USER/ADMIN |
| GET | `/api/movies/{id}` | Obtener película por ID | USER/ADMIN |
| POST | `/api/movies` | Crear película | ADMIN |
| PUT | `/api/movies/{id}` | Actualizar película | ADMIN |
| DELETE | `/api/movies/{id}` | Eliminar película | ADMIN |

### Salas

| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|--------|
| GET | `/api/theaters` | Listar todas las salas | USER/ADMIN |
| GET | `/api/theaters/{id}` | Obtener sala por ID | USER/ADMIN |
| POST | `/api/theaters` | Crear nueva sala | ADMIN |
| PUT | `/api/theaters/{id}` | Actualizar sala | ADMIN |
| DELETE | `/api/theaters/{id}` | Eliminar sala | ADMIN |

### Funciones

| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|--------|
| GET | `/api/showtimes` | Listar funciones (paginado) | USER/ADMIN |
| GET | `/api/showtimes/{id}` | Obtener función por ID | USER/ADMIN |
| POST | `/api/showtimes` | Crear nueva función | ADMIN |
| PUT | `/api/showtimes/{id}` | Actualizar función | ADMIN |
| DELETE | `/api/showtimes/{id}` | Eliminar función | ADMIN |

### Reservas

| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|--------|
| POST | `/api/reservations` | Crear reserva | USER |
| GET | `/api/reservations/me` | Mis reservas | USER |
| GET | `/api/reservations/{id}` | Obtener reserva por ID | USER/ADMIN |
| DELETE | `/api/reservations/{id}` | Cancelar reserva | USER/ADMIN |

### Administración

| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|--------|
| GET | `/api/admin/reservations` | Todas las reservas | ADMIN |
| GET | `/api/admin/statistics` | Estadísticas del sistema | ADMIN |

### Documentación

La documentación interactiva de la API está disponible en:
```
http://localhost:8080/swagger-ui.html
```

---

## Motivación

Este proyecto nació como una iniciativa de aprendizaje para profundizar en el desarrollo de APIs RESTful con Spring Boot y construir un sistema real de reservas.

### Objetivos Alcanzados

1. **Dominio de Spring Boot 3**: Implementar una aplicación completa desde cero, manejando seguridad, persistencia y arquitectura REST con las últimas versiones del framework.

2. **Sistema de Reservas Complejo**: Construir un sistema real donde los usuarios pueden reservar asientos, con validación de disponibilidad y cálculo de precios.

3. **Seguridad Avanzada**: Implementar autenticación JWT con filtros personalizados, control de acceso basado en roles (RBAC) y manejo centralizado de excepciones.

4. **Buenas Prácticas de Desarrollo**: Aplicar principios de diseño como inyección de dependencias, separación de responsabilidades, validaciones, paginación y documentación con Swagger.

5. **Features de APIs Modernas**: Incorporar HATEOAS para navegación REST, paginación, filtrado y búsqueda.

Este proyecto sirve como base sólida para construir aplicaciones más complejas de comercio electrónico y demuestra la capacidad de integrar múltiples tecnologías en una solución funcional.

---

## Manual de Instalación y Deployment

### Prerrequisitos

- Java 21 o superior
- Maven 3.8 o superior
- Docker y Docker Compose

### Pasos de Instalación

#### 1. Clonar el repositorio

```bash
git clone <repositorio>
cd movie-service
```

#### 2. Levantar la base de datos con Docker Compose

```bash
docker-compose up -d
```

Esto iniciarán:
- PostgreSQL en el puerto **5432**

#### 3. Compilar el proyecto

```bash
./mvnw clean package
```

O en Windows:

```cmd
mvnw.cmd clean package
```

#### 4. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

### Configuración de Variables

El archivo `application.properties` contiene la configuración principal:

```properties
# Nombre de la aplicación
spring.application.name=movie-service

# Configuración JWT
jwt.secret=Y29tLm1vdmllLnJlc2VydmF0aW9uLXNlY3JldC1rZXktZm9yLWp3dC10b2tlbi1nZW5lcmF0aW9u
jwt.expiration=86400000

# Conexión a PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/movie-service
spring.datasource.username=postgres
spring.datasource.password=Losprimos123

# Hibernate
spring.jpa.hibernate.ddl-auto=update
```

### Verificación

1. Accede a Swagger UI: `http://localhost:8080/swagger-ui.html`
2. Registra un nuevo usuario usando `/api/auth/register`
3. Inicia sesión con `/api/auth/login`
4. Usa el token JWT para autenticarte en los endpoints protegidos

---

## Ejemplos de Uso

### Registro de Usuario

**Request:**
```bash
POST /api/auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "john@example.com",
  "role": "ROLE_USER",
  "expiresIn": 86400
}
```

### Inicio de Sesión

**Request:**
```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

### Crear una Película (Admin)

**Request:**
```bash
POST /api/movies
Authorization: Bearer <token_admin>
Content-Type: application/json

{
  "title": "Inception",
  "description": "A thief who steals corporate secrets...",
  "posterUrl": "https://example.com/inception.jpg",
  "genre": "SCIENCE_FICTION"
}
```

### Listar Películas con Filtros

**Request:**
```bash
GET /api/movies?genre=ACTION&searchTerm=avatar&page=0&size=10
```

### Crear una Reserva

**Request:**
```bash
POST /api/reservations
Authorization: Bearer <token_user>
Content-Type: application/json

{
  "showtimeId": 1,
  "seatIds": [1, 2, 3]
}
```

**Response:**
```json
{
  "id": 1,
  "userId": 1,
  "showtimeId": 1,
  "reservationDate": "2026-05-02T10:30:00",
  "totalAmount": 45.00,
  "status": "CONFIRMED",
  "reservedSeats": [...]
}
```

### Obtener Estadísticas (Admin)

**Request:**
```bash
GET /api/admin/statistics?start=2026-01-01T00:00:00&end=2026-12-31T23:59:59
Authorization: Bearer <token_admin>
```

**Response:**
```json
{
  "totalReservations": 150,
  "confirmedReservations": 120,
  "cancelledReservations": 30,
  "totalRevenue": 4500.00,
  "averagePerReservation": 30.00
}
```

---

## Roadmap

### ✅ Funcionalidades Completadas
- Autenticación JWT (registro/login)
- Sistema de roles (USER, ADMIN)
- CRUD completo de películas
- CRUD completo de salas (theaters)
- CRUD completo de funciones (showtimes)
- Sistema de reservas con validación de asientos
- Cálculo automático de precio total
- Cancelación de reservas
- Historial de reservas por usuario
- Panel de administración con estadísticas
- Paginación de resultados
- Filtrado y búsqueda
- HATEOAS en respuestas
- Documentación con Swagger
- Manejo centralizado de excepciones
- Tests unitarios

### 📋 Planeadas para Futuras Versiones

| Versión | Funcionalidad | Descripción |
|---------|---------------|-------------|
| v1.1 | Notificaciones push | Envío de notificaciones cuando la reserva se confirma |
| v1.2 | Integración de pagos | Integración con pasarelas de pago (Stripe, PayPal) |
| v1.3 | Envío de emails | Confirmación de reserva por email |
| v1.4 | Asientos premium | Sistema de asientos VIP con beneficios adicionales |
| v1.5 | Funciones especiales | Soporte para funciones 3D, IMAX con precios diferenciados |
| v2.0 | API pública | API pública para integración con aplicaciones de terceros |
| v2.1 | Reportes avanzados | Generación de reportes en PDF y Excel |
| v2.2 | Dashboard interactivo | Panel de control con gráficos en tiempo real |

---

## Contribución

¿Encontraste un bug o tienes sugerencias? Por favor, crea un issue en el repositorio.

---

<p align="center">
  <strong>© 2026 Movie Service</strong>
</p>