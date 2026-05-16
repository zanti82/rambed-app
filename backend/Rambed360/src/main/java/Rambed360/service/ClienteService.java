package Rambed360.service;

import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;

import Rambed360.entity.Cliente;
import Rambed360.entity.Vendedor;
import Rambed360.repository.ClienteRepository;
import Rambed360.repository.VendedorRepository;

@Service
public class ClienteService {

    // Repositorio para acceder a la tabla clientes
    private final ClienteRepository clienteRepository;

    // Repositorio para validar que el vendedor exista
    private final VendedorRepository vendedorRepository;

    // Constructor explicito con las dos dependencias
    public ClienteService(ClienteRepository clienteRepository, VendedorRepository vendedorRepository) {
        this.clienteRepository = clienteRepository;
        this.vendedorRepository = vendedorRepository;
    }

    // Retorna todos los clientes
    public List<Cliente> listarTodos() {
        // Trae todos los registros de la tabla clientes
        List<Cliente> todosLosClientes = clienteRepository.findAll();
        return todosLosClientes;
    }

    // Retorna solo los clientes activos
    public List<Cliente> listarActivos() {
        // Busca clientes con activo igual a 1
        List<Cliente> clientesActivos = clienteRepository.findByActivo((byte) 1);
        return clientesActivos;
    }

    // Retorna clientes por ciudad
    public List<Cliente> listarPorCiudad(String ciudad) {
        // Busca clientes que pertenezcan a esa ciudad
        List<Cliente> clientesPorCiudad = clienteRepository.findByCiudad(ciudad);
        return clientesPorCiudad;
    }

    // Retorna clientes asignados a un vendedor
    public List<Cliente> listarPorVendedor(Long vendedorId) {
        // Busca el vendedor en la base de datos
        Optional<Vendedor> resultado = vendedorRepository.findById(vendedorId);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Vendedor no encontrado con id: " + vendedorId);
        }

        // Guarda el vendedor encontrado
        Vendedor vendedor = resultado.get();

