# Catálogo de Errores - Smallbox

Este documento define el estándar para el manejo de errores en toda la aplicación, asegurando que tanto el frontend como otros microservicios reciban respuestas consistentes y semánticas.

## Formato de Respuesta (`ApiErrorResponse`)

Todas las excepciones de negocio y errores del framework son transformados al siguiente formato JSON:

```json
{
  "code": "BRANCH_NOT_FOUND",
  "message": "Branch with id 10 not found",
  "status": 404,
  "timestamp": "2026-05-25T14:30:00.000Z"
}
```

### Campos:
- **`code`**: Cadena de texto única (String) que identifica el error específico. Ideal para lógica de internacionalización (i18n) en el frontend.
- **`message`**: Descripción detallada del error (principalmente para desarrolladores o logs).
- **`status`**: Código de estado HTTP numérico.
- **`timestamp`**: Fecha y hora del error en formato ISO-8601.

---

## Errores Base (Shared)

Estos errores están definidos en el núcleo de la aplicación (`shared/domain/exception`) y representan fallos genéricos de protocolo o estado.

| Código (`code`)         | HTTP Status | Descripción                                                                 |
|:------------------------|:------------|:----------------------------------------------------------------------------|
| `BAD_REQUEST`           | 400         | La petición es inválida o mal formada.                                      |
| `VALIDATION_ERROR`      | 400         | Fallo en las validaciones de entrada (ej: `@Valid` en DTOs).                |
| `UNAUTHORIZED`          | 401         | El usuario no está autenticado o las credenciales son inválidas.            |
| `FORBIDDEN`             | 403         | El usuario está autenticado pero no tiene permisos para realizar la acción. |
| `NOT_FOUND`             | 404         | El recurso solicitado no existe.                                            |
| `METHOD_NOT_ALLOWED`    | 405         | El método HTTP no está permitido para este endpoint.                        |
| `CONFLICT`              | 409         | Conflicto con el estado actual del servidor (ej: registro duplicado).       |
| `UNPROCESSABLE_ENTITY`  | 422         | Error de lógica de negocio (reglas que impiden procesar la solicitud).      |
| `TOO_MANY_REQUESTS`     | 429         | El cliente ha superado el límite de peticiones permitido.                   |
| `INTERNAL_SERVER_ERROR` | 500         | Error inesperado en el servidor.                                            |
| `SERVICE_UNAVAILABLE`   | 503         | Una dependencia crítica (DB, API externa) no está disponible.               |

---

## Errores de Dominio (Módulos Específicos)

Cada módulo debe extender las excepciones base para proporcionar contexto de negocio. La convención para el `code` es `MODULO_MOTIVO`.

### Módulo: Branch
| Código (`code`)              | Base Exception                 | Descripción                                                      |
|:-----------------------------|:-------------------------------|:-----------------------------------------------------------------|
| `BRANCH_NOT_FOUND`           | `NotFoundException`            | La sucursal solicitada no existe en el sistema.                  |
| `BRANCH_ALREADY_EXISTS`      | `ConflictException`            | Ya existe una sucursal con el mismo nombre o código.             |
| `LOCATION_NOT_FOUND`         | `NotFoundException`            | La ubicación (departamento) especificada no existe.              |
| `BRANCH_INACTIVE`            | `UnprocessableEntityException` | La sucursal está inactiva o eliminada y no permite asignaciones. |
| `BRANCH_USER_NOT_FOUND`      | `NotFoundException`            | El usuario no está asignado a la sucursal especificada.          |
| `BRANCH_USER_ALREADY_EXISTS` | `ConflictException`            | El usuario ya se encuentra asignado a la sucursal.               |
| `INVALID_ROLE_FOR_BRANCH`    | `UnprocessableEntityException` | El rol del usuario no es apto para ser asignado a una sucursal.  |

### Módulo: Auth / User
| Código (`code`)        | Base Exception          | Descripción                                                   |
|:-----------------------|:------------------------|:--------------------------------------------------------------|
| `USER_NOT_FOUND`       | `NotFoundException`     | El usuario solicitado no existe.                              |
| `ROLE_NOT_FOUND`       | `NotFoundException`     | El rol solicitado no existe.                                  |
| `EMAIL_ALREADY_IN_USE` | `ConflictException`     | El correo electrónico ya está registrado.                     |
| `INVALID_CREDENTIALS`  | `UnauthorizedException` | Usuario o contraseña incorrectos.                             |
| `INVALID_TOKEN`        | `UnauthorizedException` | El token JWT es inválido.                                     |
| `EXPIRED_TOKEN`        | `UnauthorizedException` | El token JWT ha expirado.                                     |
| `SESSION_NOT_FOUND`    | `NotFoundException`     | La sesión de refresco no existe.                              |
| `TOKEN_REVOKED`        | `UnauthorizedException` | El token ha sido revocado por seguridad (detección de reúso). |

