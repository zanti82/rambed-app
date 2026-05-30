use rambed_db;

select * from usuarios;
select * from vendedores;
select * from referencias;
select * from inventario;
select * from roles;
SELECT id, referencia_id, talla, cantidad FROM inventario;
SHOW INDEX FROM inventario;	

-- descativa llaves foraneas con 0 y con 1 las regresa
SET FOREIGN_KEY_CHECKS = 1; 
SET FOREIGN_KEY_CHECKS = 0;

-- anexando costo al inevnatario
ALTER TABLE inventario
ADD COLUMN costo DECIMAL(10,2);




UPDATE usuarios 
SET password = '$2a$10$xvQDcH8unwUCeoskthYnuOOn/uxdnZ.5o81R6yOgIREgQxvztyFKS' 
WHERE username = 'admin';