        // Busca todos los clientes de ese vendedor
        List<Cliente> clientesPorVendedor = clienteRepository.findByVendedor(vendedor);
        return clientesPorVendedor;
    }

    // Busca un cliente por su ID
    public Cliente buscarPorId(Long id) {
        // Intenta encontrar el cliente en la base de datos
        Optional<Cliente> resultado = clienteRepository.findById(id);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Cliente no encontrado con id: " + id);
        }

        // Guarda y retorna el cliente encontrado
        Cliente clienteEncontrado = resultado.get();
        return clienteEncontrado;
    }

    // Guarda un cliente nuevo validando que no haya duplicados
    public Cliente guardar(Cliente cliente) {

        // Valida que el nombre no venga vacio
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del cliente es obligatorio");
        }

        // Valida que la identificacion no venga vacia
        if (cliente.getIdentificacion() == null || cliente.getIdentificacion().trim().isEmpty()) {
            throw new RuntimeException("La identificacion del cliente es obligatoria");
        }

        // Valida que el telefono no venga vacio
        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) {
            throw new RuntimeException("El telefono del cliente es obligatorio");
        }

        // Valida que la ciudad no venga vacia
        if (cliente.getCiudad() == null || cliente.getCiudad().trim().isEmpty()) {
            throw new RuntimeException("La ciudad del cliente es obligatoria");
        }

        // Valida que la direccion no venga vacia
        if (cliente.getDireccion() == null || cliente.getDireccion().trim().isEmpty()) {
            throw new RuntimeException("La direccion del cliente es obligatoria");
        }

        // Valida que el nombre del almacen no venga vacio
        if (cliente.getNombreAlmacen() == null || cliente.getNombreAlmacen().trim().isEmpty()) {
            throw new RuntimeException("El nombre del almacen es obligatorio");
        }

        // Valida que venga un vendedor asignado
        if (cliente.getVendedor() == null || cliente.getVendedor().getId() == null) {
            throw new RuntimeException("El vendedor es obligatorio");
        }

        // Valida que no exista otro cliente con la misma identificacion
        Optional<Cliente> existentePorIdentificacion = clienteRepository.findByIdentificacion(cliente.getIdentificacion().trim());

        // Si ya existe lanza un error
        if (existentePorIdentificacion.isPresent()) {
            throw new RuntimeException("Ya existe un cliente con la identificacion: " + cliente.getIdentificacion());
        }

        // Busca el vendedor en la base de datos
        Optional<Vendedor> vendedorResultado = vendedorRepository.findById(cliente.getVendedor().getId());

        // Si el vendedor no existe lanza un error
        if (vendedorResultado.isPresent() == false) {
            throw new RuntimeException("El vendedor no existe");
        }

        // Guarda el vendedor encontrado
        Vendedor vendedorEncontrado = vendedorResultado.get();

        // Limpia los espacios de los campos de texto
        cliente.setNombre(cliente.getNombre().trim());
        cliente.setIdentificacion(cliente.getIdentificacion().trim());
        cliente.setCiudad(cliente.getCiudad().trim());
        cliente.setDireccion(cliente.getDireccion().trim());
        cliente.setNombreAlmacen(cliente.getNombreAlmacen().trim());

        // Asigna el vendedor completo al cliente
        cliente.setVendedor(vendedorEncontrado);

        // Asigna activo por defecto en 1 si no viene en la peticion
        if (cliente.getActivo() == null) {
            cliente.setActivo((byte) 1);
        }

        // Guarda y retorna el nuevo cliente
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return clienteGuardado;
    }

    // Actualiza los datos de un cliente existente
    public Cliente actualizar(Long id, Cliente datos) {

        // Verifica que el cliente exista
        Cliente cliente = buscarPorId(id);

        // Valida que el nombre no venga vacio
        if (datos.getNombre() == null || datos.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del cliente es obligatorio");
        }

        // Valida que la identificacion no venga vacia
        if (datos.getIdentificacion() == null || datos.getIdentificacion().trim().isEmpty()) {
            throw new RuntimeException("La identificacion del cliente es obligatoria");
        }

        // Valida que la nueva identificacion no la tenga otro cliente diferente
        Optional<Cliente> existentePorIdentificacion = clienteRepository.findByIdentificacion(datos.getIdentificacion().trim());

        // Si existe pero es un registro diferente lanza un error
        if (existentePorIdentificacion.isPresent() && existentePorIdentificacion.get().getId().equals(id) == false) {
            throw new RuntimeException("Ya existe otro cliente con la identificacion: " + datos.getIdentificacion());
        }

        // Valida que venga un vendedor asignado
        if (datos.getVendedor() == null || datos.getVendedor().getId() == null) {
            throw new RuntimeException("El vendedor es obligatorio");
        }

        // Busca el vendedor en la base de datos
        Optional<Vendedor> vendedorResultado = vendedorRepository.findById(datos.getVendedor().getId());

        // Si el vendedor no existe lanza un error
        if (vendedorResultado.isPresent() == false) {
            throw new RuntimeException("El vendedor no existe");
        }

        // Guarda el vendedor encontrado
        Vendedor vendedorEncontrado = vendedorResultado.get();

        // Actualiza todos los campos
        cliente.setNombre(datos.getNombre().trim());
        cliente.setIdentificacion(datos.getIdentificacion().trim());
        cliente.setCiudad(datos.getCiudad().trim());
        cliente.setDireccion(datos.getDireccion().trim());
        cliente.setCorreo(datos.getCorreo());
        cliente.setTelefono(datos.getTelefono().trim());
        cliente.setNombreAlmacen(datos.getNombreAlmacen().trim());
        cliente.setVendedor(vendedorEncontrado);
        cliente.setInstagram(datos.getInstagram());
        cliente.setFacebook(datos.getFacebook());
        cliente.setWhatsapp(datos.getWhatsapp());

        // Guarda y retorna el cliente actualizado
        Cliente clienteActualizado = clienteRepository.save(cliente);
        return clienteActualizado;
    }

    // Activa un cliente
    public Cliente activar(Long id) {
        // Verifica que el cliente exista
        Cliente cliente = buscarPorId(id);

        // Cambia el estado a activo
        cliente.setActivo((byte) 1);

        // Guarda y retorna el cliente activado
        Cliente clienteActivado = clienteRepository.save(cliente);
        return clienteActivado;
    }

    // Desactiva un cliente
    public Cliente desactivar(Long id) {
        // Verifica que el cliente exista
        Cliente cliente = buscarPorId(id);

        // Cambia el estado a inactivo
        cliente.setActivo((byte) 0);

        // Guarda y retorna el cliente desactivado
        Cliente clienteDesactivado = clienteRepository.save(cliente);
        return clienteDesactivado;
    }

    // SOLO DESARROLLO - elimina fisicamente el registro
    public void eliminar(Long id) {
        // Verifica que el cliente exista antes de eliminar
        buscarPorId(id);
        clienteRepository.deleteById(id);
    }
}
