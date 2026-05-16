USE rambed_db;

CREATE TABLE facturas (
  id                  INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  numero_factura      VARCHAR(20)    NOT NULL UNIQUE,
  cliente_id          INT UNSIGNED   NOT NULL,
  vendedor_id         INT NOT NULL,
  fecha_emision       DATE           NOT NULL,
  fecha_pago          DATE,
  descuento_porcentaje DECIMAL(5,2)  DEFAULT 0.00,
  subtotal            DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
  total               DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
  estado              ENUM('pendiente','pagada','anulada') NOT NULL DEFAULT 'pendiente',
  notas               TEXT,
  creado_en           DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado_en      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (cliente_id)  REFERENCES clientes(id),
  FOREIGN KEY (vendedor_id) REFERENCES vendedores(id)
);