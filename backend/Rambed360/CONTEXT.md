# CONTEXT.md — Rambed360

Documento técnico para continuar el desarrollo sin perder contexto.
Leer antes de escribir cualquier código.

---

## Sobre el proyecto

App de gestión interna para empresa textil. Maneja inventario de
jeans y camisas, clientes, facturación y devoluciones.
Máximo 2 usuarios simultáneos. Uso interno, no público.

---

## Stack

- Java 17 + Spring Boot 3.3 + Maven
- React 18 + Vite
- MySQL 8
- JWT con jjwt 0.11.5
- Spring Security
- Axios para peticiones HTTP

---

## Estilo de código — MUY IMPORTANTE

El desarrollador es junior. Todo el código debe seguir estas reglas
sin excepción:

### Java
- Sin `@Data`, sin `@RequiredArgsConstructor` de Lombok
- Constructor vacío obligatorio para JPA
- Constructor con campos obligatorios explícito
- Getters y setters explícitos, uno por línea
- Variables intermedias antes del return, nunca return directo
- `if/else` en vez de ternarios
- `if (resultado.isPresent() == false)` en vez de `if (!resultado.isPresent())`
- Comentario en cada línea explicando qué hace
- Sin streams ni lambdas, usar for loops clásicos

### React
- Sin destructuring de objetos
- `objeto.propiedad` en vez de `const { propiedad } = objeto`
- `function` en vez de arrow functions donde sea posible
- Variables intermedias antes del return
- Comentario en cada línea explicando qué hace
- CSS en archivos separados, no CSS-in-JS excepto en FacturaPrint
  que usa estilos en línea porque se imprime en ventana nueva

---

## Estructura de carpetas

rambed-app/
├── database/migrations/     # Scripts SQL numerados 001, 002...
├── backend/src/main/java/com/rambed/rambed360/
│   ├── entity/              # Entidades JPA + enums
│   ├── repository/          # Interfaces JpaRepository
│   ├── service/             # Lógica de negocio con DTOs
│   ├── controller/          # Endpoints REST
│   ├── dto/
│   │   ├── request/         # Lo que recibe el backend
│   │   └── response/        # Lo que devuelve el backend
│   ├── security/            # JWT, filtros, Spring Security
│   └── config/              # CORS
└── frontend/src/
├── api/                 # Un archivo por módulo con Axios
├── components/          # Modal, Sidebar, Layout, FacturaPrint
├── context/             # AuthContext
├── pages/               # Una página por módulo
├── routes/              # RutaProtegida, RutaAdmin
└── styles/              # global.css + css por módulo

---

## Decisiones técnicas importantes

### Base de datos
- Todas las IDs son `BIGINT UNSIGNED` — se aprendió que `INT UNSIGNED`
  causa error de validación con Hibernate porque Java usa `Long` = BIGINT
- El ENUM de tallas tiene prefijo T: `T28`, `T30`, `XS`, `M` etc.
  porque MySQL no acepta valores que empiecen con número en ENUM
- El campo `activo` es `TINYINT` no `BOOLEAN` — MySQL convierte
  BOOLEAN a TINYINT internamente, se prefiere ser explícito
- `creado_en` y `actualizado_en` los maneja la base de datos con
  `DEFAULT CURRENT_TIMESTAMP`, no el código Java

### Entities
- `@JsonIgnore` en `creadoEn` y `actualizadoEn` — no se exponen al frontend
- `@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})` en
  relaciones `@ManyToOne` para evitar loops infinitos en JSON
- `@Column(name = "creado_en", updatable = false, insertable = false)`
  porque la base de datos maneja esa fecha, no Hibernate
- Sin `@PrePersist` — la base de datos maneja las fechas

### DTOs
Regla: si el Entity tiene relaciones anidadas que causarían loop → DTO
Si el Entity es simple → `@JsonIgnore` directo, sin DTO

