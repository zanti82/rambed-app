package Rambed360.dto.response;


// Datos de una referencia en el top 20
public class TopReferenciaResponse {

    // Marca del producto
    private String marca;

    // Referencia del producto
    private String referencia;

    // Total de unidades vendidas
    private Long totalUnidades;

    public TopReferenciaResponse() {}

    public TopReferenciaResponse(String marca, String referencia, Long totalUnidades) {
        this.marca = marca;
        this.referencia = referencia;
        this.totalUnidades = totalUnidades;
    }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public Long getTotalUnidades() { return totalUnidades; }
    public void setTotalUnidades(Long totalUnidades) { this.totalUnidades = totalUnidades; }
}
