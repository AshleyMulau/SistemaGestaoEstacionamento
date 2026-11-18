
package mz.academia.grupo8.persistence;

import mz.academia.grupo8.model.Utilizador;
import mz.academia.grupo8.model.Veiculo;
import mz.academia.grupo8.model.Lugar;
import mz.academia.grupo8.model.Estacionamento;

import java.io.Serializable;
import java.util.List;

/**
 * Classe responsável por reunir todos os dados
 * do sistema de gestão de estacionamento.
 *
 * Implementa Serializable para permitir guardar
 * todos os dados num único ficheiro.
 */
public class DadosSistema implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Utilizador> utilizadores;
    private List<Veiculo> veiculos;
    private List<Lugar> lugares;
    private List<Estacionamento> estacionamentos;

    public DadosSistema(
            List<Utilizador> utilizadores,
            List<Veiculo> veiculos,
            List<Lugar> lugares,
            List<Estacionamento> estacionamentos) {

        this.utilizadores = utilizadores;
        this.veiculos = veiculos;
        this.lugares = lugares;
        this.estacionamentos = estacionamentos;
    }

    public List<Utilizador> getUtilizadores() {
        return utilizadores;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public List<Lugar> getLugares() {
        return lugares;
    }

    public List<Estacionamento> getEstacionamentos() {
        return estacionamentos;
    }
}