| Entity | ¿Tiene DTO? | Razón |
|---|---|---|
| Vendedor | NO | Sin relaciones anidadas |
| Referencia | NO | Sin relaciones anidadas |
| Cliente | NO | @JsonIgnoreProperties es suficiente |
| Inventario | SÍ | Necesita aplanar Referencia |
| Factura | SÍ | Necesita aplanar Cliente y Vendedor |
| FacturaDetalle | SÍ | Relaciones anidadas profundas |
| Devolucion | SÍ | Viene de FacturaDetalle que viene de Factura |

### Services
- Reciben DTOs Request y retornan DTOs Response
- Método privado `convertirAResponse()` al final de cada Service
  para no repetir la conversión en cada método
- Validaciones explícitas con `if` antes de cualquier operación
- `buscarPorId()` privado en cada Service para reutilizar

### Controllers
- Sin `@RequiredArgsConstructor` — constructor explícito siempre
- Variables intermedias antes del return:
```java
  Vendedor vendedorGuardado = vendedorService.guardar(vendedor);
  return ResponseEntity.ok(vendedorGuardado);
```
- Sin `@CrossOrigin` — el CORS está en `CorsConfig.java` global

### Seguridad
- JWT con clave fija en `application.properties` como `jwt.secret`
  Si la clave es aleatoria el token falla al reiniciar el servidor
- `@PostConstruct` en `JwtUtil` para inicializar la clave después
  de que Spring inyecte el valor de `application.properties`
- `SecurityConfig` maneja todos los permisos por ruta
  No usar `@PreAuthorize` en los controllers
- BCrypt para passwords — nunca guardar passwords en texto plano
- El rol en el token JWT se guarda como `"VENDEDOR"` pero Spring
  Security lo busca como `"ROLE_VENDEDOR"` — por eso en
  `UserDetailsServiceImpl` se agrega `"ROLE_"` adelante

### Número de factura
- Se genera automáticamente en el backend con prefijo `REM-`
- Formato: `REM-0001`, `REM-0002`, etc.
- El frontend NO envía el número, el backend lo genera solo
- `FacturaRequest` no tiene campo `numeroFactura`

### Tallas — ENUM
```java
public enum Talla {
    T4, T6, T8, T10, T12, T14, T16, T18, T20, T22,  // dama/junior
    T28, T29, T30, T31, T32, T33, T34, T36, T38, T40, T42, T44, // caballero
    XS, S, M, L, XL, XXL;  // camisas
}
```
El `@Enumerated(EnumType.STRING)` es obligatorio — sin él Hibernate
guarda el índice numérico y el UNIQUE KEY falla con `Duplicate entry '1'`

---

## Errores resueltos — no repetir

### 1. ENUM tallas guardaba índice numérico
**Síntoma:** `Duplicate entry '1' for key 'inventario.uq_ref_talla'`
**Causa:** Faltaba `@Enumerated(EnumType.STRING)` en el Entity
**Solución:** Agregar la anotación en el campo talla del Entity Inventario

### 2. UNIQUE KEY incompleto
**Síntoma:** Solo dejaba agregar una talla por referencia
**Causa:** El `UNIQUE KEY uq_ref_talla` solo tenía `referencia_id`,
faltaba la columna `talla`
**Solución:** `ALTER TABLE inventario ADD UNIQUE KEY uq_ref_talla (referencia_id, talla)`

### 3. JWT SignatureException
**Síntoma:** `JWT signature does not match locally computed signature`
**Causa:** `Keys.secretKeyFor()` genera clave aleatoria en cada arranque
**Solución:** Clave fija en `application.properties` + `@PostConstruct` en JwtUtil

### 4. BCrypt hash incorrecto
**Síntoma:** Login falla con credenciales correctas
**Causa:** El hash en el INSERT SQL no fue generado por BCrypt de Spring
**Solución:** Endpoint temporal `GET /api/auth/hash?password=xxx`,
copiar el hash y hacer UPDATE en la base de datos.
**IMPORTANTE:** Borrar ese endpoint después de usarlo

### 5. INT vs BIGINT
**Síntoma:** `Schema validation: wrong column type found [int], but expecting [bigint]`
**Causa:** MySQL tenía `INT UNSIGNED` pero Java usa `Long` que mapea a `BIGINT`
**Solución:** Cambiar todas las IDs a `BIGINT UNSIGNED` en MySQL

