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
