# Registro de Endpoints - Smallbox

Este documento registra todos los endpoints expuestos por la API, organizados por módulos. La mayoría de los endpoints requieren autenticación vía JWT (Cookie `accessToken`).

## Módulo: Autenticación (`/api/v1/auth`)

| Método | Endpoint    | Descripción                                                                | Acceso      |
|:-------|:------------|:---------------------------------------------------------------------------|:------------|
| `POST` | `/login`    | Autentica a un usuario y establece cookies `accessToken` y `refreshToken`. | Público     |
| `POST` | `/register` | Registra un nuevo cliente en el sistema.                                   | Público     |
| `POST` | `/refresh`  | Rota los tokens usando el `refreshToken` de la cookie.                     | Público     |
| `POST` | `/logout`   | Revoca la sesión actual y limpia las cookies.                              | Autenticado |

## Módulo: Usuarios (`/api/v1/users`)

| Método   | Endpoint | Descripción                            | Acceso                              |
|:---------|:---------|:---------------------------------------|:------------------------------------|
| `GET`    | `/`      | Lista todos los usuarios registrados.  | SUPER_ADMIN, BRANCH_ADMIN, EMPLOYEE |
| `GET`    | `/{id}`  | Obtiene detalles de un usuario por ID. | SUPER_ADMIN, BRANCH_ADMIN, EMPLOYEE |
| `POST`   | `/`      | Crea un nuevo usuario.                 | SUPER_ADMIN, BRANCH_ADMIN           |
| `DELETE` | `/{id}`  | Elimina físicamente un usuario.        | SUPER_ADMIN, BRANCH_ADMIN           |

## Módulo: Sucursales (`/api/v1/branches`)

| Método   | Endpoint            | Descripción                                                  | Acceso      |
|:---------|:--------------------|:-------------------------------------------------------------|:------------|
| `GET`    | `/`                 | Lista todas las sucursales.                                  | Autenticado |
| `GET`    | `/{id}`             | Obtiene detalles de una sucursal por ID.                     | Autenticado |
| `GET`    | `/location/{locId}` | Obtiene la sucursal asignada a una ubicación (Departamento). | Autenticado |
| `POST`   | `/`                 | Crea una nueva sucursal.                                     | SUPER_ADMIN |
| `PATCH`  | `/{id}`             | Actualiza datos parciales de una sucursal.                   | SUPER_ADMIN |
| `DELETE` | `/{id}`             | Elimina una sucursal.                                        | SUPER_ADMIN |

### Gestión de Usuarios por Sucursal (`/api/v1/branches/{branchId}/users`)

| Método  | Endpoint           | Descripción                                              | Acceso                    |
|:--------|:-------------------|:---------------------------------------------------------|:--------------------------|
| `GET`   | `/`                | Lista usuarios asignados a la sucursal.                  | SUPER_ADMIN, BRANCH_ADMIN |
| `POST`  | `/`                | Asigna un usuario a la sucursal.                         | SUPER_ADMIN, BRANCH_ADMIN |
| `PATCH` | `/{userId}/status` | Activa/Desactiva el estado de un usuario en la sucursal. | SUPER_ADMIN, BRANCH_ADMIN |

## Módulo: Envíos (`/api/v1/shipments`)

| Método  | Endpoint                    | Descripción                                             | Acceso                          |
|:--------|:----------------------------|:--------------------------------------------------------|:--------------------------------|
| `POST`  | `/`                         | Crea un nuevo envío con sus paquetes y destinatario.    | BRANCH_ADMIN, EMPLOYEE, CLIENT  |
| `GET`   | `/{trackingNumber}`         | Obtiene los detalles de un envío por su número de guía. | BRANCH_ADMIN, EMPLOYEE, CLIENT  |
| `PATCH` | `/{trackingNumber}/status`  | Actualiza el estado de un envío.                        | BRANCH_ADMIN, EMPLOYEE, CLIENT  |
| `GET`   | `/{trackingNumber}/history` | Obtiene el historial de estados de un envío.            | BRANCH_ADMIN, EMPLOYEE, CLIENT  |

## Módulo: Shared / Ubicaciones (`/v1/cities`)

| Método | Endpoint | Descripción                            | Acceso                                      |
|:-------|:---------|:---------------------------------------|:--------------------------------------------|
| `GET`  | `/`      | Lista todas las ciudades y sus países. | SUPER_ADMIN, BRANCH_ADMIN, EMPLOYEE, CLIENT |

---

## Documentación Interactiva
La documentación interactiva está disponible en:
- **Scalar**: `/scalar-ui.html`
- **Swagger UI**: `/swagger-ui/index.html`
- **OpenAPI Spec**: `/v3/api-docs`
