-- Seed de Roles
INSERT INTO roles (role_name) VALUES 
('SUPER_ADMIN'),
('BRANCH_ADMIN'),
('EMPLOYEE'),
('CLIENT');

-- Seed de Status de Envío
INSERT INTO shipment_status (status_id, status_name, description) VALUES
(1, 'CREATED', 'El pedido ha sido creado'),
(2, 'RECEIVED_ORIGIN', 'Recibido en sucursal de origen'),
(3, 'IN_TRANSIT', 'En tránsito entre sucursales'),
(4, 'RECEIVED_DESTINATION', 'Recibido en sucursal de destino'),
(5, 'DELIVERED', 'Entregado al destinatario'),
(6, 'CANCELLED', 'Pedido cancelado');

-- Seed de Tipos de Paquete
INSERT INTO package_type (package_type_id, type_name, description) VALUES
(1, 'DOCUMENT', 'Documentos, sobres, etc.'),
(2, 'SMALL_BOX', 'Caja pequeña (hasta 5kg)'),
(3, 'MEDIUM_BOX', 'Caja mediana (hasta 15kg)'),
(4, 'LARGE_BOX', 'Caja grande (más de 15kg)'),
(5, 'FRAGILE', 'Cuidado especial');

-- Seed de Paises iniciales (Ejemplo)
INSERT INTO countries (country_name, iso_code, phone_code) VALUES
('Guatemala', 'GT', '+502'),
('El Salvador', 'SV', '+503'),
('Honduras', 'HN', '+504');

-- Usuario Administrador Inicial (password: admin123)
-- hash generado: $2a$10$a1TIpYdh1zkDXx75k5wzY.SI8VIXt0bGjNkdyXbfFKGy59csvMe6C
INSERT INTO users (role_id, firstName, lastname, phone, email, hash_password)
SELECT role_id, 'Super', 'Admin', '00000000', 'admin@smallbox.com', '$2a$10$a1TIpYdh1zkDXx75k5wzY.SI8VIXt0bGjNkdyXbfFKGy59csvMe6C'
FROM roles WHERE role_name = 'SUPER_ADMIN';
