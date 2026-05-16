USE rambed_db;

CREATE TABLE devoluciones (
  id               INT UNSIGNED  AUTO_INCREMENT PRIMARY KEY,
  factura_detalle_id INT UNSIGNED NOT NULL,
  cantidad         INT UNSIGNED  NOT NULL,
  motivo           TEXT,
  creado_en        DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (factura_detalle_id) REFERENCES factura_detalle(id)
);