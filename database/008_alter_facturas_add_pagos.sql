use rambed_db;
-- Agrega campos de pagos parciales a facturas
ALTER TABLE facturas
  ADD COLUMN total_pagado      DECIMAL(10,2) NOT NULL DEFAULT 0.00
             AFTER total,
  ADD COLUMN porc_comision_venta DECIMAL(5,2) NULL DEFAULT NULL
             AFTER total_pagado;

-- Registra cada abono parcial a una factura
CREATE TABLE factura_pagos (
  id           BIGINT UNSIGNED  AUTO_INCREMENT PRIMARY KEY,
  factura_id   BIGINT UNSIGNED  NOT NULL,
  monto        DECIMAL(10,2)    NOT NULL,
  fecha_pago   DATE             NOT NULL,
  notas        TEXT,
  creado_en    DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (factura_id) REFERENCES facturas(id)
);