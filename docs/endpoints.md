# Registro de Endpoints - Smallbox

Este documento registra todos los endpoints expuestos por la API, organizados por módulos.

## Módulo: Autenticación (`/auth`)

| Método | Endpoint | Descripción | Acceso |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/login` | Autentica a un usuario y retorna un Access Token (JWT) y Refresh Token (Cookie). | Público |
| `POST` | `/api/v1/auth/refresh` | Rota los tokens usando el Refresh Token de la cookie. | Público (requiere cookie) |
| `POST` | `/api/v1/auth/logout` | Revoca la sesión actual. | Autenticado |

## Módulo: Usuarios (`/users`)

| Método | Endpoint | Descripción | Acceso |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/users` | Lista todos los usuarios. | Admin / SuperAdmin |
| `GET` | `/api/v1/users/{id}` | Obtiene detalles de un usuario por ID. | Autenticado |
| `POST` | `/api/v1/users` | Crea un nuevo usuario (Empleado/Admin/Cliente). | Admin / SuperAdmin |
| `DELETE` | `/api/v1/users/{id}` | Desactiva/Elimina un usuario. | Admin / SuperAdmin |
| `PUT` | `/api/v1/users/profile` | Actualiza el perfil del usuario logueado. | Autenticado |

## Módulo: Sucursales (`/branches`)

| Método | Endpoint | Descripción | Acceso |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/branches` | Lista todas las sucursales activas. | Autenticado |
| `GET` | `/api/v1/branches/{id}` | Obtiene detalles de una sucursal por ID. | Autenticado |
| `GET` | `/api/v1/branches/location/{locId}` | Obtiene la sucursal asignada a una ubicación. | Autenticado |
| `POST` | `/api/v1/branches` | Crea una nueva sucursal. | SuperAdmin |
| `PATCH` | `/api/v1/branches/{id}` | Actualiza datos parciales de una sucursal. | SuperAdmin / Admin |
| `DELETE` | `/api/v1/branches/{id}` | Elimina (soft-delete) una sucursal. | SuperAdmin |
| `GET` | `/api/v1/branches/{id}/users` | Lista usuarios asignados a una sucursal. | Admin / BranchAdmin |
| `POST` | `/api/v1/branches/{id}/users` | Asigna un empleado/admin a una sucursal. | Admin / SuperAdmin |
| `PATCH` | `/api/v1/branches/{id}/users/{uId}/status` | Activa/Desactiva acceso de un usuario a la sucursal. | Admin / BranchAdmin |

## Módulo: Envíos (`/shipments`)

| Método | Endpoint | Descripción | Acceso |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/shipments` | Crea un nuevo envío con sus paquetes y destinatario. | Cliente / Empleado |
| `GET` | `/api/v1/shipments` | Lista los envíos (filtrado por rol). | Autenticado |
| `GET` | `/api/v1/shipments/{id}` | Obtiene detalles de un envío por UUID. | Autenticado |
| `GET` | `/api/v1/shipments/tracking/{number}` | Consulta un envío por su número de guía. | Público / Autenticado |
| `PATCH` | `/api/v1/shipments/{id}/status` | Actualiza el estado de un envío. | Empleado / Admin |
| `GET` | `/api/v1/shipments/my-shipments` | Lista los envíos realizados por el usuario actual. | Cliente |