### Módulo: Shipment
| Código (`code`)                | Base Exception                 | Descripción                                                              |
|:-------------------------------|:-------------------------------|:-------------------------------------------------------------------------|
| `SHIPMENT_NOT_FOUND`           | `NotFoundException`            | El envío solicitado no existe.                                           |
| `INVALID_SHIPMENT_STATUS`      | `UnprocessableEntityException` | La operación no es permitida para el estado actual del envío.            |
| `TRACKING_NUMBER_NOT_FOUND`    | `NotFoundException`            | No se encontró ningún envío con el número de guía proporcionado.         |
| `PACKAGE_NOT_FOUND`            | `NotFoundException`            | El paquete especificado no existe en el envío.                           |
| `INVALID_PACKAGE_DIMENSIONS`   | `BadRequestException`          | Las dimensiones o peso del paquete no cumplen con los requisitos.        |
| `PACKAGE_DESCRIPTION_REQUIRED` | `BadRequestException`          | La descripción del paquete es obligatoria.                               |
| `PACKAGE_REQUIRED`             | `BadRequestException`          | El envío debe contener al menos un paquete.                              |
| `WEIGHT_REQUIRED`              | `BadRequestException`          | El peso del paquete es obligatorio.                                      |
| `INVALID_WEIGHT`               | `BadRequestException`          | El peso debe ser mayor a cero.                                           |
| `UNSUPPORTED_WEIGHT_UNIT`      | `BadRequestException`          | Unidad de peso no soportada (Solo KG).                                   |
| `DIMENSIONS_REQUIRED`          | `BadRequestException`          | Las dimensiones del paquete son obligatorias.                            |
| `INVALID_DIMENSION_LENGTH`     | `BadRequestException`          | El largo debe ser mayor a cero.                                          |
| `INVALID_DIMENSION_WIDTH`      | `BadRequestException`          | El ancho debe ser mayor a cero.                                          |
| `INVALID_DIMENSION_HEIGHT`     | `BadRequestException`          | El alto debe ser mayor a cero.                                           |
| `UNSUPPORTED_DIMENSION_UNIT`   | `BadRequestException`          | Unidad de dimensión no soportada (Solo CM).                              |
| `INVALID_TRACKING_NUMBER`      | `BadRequestException`          | El formato del número de guía es inválido.                               |
| `INVALID_TRACKING_PREFIX`      | `BadRequestException`          | El prefijo del número de guía es inválido (2-4 letras).                  |
| `CURRENCY_REQUIRED`            | `BadRequestException`          | La moneda es obligatoria.                                                |
| `INVALID_PRICE_AMOUNT`         | `BadRequestException`          | El monto del precio debe ser mayor a cero.                               |
| `UNSUPPORTED_CURRENCY`         | `BadRequestException`          | Moneda no soportada (Solo USD).                                          |
| `CURRENCY_MISMATCH`            | `BadRequestException`          | No se pueden realizar operaciones con diferentes monedas.                |
| `RECIPIENT_NAME_REQUIRED`      | `BadRequestException`          | El nombre del destinatario es obligatorio.                               |
| `INVALID_RECIPIENT_NAME`       | `BadRequestException`          | El nombre del destinatario contiene caracteres inválidos o es muy corto. |
| `RECIPIENT_PHONE_REQUIRED`     | `BadRequestException`          | El teléfono del destinatario es obligatorio.                             |
| `RECIPIENT_EMAIL_REQUIRED`     | `BadRequestException`          | El correo del destinatario es obligatorio.                               |
| `UNKNOWN_SHIPMENT_STATUS`      | `BadRequestException`          | El ID del estado de envío no es reconocido.                              |

---

## Guía de Implementación para Desarrollo

Para crear un nuevo error de negocio:

1. **Identifique la base**: Elija una clase abstracta de `shared.domain.exception` (ej: `NotFoundException`).
2. **Cree la clase**:
   ```java
   public class UserNotFoundException extends NotFoundException {
       public UserNotFoundException(String email) {
           super("USER_NOT_FOUND", "User with email " + email + " not found");
       }
   }
   ```
3. **Láncela en el dominio o aplicación**:
   ```java
   userRepository.findByEmail(email)
       .orElseThrow(() -> new UserNotFoundException(email));
   ```
