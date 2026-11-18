
package mz.academia.grupo8.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa um registo de estacionamento de um veículo.
 *
 * Guarda os dados de entrada e saída do veículo no parque.
 */
public class Estacionamento implements Serializable {

    private static final long serialVersionUID = 1L;

    // Identificador único do registo
    private final String codigo;

    // Dados do veículo, proprietário e lugar
    private final Veiculo veiculo;
    private final Utilizador proprietario;
    private final Lugar lugar;

    // Datas e horas de entrada e saída
    private final LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraSaida;

    /**
     * Construtor de um novo registo de estacionamento.
     */
    public Estacionamento(String codigo,
                          Veiculo veiculo,
                          Utilizador proprietario,
                          Lugar lugar,
                          LocalDateTime dataHoraEntrada) {

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "O código do registo é obrigatório."
            );
        }

        if (veiculo == null || proprietario == null || lugar == null) {
            throw new IllegalArgumentException(
                    "O veículo, proprietário e lugar são obrigatórios."
            );
        }

        if (dataHoraEntrada == null) {
            throw new IllegalArgumentException(
                    "A data e hora de entrada são obrigatórias."
            );
        }

        if (!veiculo.getProprietario().equals(proprietario)) {
            throw new IllegalArgumentException(
                    "O proprietário não corresponde ao veículo."
            );
        }

        this.codigo = codigo.trim();
        this.veiculo = veiculo;
        this.proprietario = proprietario;
        this.lugar = lugar;
        this.dataHoraEntrada = dataHoraEntrada;
        this.dataHoraSaida = null;
    }

    // Getters

    public String getCodigo() {
        return codigo;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public Utilizador getProprietario() {
        return proprietario;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public LocalDateTime getDataHoraEntrada() {
        return dataHoraEntrada;
    }

    public LocalDateTime getDataHoraSaida() {
        return dataHoraSaida;
    }

    /**
     * Regista a saída do veículo.
     */
    public void registarSaida(LocalDateTime dataHoraSaida) {

        if (this.dataHoraSaida != null) {
            throw new IllegalStateException(
                    "A saída deste veículo já foi registada."
            );
        }

        if (dataHoraSaida == null) {
            throw new IllegalArgumentException(
                    "A data e hora de saída são obrigatórias."
            );
        }

        if (dataHoraSaida.isBefore(dataHoraEntrada)) {
            throw new IllegalArgumentException(
                    "A saída não pode ocorrer antes da entrada."
            );
        }

        this.dataHoraSaida = dataHoraSaida;
    }

    /**
     * Verifica se o veículo ainda está estacionado.
     */
    public boolean estaActivo() {
        return dataHoraSaida == null;
    }

    /**
     * Compara registos através do seu código.
     */
    @Override
    public boolean equals(Object objeto) {

        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Estacionamento outro)) {
            return false;
        }

        return codigo.equals(outro.codigo);
    }

    /**
     * Gera o código hash do registo.
     */
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    /**
     * Representação textual do registo.
     */
    @Override
    public String toString() {

        return "Código: " + codigo
                + "\nMatrícula: " + veiculo.getMatricula()
                + "\nProprietário: " + proprietario.getNome()
                + "\nLugar: " + lugar.getNumero()
                + "\nEntrada: " + dataHoraEntrada
                + "\nSaída: "
                + (dataHoraSaida == null
                ? "Ainda estacionado"
                : dataHoraSaida);
    }
}
