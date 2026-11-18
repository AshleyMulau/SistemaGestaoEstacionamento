
package mz.academia.grupo8.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa um lugar físico no parque
 * de estacionamento.
 */
public class Lugar implements Serializable {

    private static final long serialVersionUID = 1L;

    // Identificador único do lugar
    private final int numero;

    // Zona ou localização do lugar
    private String localizacao;

    // Categoria do lugar
    private TipoLugar tipo;

    // Estado actual de ocupação
    private EstadoLugar estado;

    /**
     * Construtor da classe Lugar.
     */
    public Lugar(int numero, String localizacao, TipoLugar tipo) {

        if (numero <= 0) {
            throw new IllegalArgumentException(
                    "O número do lugar deve ser positivo."
            );
        }

        this.numero = numero;

        setLocalizacao(localizacao);
        setTipo(tipo);

        // Todo lugar começa livre
        this.estado = EstadoLugar.LIVRE;
    }

    // Getters

    public int getNumero() {
        return numero;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public TipoLugar getTipo() {
        return tipo;
    }

    public EstadoLugar getEstado() {
        return estado;
    }

    // Setters

    public void setLocalizacao(String localizacao) {

        if (localizacao == null || localizacao.isBlank()) {
            throw new IllegalArgumentException(
                    "A localização é obrigatória."
            );
        }

        this.localizacao = localizacao.trim();
    }

    public void setTipo(TipoLugar tipo) {

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "O tipo de lugar é obrigatório."
            );
        }

        this.tipo = tipo;
    }

    /**
     * Altera o estado de ocupação do lugar.
     */
    public void setEstado(EstadoLugar estado) {

        if (estado == null) {
            throw new IllegalArgumentException(
                    "O estado do lugar é obrigatório."
            );
        }

        this.estado = estado;
    }

    /**
     * Verifica se o lugar está livre.
     */
    public boolean estaLivre() {

        return estado == EstadoLugar.LIVRE;
    }

    /**
     * Compara lugares através do número de identificação.
     */
    @Override
    public boolean equals(Object objeto) {

        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Lugar outro)) {
            return false;
        }

        return numero == outro.numero;
    }

    /**
     * Gera o código hash do lugar.
     */
    @Override
    public int hashCode() {

        return Objects.hash(numero);
    }

    /**
     * Representação textual do lugar.
     */
    @Override
    public String toString() {

        return "Número: " + numero
                + "\nLocalização: " + localizacao
                + "\nTipo: " + tipo
                + "\nEstado: " + estado;
    }
}
