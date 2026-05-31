# Arquitectura Hexagonal Pragmática - Smallbox

Este proyecto utiliza una versión simplificada y pragmática de la Arquitectura Hexagonal para facilitar el desarrollo sin perder la separación de responsabilidades.

## Estructura de Módulos

Cada módulo (ej. `user`, `shipment`, `shared`) se organiza de la siguiente manera:

### 1. Dominio (`domain/`)
Es el corazón del negocio. **No depende de ningún framework (incluyendo Spring)**.
- **Entidades:** Clases Java puras que representan conceptos de negocio (ej. `User`, `Shipment`).
- **Value Objects:** Objetos inmutables que validan datos (ej. `Email`, `Price`). Se ubican en `domain/vo/` o directamente en `domain/`.
- **Excepciones:** Excepciones específicas de negocio (ej. `InsufficientStockException`).
- **Puertos de Salida (Repositories):** Interfaces que definen cómo el dominio necesita persistir o recuperar datos. Se ubican en la raíz de `domain/` (ej. `UserRepository.java`).

### 2. Aplicación (`application/`)
Coordina las acciones del negocio.
- **Servicios/Casos de Uso:** Clases concretas (ej. `UserService.java`) que implementan la lógica de coordinación. Llaman a los repositorios (puertos de salida) y orquestan las entidades de dominio.
- **DTOs:** Objetos para transferir datos entre la infraestructura y la aplicación.

### 3. Infraestructura (`infrastructure/`)
Implementación técnica y detalles de frameworks.
- **Persistence:** Entidades de JPA (`@Entity`), repositorios de Spring Data y **Adaptadores** que implementan las interfaces del dominio (puertos de salida).
- **Web:** Controladores REST (Adaptadores de Entrada) que reciben peticiones y llaman a los servicios de aplicación.
- **Security/Config:** Configuraciones técnicas específicas de Spring u otras librerías.

## Documentación de la API

El proyecto utiliza **OpenAPI 3** para la documentación de los endpoints.

- **Annotations:** Se utilizan anotaciones de `io.swagger.v3.oas.annotations` en los controladores (`@Tag`, `@Operation`, `@ApiResponse`) y DTOs (`@Schema`) para enriquecer la especificación.
- **Scalar:** Se ha integrado **Scalar** como interfaz principal para explorar la API, ofreciendo una experiencia más moderna y funcional que Swagger UI.
- **Acceso:**
    - Scalar: `/scalar-ui.html`
    - Swagger UI: `/swagger-ui/index.html`
    - JSON Spec: `/v3/api-docs`

## Reglas de Dependencia
1. **Dominio** no conoce a nadie.
2. **Aplicación** conoce al Dominio.
3. **Infraestructura** conoce a Aplicación y Dominio.