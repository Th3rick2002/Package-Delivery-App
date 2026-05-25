-- Extensiones necesarias
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Módulo de Usuarios y Roles
CREATE TABLE roles (
  role_id SERIAL PRIMARY KEY,
  role_name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE users (
  user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  role_id INT NOT NULL REFERENCES roles(role_id),
  firstName TEXT NOT NULL,
  secondName TEXT,
  lastname TEXT NOT NULL,
  secondLastname TEXT,
  phone VARCHAR(20) NOT NULL,
  email VARCHAR(120) NOT NULL UNIQUE,
  hash_password TEXT NOT NULL,
  lastLogin TIMESTAMP,
  created_at TIMESTAMP DEFAULT now(),
  updated_at TIMESTAMP,
  deleted_at TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);

-- Módulo de Autenticación
CREATE TABLE sessions (
  id UUID PRIMARY KEY,
  refresh_token_hash TEXT NOT NULL,
  user_id UUID NOT NULL REFERENCES users(user_id),
  revoked BOOLEAN DEFAULT false,
  expired_at TIMESTAMP NOT NULL,
  ip_address INET NOT NULL,
  user_agent TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT now(),
  last_used_at TIMESTAMP,
  replaced_by UUID REFERENCES sessions(id)
);

CREATE INDEX idx_sessions_expired_at ON sessions(expired_at);

CREATE TABLE password_reset_tokens (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES users(user_id),
  token_hash TEXT NOT NULL UNIQUE,
  expires_at TIMESTAMP NOT NULL,
  used_at TIMESTAMP,
  ip_address INET NOT NULL,
  user_agent TEXT,
  created_at TIMESTAMP DEFAULT now()
);

CREATE INDEX idx_prt_user_id ON password_reset_tokens(user_id);

-- Módulo de Direcciones
CREATE TABLE countries (
  country_id SERIAL PRIMARY KEY,
  country_name TEXT NOT NULL UNIQUE, 
  iso_code CHAR(2) NOT NULL UNIQUE,
  phone_code VARCHAR(5)
);

CREATE TABLE department (
  id_department SERIAL PRIMARY KEY,
  deparment_name TEXT NOT NULL,
  country_id INT NOT NULL REFERENCES countries(country_id),
  delivery_zone_code VARCHAR(20),
  CONSTRAINT uq_dept_country UNIQUE (deparment_name, country_id)
);

CREATE TABLE user_addresses (
  id_user_address UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES users(user_id),
  deparment_id INT NOT NULL REFERENCES department(id_department),
  street_line TEXT NOT NULL,
  zip_code VARCHAR(20) NOT NULL,
  is_default BOOLEAN DEFAULT false
);

CREATE INDEX idx_user_addr_user ON user_addresses(user_id);
CREATE INDEX idx_user_addr_dept ON user_addresses(deparment_id);
