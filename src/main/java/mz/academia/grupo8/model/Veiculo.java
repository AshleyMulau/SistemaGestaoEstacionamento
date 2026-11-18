
package mz.academia.grupo8.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa um veículo registado no sistema
 * de gestão do parque de estacionamento.
 */
public class Veiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos do veículo
    private final String matricula;
    private String marca;
    private String modelo;
    private String cor;
    private Utilizador proprietario;

    /**
     * Construtor da classe Veiculo.
     */
    public Veiculo(String matricula, String marca,
                   String modelo, String cor,
                   Utilizador proprietario) {

        if (matricula == null || matricula.isBlank()) {
            throw new IllegalArgumentException(
                    "A matrícula é obrigatória."
            );
        }

        this.matricula = matricula.trim().toUpperCase();

        setMarca(marca);
        setModelo(modelo);
        setCor(cor);
        setProprietario(proprietario);
    }

    // Getters

    public String getMatricula() {
        return matricula;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getCor() {
        return cor;
    }

    public Utilizador getProprietario() {
        return proprietario;
    }

    // Setters

    public void setMarca(String marca) {

        if (marca == null || marca.isBlank()) {
            throw new IllegalArgumentException(
                    "A marca é obrigatória."
            );
        }

        this.marca = marca.trim();
    }

    public void setModelo(String modelo) {

        if (modelo == null || modelo.isBlank()) {
            throw new IllegalArgumentException(
                    "O modelo é obrigatório."
            );
        }

        this.modelo = modelo.trim();
    }

    public void setCor(String cor) {

        if (cor == null || cor.isBlank()) {
            throw new IllegalArgumentException(
                    "A cor é obrigatória."
            );
        }

        this.cor = cor.trim();
    }

    public void setProprietario(Utilizador proprietario) {

        if (proprietario == null) {
            throw new IllegalArgumentException(
                    "O proprietário é obrigatório."
            );
        }

        this.proprietario = proprietario;
    }

    /**
     * Compara dois veículos através da matrícula.
     */
    @Override
    public boolean equals(Object objeto) {

        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Veiculo outro)) {
            return false;
        }

        return matricula.equals(outro.matricula);
    }

    /**
     * Gera o código hash do veículo com base na matrícula.
     */
    @Override
    public int hashCode() {

        return Objects.hash(matricula);
    }

    /**
     * Representação textual do veículo.
     */
    @Override
    public String toString() {

        return "Matrícula: " + matricula
                + "\nMarca: " + marca
                + "\nModelo: " + modelo
                + "\nCor: " + cor
                + "\nProprietário: " + proprietario.getNome();
    }
}
