USE rambed_db;

CREATE TABLE factura_detalle (
  id              INT UNSIGNED  AUTO_INCREMENT PRIMARY KEY,
  factura_id      INT UNSIGNED  NOT NULL,
  inventario_id   INT UNSIGNED  NOT NULL,
  cantidad        INT UNSIGNED  NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  creado_en       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (factura_id)    REFERENCES facturas(id),
  FOREIGN KEY (inventario_id) REFERENCES inventario(id)
);