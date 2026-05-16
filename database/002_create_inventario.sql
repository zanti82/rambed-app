USE rambed_db;

-- Tabla referencias
CREATE TABLE referencias (
  id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  marca         VARCHAR(100) NOT NULL,
  referencia    VARCHAR(100) NOT NULL,
  descripcion   VARCHAR(255),
  activo TINYINT NOT NULL DEFAULT 1,
  creado_en     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uq_marca_referencia (marca, referencia)
);

-- Tabla inventario
CREATE TABLE inventario (
  id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  referencia_id INT UNSIGNED NOT NULL,
  talla         ENUM('4','6','8','10','12','14','16','18','20','22',
                     '28','29','30','31','32','33','34','36','38','40',
                     'XS','S','M','L','XL','XXL') NOT NULL,
  cantidad      INT UNSIGNED NOT NULL DEFAULT 0,
  precio        DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  creado_en     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (referencia_id) REFERENCES referencias(id),
  UNIQUE KEY uq_ref_talla (referencia_id, talla)
);