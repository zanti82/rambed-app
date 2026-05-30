USE rambed_db;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS devoluciones;
DROP TABLE IF EXISTS factura_detalle;
DROP TABLE IF EXISTS facturas;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS inventario;
DROP TABLE IF EXISTS referencias;
DROP TABLE IF EXISTS vendedores;

SET FOREIGN_KEY_CHECKS = 1;

-- ─── VENDEDORES ────────────────────────────────────────────────────────────
CREATE TABLE vendedores (
  id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre        VARCHAR(100)    NOT NULL,
  telefono      VARCHAR(20),
  correo        VARCHAR(100),
  activo        TINYINT         NOT NULL DEFAULT 1,
  creado_en     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ─── REFERENCIAS ──────────────────────────────────────────────────────────
CREATE TABLE referencias (
  id              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  marca           VARCHAR(100)    NOT NULL,
  referencia      VARCHAR(100)    NOT NULL,
  descripcion     VARCHAR(255),
  creado_en       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado_en  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uq_marca_referencia (marca, referencia)
);

-- ─── INVENTARIO ───────────────────────────────────────────────────────────
CREATE TABLE inventario (
  id              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  referencia_id   BIGINT UNSIGNED NOT NULL,
  talla           ENUM('T4','T6','T8','T10','T12','T14','T16','T18','T20','T22',
                       'T28','T29','T30','T31','T32','T33','T34','T36','T38','T40',
                       'T42','T44',
                       'XS','S','M','L','XL','XXL') NOT NULL,
  cantidad        INT UNSIGNED    NOT NULL DEFAULT 0,
  precio          DECIMAL(10,2)   NOT NULL DEFAULT 0.00,
  costo 			DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  creado_en       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado_en  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (referencia_id) REFERENCES referencias(id),
  UNIQUE KEY uq_ref_talla (referencia_id, talla)
);

-- ─── CLIENTES ─────────────────────────────────────────────────────────────
CREATE TABLE clientes (
  id              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre          VARCHAR(100)    NOT NULL,
  ciudad          VARCHAR(100)    NOT NULL,
  direccion       VARCHAR(255)    NOT NULL,
  correo          VARCHAR(100),
  telefono        VARCHAR(20)     NOT NULL,
  nombre_almacen  VARCHAR(100)    NOT NULL,
  vendedor_id     BIGINT UNSIGNED NOT NULL,
  instagram       VARCHAR(100),
  facebook        VARCHAR(100),
  whatsapp        VARCHAR(20),
  activo          TINYINT         NOT NULL DEFAULT 1,
  creado_en       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado_en  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (vendedor_id) REFERENCES vendedores(id)
);

-- ─── FACTURAS ─────────────────────────────────────────────────────────────
CREATE TABLE facturas (
  id                    BIGINT UNSIGNED  AUTO_INCREMENT PRIMARY KEY,
  numero_factura        VARCHAR(20)      NOT NULL UNIQUE,
  cliente_id            BIGINT UNSIGNED  NOT NULL,
  vendedor_id           BIGINT UNSIGNED  NOT NULL,
  fecha_emision         DATE             NOT NULL,
  fecha_pago            DATE,
  descuento_porcentaje  DECIMAL(5,2)     DEFAULT 0.00,
  subtotal              DECIMAL(10,2)    NOT NULL DEFAULT 0.00,
  total                 DECIMAL(10,2)    NOT NULL DEFAULT 0.00,
  estado                ENUM('pendiente','pagada','anulada') NOT NULL DEFAULT 'pendiente',
  notas                 TEXT,
  creado_en             DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado_en        DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (cliente_id)  REFERENCES clientes(id),
  FOREIGN KEY (vendedor_id) REFERENCES vendedores(id)
);

-- ─── FACTURA DETALLE ──────────────────────────────────────────────────────
CREATE TABLE factura_detalle (
  id               BIGINT UNSIGNED  AUTO_INCREMENT PRIMARY KEY,
  factura_id       BIGINT UNSIGNED  NOT NULL,
  inventario_id    BIGINT UNSIGNED  NOT NULL,
  cantidad         INT UNSIGNED     NOT NULL,
  precio_unitario  DECIMAL(10,2)    NOT NULL,
  creado_en        DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (factura_id)    REFERENCES facturas(id),
  FOREIGN KEY (inventario_id) REFERENCES inventario(id)
);

-- ─── DEVOLUCIONES ─────────────────────────────────────────────────────────
CREATE TABLE devoluciones (
  id                   BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  factura_detalle_id   BIGINT UNSIGNED NOT NULL,
  cantidad             INT UNSIGNED    NOT NULL,
  motivo               TEXT,
  creado_en            DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (factura_detalle_id) REFERENCES factura_detalle(id)
);


-- Agregar identificacion unica a vendedores
ALTER TABLE vendedores 
ADD COLUMN identificacion VARCHAR(20) NOT NULL UNIQUE AFTER nombre;

-- Agregar identificacion unica a clientes
ALTER TABLE clientes 
ADD COLUMN identificacion VARCHAR(20) NOT NULL UNIQUE AFTER nombre;

ALTER TABLE referencias 
ADD COLUMN activo TINYINT NOT NULL DEFAULT 1 AFTER descripcion;

alter table inventario DROP column TALLA;

alter table inventario
ADD column
talla           ENUM('T4','T6','T8','T10','T12','T14','T16','T18','T20','T22',
                       'T28','T29','T30','T31','T32','T33','T34','T36','T38','T40',
                       'T42','T44',
                       'XS','S','M','L','XL','XXL') NOT NULL;