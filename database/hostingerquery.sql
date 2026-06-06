USE u475372255_rambed_db ;
SHOW TABLES;

DESCRIBE inventario;
DESCRIBE facturas;
DESCRIBE usuarios;

SELECT * FROM roles;
SELECT id, username, activo, rol_id FROM usuarios;

INSERT INTO usuarios (username, password, activo, rol_id, vendedor_id)
VALUES ('admin', '$2a$10$xvQDcH8unwUCeoskthYnuOOn/uxdnZ.5o81R6yOgIREgQxvztyFKS', 1, 1, NULL);