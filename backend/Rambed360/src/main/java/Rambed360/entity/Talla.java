package Rambed360.entity;

public enum Talla {

    // Tallas dama y junior
    T4("4"), T6("6"), T8("8"), T10("10"), T12("12"),
    T14("14"), T16("16"), T18("18"), T20("20"), T22("22"),

    // Tallas caballero jeans
    T28("28"), T29("29"), T30("30"), T31("31"), T32("32"),
    T33("33"), T34("34"), T36("36"), T38("38"), T40("40"),

    // Tallas camisas
    XS("XS"), S("S"), M("M"), L("L"), XL("XL"), XXL("XXL");

    // Valor que se guarda en la base de datos
    private final String valor;

    // Constructor del enum con el valor de la talla
    Talla(String valor) {
        this.valor = valor;
    }

    // Retorna el valor de la talla
    public String getValor() {
        return valor;
    }

}
