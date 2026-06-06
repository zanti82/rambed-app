import { useState, useEffect } from 'react';
import facturasApi from '../api/facturasApi';
import vendedoresApi from '../api/vendedoresApi';
import '../styles/tabla.css';

export default function Comisiones() {

    // Estado para la lista de comisiones
    const [comisiones, setComisiones] = useState([]);

    // Estado para la lista de vendedores
    const [vendedores, setVendedores] = useState([]);

    // Estado de carga
    const [cargando, setCargando] = useState(true);

    // Estado para el filtro de liquidada
    const [filtroLiquidada, setFiltroLiquidada] = useState('0');

    // Estado para el vendedor seleccionado en el filtro
    const [filtroVendedor, setFiltroVendedor] = useState('');

    // Estado para mensajes
    const [mensaje, setMensaje] = useState('');

    // Carga las comisiones y vendedores al montar
    useEffect(function() {
        cargarTodo();
    }, []);

    // Carga comisiones y vendedores en paralelo
    async function cargarTodo() {
        setCargando(true);
        try {
            const respComisiones = await facturasApi.listarComisiones(filtroLiquidada !== '' ? Number(filtroLiquidada) : null);
            const respVendedores = await vendedoresApi.listarActivos();
            setComisiones(respComisiones.data);
            setVendedores(respVendedores.data);
        } catch (err) {
            console.error('Error al cargar comisiones:', err);
        } finally {
            setCargando(false);
        }
    }

    // Recarga comisiones cuando cambia el filtro
    async function handleCambiarFiltro(valor) {
        setFiltroLiquidada(valor);
        setCargando(true);
        try {
            const respuesta = await facturasApi.listarComisiones(valor !== '' ? Number(valor) : null);
            setComisiones(respuesta.data);
        } catch (err) {
            console.error('Error al cargar comisiones:', err);
        } finally {
            setCargando(false);
        }
    }

    // Liquida todas las comisiones pendientes de un vendedor
    async function handleLiquidar(vendedorId, nombreVendedor) {
        // Pide confirmacion antes de liquidar
        const confirmar = window.confirm('¿Liquidar todas las comisiones pendientes de ' + nombreVendedor + '?');
        if (confirmar === false) {
            return;
        }

        try {
            await facturasApi.liquidarComisiones(vendedorId);
            setMensaje('Comisiones de ' + nombreVendedor + ' liquidadas correctamente');
            // Recarga la lista
            handleCambiarFiltro(filtroLiquidada);
            // Limpia el mensaje despues de 3 segundos
            setTimeout(function() { setMensaje(''); }, 3000);
        } catch (err) {
            console.error('Error al liquidar comisiones:', err);
        }
    }

    // Filtra comisiones por vendedor si hay uno seleccionado
    function comisionesFiltradas() {
        let resultado = comisiones;
        if (filtroVendedor !== '') {
            resultado = resultado.filter(function(c) {
                return c.nombreVendedor === filtroVendedor;
            });
        }
        return resultado;
    }

    // Calcula el total de comisiones visibles
    function calcularTotal() {
        let total = 0;
        const lista = comisionesFiltradas();
        for (let i = 0; i < lista.length; i++) {
            const comision = lista[i];
            // Suma comision de venta
            if (comision.montoComisionVenta !== null) {
                total = total + Number(comision.montoComisionVenta);
            }
            // Suma comision de pago si existe
            if (comision.montoComisionPago !== null) {
                total = total + Number(comision.montoComisionPago);
            }
        }
        return total;
    }

    // Formatea un numero como moneda colombiana
    function formatearMoneda(valor) {
        if (valor === null || valor === undefined) {
            return '—';
        }
        return '$' + Number(valor).toLocaleString('es-CO');
    }

    return (
        <div>
            {/* Titulo */}
            <div style={{ marginBottom: 24 }}>
                <h1 className="titulo-pagina">Comisiones</h1>
            </div>

            {/* Mensaje de exito */}
            {mensaje !== '' && (
                <div className="exito-mensaje" style={{ marginBottom: 16 }}>{mensaje}</div>
            )}

            {/* Botones de liquidar por vendedor */}
            <div style={{ display: 'flex', gap: 12, marginBottom: 24, flexWrap: 'wrap' }}>
                {vendedores.map(function(vendedor) {
                    return (
                        <button
                            key={vendedor.id}
                            className="btn-primario"
                            onClick={function() { handleLiquidar(vendedor.id, vendedor.nombre); }}>
                            Liquidar — {vendedor.nombre}
                        </button>
                    );
                })}
            </div>

            {/* Tabla de comisiones */}
            <div className="tabla-contenedor">

                {/* Toolbar con filtros */}
                <div className="tabla-toolbar">
                    <select
                        style={{ width: 'auto' }}
                        value={filtroLiquidada}
                        onChange={function(e) { handleCambiarFiltro(e.target.value); }}>
                        <option value="">Todas</option>
                        <option value="0">Pendientes</option>
                        <option value="1">Liquidadas</option>
                    </select>

                    <select
                        style={{ width: 'auto' }}
                        value={filtroVendedor}
                        onChange={function(e) { setFiltroVendedor(e.target.value); }}>
                        <option value="">Todos los vendedores</option>
                        {vendedores.map(function(vendedor) {
                            return (
                                <option key={vendedor.id} value={vendedor.nombre}>
                                    {vendedor.nombre}
                                </option>
                            );
                        })}
                    </select>
                </div>

                {/* Tabla */}
                <table className="tabla">
                    <thead>
                        <tr>
                            <th>Factura</th>
                            <th>Vendedor</th>
                            <th>Total factura</th>
                            <th>Com. venta</th>
                            <th>Com. pago</th>
                            <th>Total comisión</th>
                            <th>Estado</th>
                            <th>Liquidada</th>
                        </tr>
                    </thead>
                    <tbody>

                        {cargando && (
                            <tr>
                                <td colSpan={8} className="tabla-vacia">Cargando...</td>
                            </tr>
                        )}

                        {cargando === false && comisionesFiltradas().length === 0 && (
                            <tr>
                                <td colSpan={8} className="tabla-vacia">No hay comisiones</td>
                            </tr>
                        )}

                        {cargando === false && comisionesFiltradas().map(function(comision) {
                            // Calcula el total de la comision sumando venta y pago
                            const totalComision = Number(comision.montoComisionVenta || 0) + Number(comision.montoComisionPago || 0);

                            return (
                                <tr key={comision.id}>
                                    <td style={{ fontWeight: 500 }}>{comision.numeroFactura}</td>
                                    <td>{comision.nombreVendedor}</td>
                                    <td>{formatearMoneda(comision.totalFactura)}</td>
                                    <td>
                                        <div>{formatearMoneda(comision.montoComisionVenta)}</div>
                                        {comision.porcComisionVenta !== null && (
                                            <div style={{ fontSize: 11, color: 'var(--gris-texto)' }}>
                                                {comision.porcComisionVenta}%
                                            </div>
                                        )}
                                    </td>
                                    <td>
                                        <div>{formatearMoneda(comision.montoComisionPago)}</div>
                                        {comision.porcComisionPago !== null && (
                                            <div style={{ fontSize: 11, color: 'var(--gris-texto)' }}>
                                                {comision.porcComisionPago}%
                                            </div>
                                        )}
                                    </td>
                                    <td style={{ fontWeight: 600 }}>
                                        {formatearMoneda(totalComision)}
                                    </td>
                                    <td>
                                        {comision.liquidada === 0
                                            ? <span className="badge badge-pendiente">Pendiente</span>
                                            : <span className="badge badge-exito">Liquidada</span>
                                        }
                                    </td>
                                    <td style={{ fontSize: 12, color: 'var(--gris-texto)' }}>
                                        {comision.fechaLiquidacion !== null ? comision.fechaLiquidacion : '—'}
                                    </td>
                                </tr>
                            );
                        })}
                    </tbody>
                </table>

                {/* Total visible */}
                <div className="tabla-paginacion" style={{ justifyContent: 'space-between' }}>
                    <span>{comisionesFiltradas().length} comisión(es)</span>
                    <span style={{ fontWeight: 600 }}>
                        Total: {formatearMoneda(calcularTotal())}
                    </span>
                </div>
            </div>
        </div>
    );
}