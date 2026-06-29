# Smallbox - Sistema de Gestión de Envíos (Package Delivery)

**Smallbox** es un sistema web diseñado para gestionar de manera eficiente y segura los envíos de paquetes entre diferentes sucursales. El sistema está construido con una arquitectura hexagonal pragmática utilizando Spring Boot.

---

## 🚀 Acciones que realiza el sistema

El sistema está organizado por módulos funcionales que realizan las siguientes operaciones:

### 1. Autenticación y Control de Sesión (`auth`)
* **Registro de Clientes:** Permite la creación de nuevas cuentas para clientes que utilizarán la plataforma.
* **Inicio de Sesión (Login):** Autenticación diferenciada tanto para clientes (`/login`) como para personal administrativo (`/private/login`).
* **Seguridad por JWT:** Establecimiento de tokens seguros a través de cookies (`accessToken` y `refreshToken`).
* **Rotación y Cierre de Sesión:** Mecanismos para refrescar tokens de acceso de manera segura (`/refresh`) y revocar sesiones (`/logout`).

### 2. Gestión de Usuarios (`users`)
* **Administración de Cuentas:** Registro, consulta, detalle y eliminación de usuarios del sistema.
* **Control de Perfiles:** Consulta del perfil actual del usuario autenticado (`/me`).
* **Control de Acceso basado en Roles:** Diferenciación de permisos para `SUPER_ADMIN`, `BRANCH_ADMIN`, `EMPLOYEE` y `CLIENT`.

### 3. Gestión de Sucursales (`branches`)
* **Mantenimiento de Sucursales:** Creación, actualización parcial (PATCH), consulta detallada y eliminación de sucursales físicas.
* **Asociación Geográfica:** Búsqueda y asignación de sucursales según su ubicación (Departamentos).

### 4. Personal de Sucursales (`branch-users`)
* **Asignación de Personal:** Vinculación de usuarios administrativos y empleados a sucursales específicas.
* **Control de Estado Laboral:** Activación y desactivación del estado de un usuario dentro de su respectiva sucursal.

### 5. Registro y Rastreo de Envíos (`shipments`)
* **Creación de Envíos:** Registro de nuevos envíos especificando el destinatario, sucursal de origen/destino y los paquetes asociados (con descripción, peso en KG y dimensiones en CM).
* **Búsqueda y Seguimiento:** Consulta de envíos mediante número de guía único (Tracking Number).
* **Control de Estado de Envíos:** Transición guiada y actualización del estado del paquete.
* **Historial de Trayecto:** Consulta histórica de todos los cambios de estado por los que ha pasado un envío.

### 6. Catálogo de Ubicaciones (`cities`)
* **Consulta Geográfica:** Listado de ciudades y países soportados por la red de distribución.

---

## 📖 Documentación del Sistema

Para resolver dudas específicas sobre la implementación, arquitectura o API, consulte los siguientes documentos de soporte dentro de la carpeta [docs/](docs):

* **[Arquitectura del Proyecto](docs/ARCHITECTURE.md):** Detalla la arquitectura hexagonal simplificada, estructura de las capas (`domain`, `application`, `infrastructure`) y las reglas de dependencias del proyecto.
* **[Registro de Endpoints de la API](docs/endpoints.md):** Catálogo completo de las rutas de la API REST, con los métodos HTTP correspondientes, descripción detallada y roles de acceso requeridos.
* **[Catálogo y Manejo de Errores](docs/ERRORS.md):** Guía sobre el formato estándar de error del sistema (`ApiErrorResponse`) y la tabla detallada con los códigos de error por módulo.

### 🔌 API Interactiva
Al iniciar la aplicación localmente, puede acceder a las herramientas de prueba e interacción rápida:
* **Scalar UI (Recomendado):** `/scalar-ui.html`
* **Swagger UI:** `/swagger-ui/index.html`
* **Especificación OpenAPI (JSON):** `/v3/api-docs`