### 6. @Value no inyecta en campo final
**Síntoma:** Error al arrancar con `@Value` en campo `final`
**Causa:** Spring no puede inyectar en campos finales
**Solución:** Quitar `final`, agregar `@PostConstruct` para inicializar

---

## Paleta de colores frontend

```css
--fondo-principal:    #0f0f0f
--fondo-secundario:   #1a1a1a
--fondo-terciario:    #242424
--rojo-principal:     #e63946
--rojo-hover:         #c1121f
--gris-texto:         #a0a0a0
--blanco-texto:       #f0f0f0
--borde:              #2e2e2e
--exito-fondo:        #1a3a2a
--exito-texto:        #4caf82
--peligro-fondo:      #3b0a0a
--peligro-texto:      #ff6b6b
--amarillo-fondo:     #3a2e00
--amarillo-texto:     #ffc107
```

---

## Permisos por rol

| Endpoint | ADMIN | VENDEDOR |
|---|---|---|
| `/api/vendedores/**` | ✅ | ❌ |
| `/api/referencias/**` | ✅ | ❌ |
| `/api/usuarios/**` | ✅ | ❌ |
| `/api/inventario/**` | ✅ | ✅ |
| `/api/clientes/**` | ✅ | ✅ |
| `/api/facturas/**` | ✅ | ✅ |
| `/api/factura-detalle/**` | ✅ | ✅ |
| `/api/devoluciones/**` | ✅ | ✅ |

El VENDEDOR ve solo sus propias facturas y clientes.
Esto se controla en el Service con el `vendedorId` del token,
no en el SecurityConfig.

---

## Pendiente por desarrollar

- Costo real del inventario para calcular utilidades
- Top 10 clientes por total facturado en dashboard
- Top 10 referencias más vendidas por unidades en dashboard
- Despliegue en VPS Hostinger con Nginx
- Reportes de ventas por periodo
- Recuperación de contraseña

---

## Credenciales locales por defecto

### Pagos parciales y comisiones
- `factura_pagos` registra cada abono a una factura — una factura puede
  tener varios abonos antes de marcarse como pagada
- `total_pagado` en facturas acumula los abonos, no se calcula, se persiste
- La factura NO se marca pagada automaticamente — hay un botón manual
  que dispara el endpoint `/pagar` y en ese momento se calcula la comisión de pago
- `comisiones` tiene una fila por factura (UNIQUE en factura_id)
- `porc_comision_venta` en facturas es nullable — no todas las facturas
  tienen comisión de venta (vendedor con 0% siempre)
- La comisión de venta se calcula al crear la factura sobre el subtotal
- La comisión de pago se calcula al marcar pagada sobre el total final
  (ya con descuento aplicado si hubo)
- `liquidada` es TINYINT 0/1 — se filtra con `findByLiquidada(0)` para
  ver pendientes y `findByLiquidada(1)` para liquidadas
- La liquidación marca todas las comisiones pendientes de un vendedor
  con `liquidada=1` y registra `fecha_liquidacion` con `LocalDateTime.now()`
- `PagoRequest` se reutilizó — se le agregó `porcComisionPago` en vez
  de crear un DTO nuevo
- Los métodos de comisiones y abonos viven en `FacturaService` y
  `FacturaController` — no se crearon servicios separados

  ### 7. Comisión de venta se calcula sobre subtotal, no total
- La factura al crearse no tiene descuento todavía — el descuento
  se aplica al momento de pagar
- Por eso `monto_comision_venta` se calcula sobre `subtotal`
- Y `monto_comision_pago` se calcula sobre `total` (ya con descuento)

| FacturaPago     | SÍ | Relación anidada con Factura |
| Comision        | SÍ | Relaciones anidadas profundas |

- Pagos parciales con historial de abonos por factura ✅
- Comisiones por vendedor con liquidación mensual ✅
- Despliegue en VPS Hostinger con Nginx
- Reportes de ventas por periodo
- Recuperación de contraseña



