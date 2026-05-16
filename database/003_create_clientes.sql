USE rambed_db;

CREATE TABLE clientes (
  id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre        VARCHAR(100) NOT NULL,
  identificacion VARCHAR(20) NOT NULL UNIQUE,
  ciudad        VARCHAR(100) NOT NULL,
  direccion     VARCHAR(255) NOT NULL,
  correo        VARCHAR(100),
  telefono      VARCHAR(20)  NOT NULL,
  nombre_almacen VARCHAR(100) NOT NULL,
  vendedor_id   INT NOT NULL,
  instagram     VARCHAR(100),
  facebook      VARCHAR(100),
  whatsapp      VARCHAR(20),
  activo        TINYINT(1)   NOT NULL DEFAULT 1,
  creado_en     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado_en DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (vendedor_id) REFERENCES vendedores(id)
);