# Rambed360

Sistema de gestión empresarial para control de inventario, clientes y facturación, diseñado para empresas del sector textil y de moda.

## Stack tecnológico

- **Backend:** Java con Spring Boot
- **Frontend:** React
- **Base de datos:** MySQL hostinger
- **Despliegue:** por definir

## Módulos

- **Inventario** — registro de productos por marca, referencia y talla
- **Clientes** — directorio de clientes con vendedor asignado
- **Facturación** — generación de facturas con descuentos y control de pagos
- **Devoluciones** — registro de devoluciones con reintegro automático al inventario

## Estructura de la base de datos

| Tabla | Descripción |
|---|---|
| `vendedores` | Equipo de ventas asignado a clientes |
| `referencias` | Catálogo de productos por marca |
| `inventario` | Stock por referencia y talla |
| `clientes` | Directorio de clientes y almacenes |
| `facturas` | Registro de ventas y pagos |
| `factura_detalle` | Detalle de productos por factura |
| `devoluciones` | Control de devoluciones |

## Correr en local

### Requisitos
- Java 17+
- Node.js 18+
- MySQL 8+

### Base de datos
```bash
mysql -u root -p < database/migrations/001_create_vendedores.sql
mysql -u root -p < database/migrations/002_create_inventario.sql
mysql -u root -p < database/migrations/003_create_clientes.sql
mysql -u root -p < database/migrations/004_create_facturas.sql
mysql -u root -p < database/migrations/005_create_factura_detalle.sql
mysql -u root -p < database/migrations/006_create_devoluciones.sql
```

### Backend Spring Boot
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend React
```bash
cd frontend
npm install
npm run dev
```

## Roadmap

- [x] Estructura de base de datos
- [ ] API REST con Spring Boot
- [ ] Interfaz React
- [ ] Autenticación y roles de usuario
- [ ] Reportes de ventas
- [ ] Despliegue en Hostinger
