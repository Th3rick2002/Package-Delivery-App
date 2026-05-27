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

-- Seed de Paises iniciales
INSERT INTO countries (country_name, iso_code, phone_code) VALUES
('Guatemala', 'GT', '+502'),
('El Salvador', 'SV', '+503'),
('Honduras', 'HN', '+504');

-- Seed de Departamentos - Guatemala
INSERT INTO department (deparment_name, country_id, delivery_zone_code)
SELECT d.name, c.country_id, d.zone
FROM (VALUES 
    ('Alta Verapaz', 'N/A'),
    ('Baja Verapaz', 'N/A'),
    ('Chimaltenango', 'N/A'),
    ('Chiquimula', 'N/A'),
    ('El Progreso', 'N/A'),
    ('Escuintla', 'N/A'),
    ('Guatemala', 'N/A'),
    ('Huehuetenango', 'N/A'),
    ('Izabal', 'N/A'),
    ('Jalapa', 'N/A'),
    ('Jutiapa', 'N/A'),
    ('Petén', 'N/A'),
    ('Quetzaltenango', 'N/A'),
    ('Quiché', 'N/A'),
    ('Retalhuleu', 'N/A'),
    ('Sacatepéquez', 'N/A'),
    ('San Marcos', 'N/A'),
    ('Santa Rosa', 'N/A'),
    ('Sololá', 'N/A'),
    ('Suchitepéquez', 'N/A'),
    ('Totonicapán', 'N/A'),
    ('Zacapa', 'N/A')
) AS d(name, zone)
CROSS JOIN (SELECT country_id FROM countries WHERE iso_code = 'GT') AS c;

-- Seed de Departamentos - El Salvador
INSERT INTO department (deparment_name, country_id, delivery_zone_code)
SELECT d.name, c.country_id, d.zone
FROM (VALUES 
    ('Ahuachapán', 'OCCIDENTAL'),
    ('Santa Ana', 'OCCIDENTAL'),
    ('Sonsonate', 'OCCIDENTAL'),
    ('Chalatenango', 'CENTRAL'),
    ('La Libertad', 'CENTRAL'),
    ('San Salvador', 'CENTRAL'),
    ('Cuscatlán', 'CENTRAL'),
    ('La Paz', 'PARACENTRAL'),
    ('Cabañas', 'PARACENTRAL'),
    ('San Vicente', 'PARACENTRAL'),
    ('Usulután', 'ORIENTAL'),
    ('San Miguel', 'ORIENTAL'),
    ('Morazán', 'ORIENTAL'),
    ('La Unión', 'ORIENTAL')
) AS d(name, zone)
CROSS JOIN (SELECT country_id FROM countries WHERE iso_code = 'SV') AS c;

-- Seed de Departamentos - Honduras
INSERT INTO department (deparment_name, country_id, delivery_zone_code)
SELECT d.name, c.country_id, d.zone
FROM (VALUES 
    ('Atlántida', 'N/A'),
    ('Choluteca', 'N/A'),
    ('Colón', 'N/A'),
    ('Comayagua', 'N/A'),
    ('Copán', 'N/A'),
    ('Cortés', 'N/A'),
    ('El Paraíso', 'N/A'),
    ('Francisco Morazán', 'N/A'),
    ('Gracias a Dios', 'N/A'),
    ('Intibucá', 'N/A'),
    ('Islas de la Bahía', 'N/A'),
    ('La Paz', 'N/A'),
    ('Lempira', 'N/A'),
    ('Ocotepeque', 'N/A'),
    ('Olancho', 'N/A'),
    ('Santa Bárbara', 'N/A'),
    ('Valle', 'N/A'),
    ('Yoro', 'N/A')
) AS d(name, zone)
CROSS JOIN (SELECT country_id FROM countries WHERE iso_code = 'HN') AS c;

-- Usuario Administrador Inicial (password: admin123)
-- hash generado: $2a$10$a1TIpYdh1zkDXx75k5wzY.SI8VIXt0bGjNkdyXbfFKGy59csvMe6C
INSERT INTO users (role_id, firstName, lastname, phone, email, hash_password)
SELECT role_id, 'Super', 'Admin', '00000000', 'admin@smallbox.com', '$2a$10$a1TIpYdh1zkDXx75k5wzY.SI8VIXt0bGjNkdyXbfFKGy59csvMe6C'
FROM roles WHERE role_name = 'SUPER_ADMIN';

INSERT INTO users (role_id, firstName, lastname, phone, email, hash_password)
SELECT role_id, 'Branch', 'Admin', '00000000', 'manager@smallbox.com', '$2a$10$a1TIpYdh1zkDXx75k5wzY.SI8VIXt0bGjNkdyXbfFKGy59csvMe6C'
FROM roles WHERE role_name = 'BRANCH_ADMIN';

INSERT INTO users (role_id, firstName, lastname, phone, email, hash_password)
SELECT role_id, 'Branch', 'employee', '00000000', 'employee@smallbox.com', '$2a$10$a1TIpYdh1zkDXx75k5wzY.SI8VIXt0bGjNkdyXbfFKGy59csvMe6C'
FROM roles WHERE role_name = 'EMPLOYEE';