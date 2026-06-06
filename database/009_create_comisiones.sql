use rambed_db;
-- Registra la comision generada por cada factura
CREATE TABLE comisiones (
  id                    BIGINT UNSIGNED  AUTO_INCREMENT PRIMARY KEY,
  factura_id            BIGINT UNSIGNED  NOT NULL UNIQUE,
  vendedor_id           BIGINT UNSIGNED  NOT NULL,

  -- Comision al vender (0% o 2%)
  porc_comision_venta   DECIMAL(5,2)     NOT NULL DEFAULT 0.00,
  monto_comision_venta  DECIMAL(10,2)    NOT NULL DEFAULT 0.00,

  -- Comision al pagar (por defecto 4%, editable)
  porc_comision_pago    DECIMAL(5,2)     NULL DEFAULT NULL,
  monto_comision_pago   DECIMAL(10,2)    NULL DEFAULT NULL,

  -- Liquidacion a fin de mes
  liquidada             INT NOT NULL DEFAULT 0,
  fecha_liquidacion     DATETIME         NULL DEFAULT NULL,

  creado_en             DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado_en        DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP,

  FOREIGN KEY (factura_id)  REFERENCES facturas(id),
  FOREIGN KEY (vendedor_id) REFERENCES vendedores(id)
);