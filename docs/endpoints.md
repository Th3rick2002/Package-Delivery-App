# Registro de Endpoints - Smallbox

Este documento registra todos los endpoints expuestos por la API, organizados por módulos. Todos los endpoints (excepto Auth y Cities) requieren autenticación vía JWT (Cookie `accessToken`).

## Módulo: Autenticación (`/api/v1/auth`)

| Método | Endpoint   | Descripción                                                                | Acceso      |
|:-------|:-----------|:---------------------------------------------------------------------------|:------------|
| `POST` | `/login`   | Autentica a un usuario y establece cookies `accessToken` y `refreshToken`. | Público     |
| `POST` | `/refresh` | Rota los tokens usando el `refreshToken` de la cookie.                     | Público     |
| `POST` | `/logout`  | Revoca la sesión actual y limpia las cookies.                              | Autenticado |

## Módulo: Usuarios (`/api/v1/users`)

| Método   | Endpoint | Descripción                            | Acceso             |
|:---------|:---------|:---------------------------------------|:-------------------|
| `GET`    | `/`      | Lista todos los usuarios registrados.  | Admin / SuperAdmin |
| `GET`    | `/{id}`  | Obtiene detalles de un usuario por ID. | Autenticado        |
| `POST`   | `/`      | Crea un nuevo usuario.                 | Admin / SuperAdmin |
| `DELETE` | `/{id}`  | Elimina físicamente un usuario.        | Admin / SuperAdmin |

## Módulo: Sucursales (`/api/v1/branches`)

| Método   | Endpoint            | Descripción                                                  | Acceso             |
|:---------|:--------------------|:-------------------------------------------------------------|:-------------------|
| `GET`    | `/`                 | Lista todas las sucursales.                                  | Autenticado        |
| `GET`    | `/{id}`             | Obtiene detalles de una sucursal por ID.                     | Autenticado        |
| `GET`    | `/location/{locId}` | Obtiene la sucursal asignada a una ubicación (Departamento). | Autenticado        |
| `POST`   | `/`                 | Crea una nueva sucursal.                                     | SuperAdmin         |
| `PATCH`  | `/{id}`             | Actualiza datos parciales de una sucursal.                   | Admin / SuperAdmin |
| `DELETE` | `/{id}`             | Elimina una sucursal.                                        | SuperAdmin         |

### Gestión de Usuarios por Sucursal (`/api/v1/branches/{branchId}/users`)

| Método  | Endpoint           | Descripción                                              | Acceso              |
|:--------|:-------------------|:---------------------------------------------------------|:--------------------|
| `GET`   | `/`                | Lista usuarios asignados a la sucursal.                  | Admin / BranchAdmin |
| `POST`  | `/`                | Asigna un usuario a la sucursal.                         | Admin / SuperAdmin  |
| `PATCH` | `/{userId}/status` | Activa/Desactiva el estado de un usuario en la sucursal. | Admin / BranchAdmin |

## Módulo: Envíos (`/api/v1/shipments`)

| Método  | Endpoint                    | Descripción                                             | Acceso             |
|:--------|:----------------------------|:--------------------------------------------------------|:-------------------|
| `POST`  | `/`                         | Crea un nuevo envío con sus paquetes y destinatario.    | Cliente / Empleado |
| `GET`   | `/{trackingNumber}`         | Obtiene los detalles de un envío por su número de guía. | Autenticado        |
| `PATCH` | `/{trackingNumber}/status`  | Actualiza el estado de un envío.                        | Empleado / Admin   |
| `GET`   | `/{trackingNumber}/history` | Obtiene el historial de estados de un envío.            | Autenticado        |

## Módulo: Shared / Ubicaciones (`/v1/cities`)

| Método | Endpoint | Descripción                            | Acceso  |
|:-------|:---------|:---------------------------------------|:--------|
| `GET`  | `/`      | Lista todas las ciudades y sus países. | Público |

---

## Documentación Interactiva
La documentación interactiva está disponible en:
- **Scalar**: `/scalar-ui.html`
- **Swagger UI**: `/swagger-ui/index.html`
- **OpenAPI Spec**: `/v3/api-docs`
