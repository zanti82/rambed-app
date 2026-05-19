USE rambed_db;

-- Tabla roles
CREATE TABLE roles (
  id        BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre    VARCHAR(20) NOT NULL UNIQUE
);

-- Tabla usuarios
CREATE TABLE usuarios (
  id           BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  username     VARCHAR(50)     NOT NULL UNIQUE,
  password     VARCHAR(255)    NOT NULL,
  activo       TINYINT         NOT NULL DEFAULT 1,
  rol_id       BIGINT UNSIGNED NOT NULL,
  vendedor_id  BIGINT UNSIGNED NULL,
  creado_en    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (rol_id)      REFERENCES roles(id),
  FOREIGN KEY (vendedor_id) REFERENCES vendedores(id),
  UNIQUE KEY uq_vendedor (vendedor_id)
);

-- Roles iniciales
INSERT INTO roles (nombre) VALUES ('ADMIN');
INSERT INTO roles (nombre) VALUES ('VENDEDOR');

-- Usuario admin por defecto
-- password: admin123 encriptado con BCrypt
INSERT INTO usuarios (username, password, activo, rol_id, vendedor_id)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt9oBm2', 1, 1, NULL);