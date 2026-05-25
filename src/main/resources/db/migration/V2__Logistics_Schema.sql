-- Módulo Logístico
CREATE TABLE recipient (
  recipient_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  firstName TEXT NOT NULL,
  secondName TEXT,
  lastname TEXT NOT NULL,
  secondLastname TEXT,
  phone VARCHAR(20) NOT NULL,
  email TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT now(),
  created_by UUID,
  updated_at TIMESTAMP,
  updated_by UUID,
  deleted_at TIMESTAMP,
  deleted_by UUID
);

CREATE TABLE branch (
  branch_id SERIAL PRIMARY KEY,
  name_branch VARCHAR(100),
  department_id INT NOT NULL REFERENCES department(id_department),
  phone_branch VARCHAR(20),
  created_at TIMESTAMP DEFAULT now(),
  created_by UUID,
  updated_at TIMESTAMP,
  updated_by UUID,
  deleted_at TIMESTAMP,
  deleted_by UUID
);

CREATE TABLE branch_user (
  branch_id INT REFERENCES branch(branch_id),
  user_id UUID REFERENCES users(user_id),
  active BOOLEAN DEFAULT true,
  created_at TIMESTAMP DEFAULT now(),
  created_by UUID,
  updated_at TIMESTAMP,
  updated_by UUID,
  deleted_at TIMESTAMP,
  deleted_by UUID,
  PRIMARY KEY (branch_id, user_id)
);

-- Módulo de Envios
CREATE TABLE shipment_status (
  status_id INT PRIMARY KEY,
  status_name VARCHAR(50),
  description VARCHAR(150),
  created_at TIMESTAMP DEFAULT now(),
  created_by UUID,
  updated_at TIMESTAMP,
  updated_by UUID,
  deleted_at TIMESTAMP,
  deleted_by UUID
);

CREATE TABLE package_type (
  package_type_id INT PRIMARY KEY,
  type_name VARCHAR(50),
  description VARCHAR(150)
);

CREATE TABLE shipment (
  shipment_id BIGSERIAL PRIMARY KEY,
  tracking_number VARCHAR(30) UNIQUE,
  user_id UUID REFERENCES users(user_id),
  recipient_id UUID REFERENCES recipient(recipient_id),
  city_id INT NOT NULL REFERENCES department(id_department),
  exact_address TEXT NOT NULL,
  shipment_status_id INT REFERENCES shipment_status(status_id),
  shipment_date TIMESTAMP,
  delivery_date TIMESTAMP,
  price DECIMAL(10,2),
  branch_from INT REFERENCES branch(branch_id),
  branch_to INT REFERENCES branch(branch_id),
  processed_by UUID REFERENCES users(user_id),
  created_at TIMESTAMP DEFAULT now(),
  created_by UUID,
  updated_at TIMESTAMP,
  updated_by UUID,
  deleted_at TIMESTAMP,
  deleted_by UUID
);

CREATE TABLE package (
  package_id BIGSERIAL PRIMARY KEY,
  shipment_id BIGINT REFERENCES shipment(shipment_id),
  package_type_id INT REFERENCES package_type(package_type_id),
  height DECIMAL(10,2),
  width DECIMAL(10,2),
  length DECIMAL(10,2),
  weight DECIMAL(10,2),
  description VARCHAR(200),
  fragile BOOLEAN DEFAULT false,
  created_at TIMESTAMP DEFAULT now(),
  created_by UUID,
  updated_at TIMESTAMP,
  updated_by UUID,
  deleted_at TIMESTAMP,
  deleted_by UUID
);

-- Módulo de Auditoría
CREATE TABLE shipment_status_history (
  history_id BIGSERIAL PRIMARY KEY,
  shipment_id BIGINT REFERENCES shipment(shipment_id),
  status_id INT REFERENCES shipment_status(status_id),
  branch_id INT REFERENCES branch(branch_id),
  user_id UUID REFERENCES users(user_id),
  changed_at TIMESTAMP DEFAULT now(),
  notes TEXT,
  created_at TIMESTAMP DEFAULT now(),
  created_by UUID,
  updated_at TIMESTAMP,
  updated_by UUID,
  deleted_at TIMESTAMP,
  deleted_by UUID
);
