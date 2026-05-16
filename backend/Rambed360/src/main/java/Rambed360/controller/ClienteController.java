package Rambed360.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Rambed360.entity.Cliente;
import Rambed360.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    // Servicio que contiene la logica de negocio
    private final ClienteService clienteService;

    // Constructor explicito con la dependencia del servicio
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Retorna todos los clientes
    @GetMapping
    public List<Cliente> listarTodos() {
        List<Cliente> todosLosClientes = clienteService.listarTodos();
        return todosLosClientes;
    }

    // Retorna solo los clientes activos
    @GetMapping("/activos")
    public List<Cliente> listarActivos() {
        List<Cliente> clientesActivos = clienteService.listarActivos();
        return clientesActivos;
    }

    // Retorna clientes filtrados por ciudad
    @GetMapping("/ciudad/{ciudad}")
    public List<Cliente> listarPorCiudad(@PathVariable String ciudad) {
        List<Cliente> clientesPorCiudad = clienteService.listarPorCiudad(ciudad);
        return clientesPorCiudad;
    }

    // Retorna clientes asignados a un vendedor
    @GetMapping("/vendedor/{vendedorId}")
    public List<Cliente> listarPorVendedor(@PathVariable Long vendedorId) {
        List<Cliente> clientesPorVendedor = clienteService.listarPorVendedor(vendedorId);
        return clientesPorVendedor;
    }

    // Busca un cliente por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        Cliente clienteEncontrado = clienteService.buscarPorId(id);
        return ResponseEntity.ok(clienteEncontrado);
    }

    // Crea un cliente nuevo
    @PostMapping
    public ResponseEntity<Cliente> guardar(@RequestBody Cliente cliente) {
        Cliente clienteGuardado = clienteService.guardar(cliente);
        return ResponseEntity.ok(clienteGuardado);
    }

    // Actualiza los datos de un cliente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        Cliente clienteActualizado = clienteService.actualizar(id, cliente);
        return ResponseEntity.ok(clienteActualizado);
    }

    // Activa un cliente
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Cliente> activar(@PathVariable Long id) {
        Cliente clienteActivado = clienteService.activar(id);
        return ResponseEntity.ok(clienteActivado);
    }

    // Desactiva un cliente
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Cliente> desactivar(@PathVariable Long id) {
        Cliente clienteDesactivado = clienteService.desactivar(id);
        return ResponseEntity.ok(clienteDesactivado);
    }

    // SOLO DESARROLLO - elimina fisicamente el cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}