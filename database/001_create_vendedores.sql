-- Crear base de datos
CREATE DATABASE IF NOT EXISTS rambed_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE rambed_db;

-- Tabla vendedores
CREATE TABLE vendedores (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  nombre     VARCHAR(100) NOT NULL,
  identificacion VARCHAR(20) NOT NULL UNIQUE,
  telefono   VARCHAR(20),
  correo     VARCHAR(100),
  activo     TINYINT(1) NOT NULL DEFAULT 1,
  creado_en  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);