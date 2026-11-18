
package mz.academia.grupo8.service;

import mz.academia.grupo8.model.DocenteFuncionario;
import mz.academia.grupo8.model.Estudante;
import mz.academia.grupo8.model.Utilizador;
import mz.academia.grupo8.model.Veiculo;
import mz.academia.grupo8.tad.MinhaHashSet;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável pela gestão dos veículos.
 */
public class VeiculoService {

    private final MinhaHashSet<Veiculo> veiculos;
    private EstacionamentoService estacionamentoService;
    private UtilizadorService utilizadorService;

    public VeiculoService() {
        veiculos = new MinhaHashSet<>();
    }


    public boolean registar(Veiculo veiculo) {

        // 1. Verificar se o veículo é nulo
        if (veiculo == null) {
            throw new IllegalArgumentException(
                    "O veículo não pode ser nulo."
            );
        }

        // 2. Verificar se o proprietário existe
        if (utilizadorService != null) {

            Utilizador proprietario = utilizadorService.procurarPorCodigo(
                    veiculo.getProprietario().getCodigo()
            );

            if (proprietario == null ||
                    !proprietario.equals(veiculo.getProprietario())) {
                return false;
            }
        }

        // 3. Verificar se a matrícula já está registada
        if (procurarPorMatricula(veiculo.getMatricula()) != null) {
            return false;
        }

        // 4. Adicionar o veículo à MinhaHashSet
        return veiculos.adicionar(veiculo);
    }


    /**
     * Liga o serviço de veículos ao serviço de estacionamento.
     */
    public void definirEstacionamentoService(
            EstacionamentoService estacionamentoService) {

        this.estacionamentoService = estacionamentoService;
    }

    /**
     * Liga o serviço de veículos ao serviço de utilizadores.
     */
    public void definirUtilizadorService(
            UtilizadorService utilizadorService) {

        this.utilizadorService = utilizadorService;
    }

    /**
     * Carrega os veículos recuperados do ficheiro
     * para a estrutura MinhaHashSet.
     */
    public void carregarDados(List<Veiculo> dados) {

        veiculos.limpar();

        if (dados == null) {
            return;
        }

        for (Veiculo veiculo : dados) {
            veiculos.adicionar(veiculo);
        }
    }

    /**
     * Devolve uma cópia da lista de veículos.
     */
    public List<Veiculo> obterDados() {
        return new ArrayList<>(veiculos.listar());
    }

    // Procurar veículo pela matrícula
    public Veiculo procurarPorMatricula(String matricula) {
        if (matricula == null || matricula.isBlank()) {
            return null;
        }

        String matriculaNormalizada =
                matricula.trim().toUpperCase(Locale.ROOT);

        for (Veiculo veiculo : veiculos.listar()) {
            if (veiculo.getMatricula().equalsIgnoreCase(
                    matriculaNormalizada)) {
                return veiculo;
            }
        }

        return null;
    }

    // Actualizar os dados de um veículo
    public boolean actualizar(String matricula,
                              String marca,
                              String modelo,
                              String cor,
                              Utilizador proprietario) {

        Veiculo veiculo = procurarPorMatricula(matricula);

        if (veiculo == null || proprietario == null) {
            return false;
        }

        // Verificar se o novo proprietário está registado
        if (utilizadorService != null) {

            Utilizador proprietarioRegistado =
                    utilizadorService.procurarPorCodigo(
                            proprietario.getCodigo()
                    );

            if (proprietarioRegistado == null
                    || !proprietarioRegistado.equals(proprietario)) {
                return false;
            }

            // Utilizar a referência do proprietário registado
            proprietario = proprietarioRegistado;
        }
        // Não permitir alterar o proprietário
        // enquanto o veículo estiver estacionado
        boolean estaEstacionado =
                estacionamentoService != null &&
                        estacionamentoService.veiculoEstaEstacionado(matricula);

        if (estaEstacionado &&
                !veiculo.getProprietario().equals(proprietario)) {

            return false;
        }

        veiculo.setMarca(marca);
        veiculo.setModelo(modelo);
        veiculo.setCor(cor);
        veiculo.setProprietario(proprietario);

        return true;
    }

    // Remover veículo pela matrícula
    public boolean remover(String matricula) {

        Veiculo veiculo = procurarPorMatricula(matricula);

        if (veiculo == null) {
            return false;
        }

        // Verificar se o veículo está estacionado
        if (estacionamentoService != null &&
                estacionamentoService.veiculoEstaEstacionado(matricula)) {

            return false;
        }

        return veiculos.remover(veiculo);
    }
    // Listar todos os veículos por matrícula crescente
    public List<Veiculo> listarTodos() {
        return veiculos.listar()
                .stream()
                .sorted(Comparator.comparing(
                        v -> v.getMatricula().toUpperCase(Locale.ROOT)
                ))
                .collect(Collectors.toList());
    }

    // Listar veículos pertencentes a estudantes
    public List<Veiculo> listarVeiculosEstudantes() {
        return veiculos.listar()
                .stream()
                .filter(v -> v.getProprietario() instanceof Estudante)
                .sorted(Comparator.comparing(
                        v -> v.getMatricula().toUpperCase(Locale.ROOT)
                ))
                .collect(Collectors.toList());
    }

    // Listar veículos pertencentes a docentes/funcionários
    public List<Veiculo> listarVeiculosDocentes() {
        return veiculos.listar()
                .stream()
                .filter(v -> v.getProprietario()
                        instanceof DocenteFuncionario)
                .sorted(Comparator.comparing(
                        v -> v.getMatricula().toUpperCase(Locale.ROOT)
                ))
                .collect(Collectors.toList());
    }

    // Quantidade total de veículos
    public int quantidade() {
        return veiculos.tamanho();
    }

    // Verificar se não existem veículos
    public boolean estaVazio() {
        return veiculos.estaVazio();
    }
}
