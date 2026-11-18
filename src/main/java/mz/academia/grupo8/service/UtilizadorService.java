
package mz.academia.grupo8.service;

import mz.academia.grupo8.model.Utilizador;
import mz.academia.grupo8.tad.MinhaHashSet;
import java.util.ArrayList;
import java.util.List;
import mz.academia.grupo8.model.Veiculo;


/**
 * Serviço responsável pela gestão dos utilizadores
 * do parque de estacionamento.
 */
public class UtilizadorService {

    // Estrutura de dados que armazena os utilizadores
    private final MinhaHashSet<Utilizador> utilizadores;
    private VeiculoService veiculoService;
    private EstacionamentoService estacionamentoService;

    /**
     * Construtor do serviço.
     */
    public UtilizadorService() {

        utilizadores = new MinhaHashSet<>();
    }

    /**
     * Carrega os utilizadores recuperados do ficheiro
     * para a estrutura MinhaHashSet.
     */
    public void carregarDados(List<Utilizador> dados) {

        utilizadores.limpar();

        if (dados == null) {
            return;
        }

        for (Utilizador utilizador : dados) {
            utilizadores.adicionar(utilizador);
        }
    }

    /**
     * Liga o serviço de utilizadores aos outros serviços.
     */
    public void definirServicosRelacionados(
            VeiculoService veiculoService,
            EstacionamentoService estacionamentoService) {

        this.veiculoService = veiculoService;
        this.estacionamentoService = estacionamentoService;
    }

    /**
     * Regista um novo utilizador.
     *
     * @param utilizador utilizador a registar
     * @return true se foi registado com sucesso
     */
    public boolean registar(Utilizador utilizador) {

        if (utilizador == null) {
            throw new IllegalArgumentException(
                    "O utilizador não pode ser nulo."
            );
        }

        // Verifica se já existe um código igual
        if (procurarPorCodigo(utilizador.getCodigo()) != null) {
            return false;
        }

        return utilizadores.adicionar(utilizador);
    }

    /**
     * Devolve uma cópia da lista de utilizadores.
     */
    public List<Utilizador> obterDados() {
        return new ArrayList<>(utilizadores.listar());
    }

    /**
     * Procura um utilizador através do seu código.
     *
     * @param codigo código do utilizador
     * @return utilizador encontrado ou null
     */
    public Utilizador procurarPorCodigo(String codigo) {

        if (codigo == null || codigo.isBlank()) {
            return null;
        }

        for (Utilizador utilizador : utilizadores.listar()) {

            if (utilizador.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return utilizador;
            }
        }

        return null;
    }

    /**
     * Remove um utilizador através do seu código.
     *
     * @param codigo código do utilizador
     * @return true se foi removido
     */
    /**
     * Remove um utilizador, desde que não tenha
     * veículos ou estacionamentos activos associados.
     */
    public boolean remover(String codigo) {

        Utilizador utilizador = procurarPorCodigo(codigo);

        if (utilizador == null) {
            return false;
        }

        // Impedir remoção se possuir veículos registados
        if (veiculoService != null) {

            for (Utilizador proprietario :
                    veiculoService.listarTodos().stream()
                            .map(Veiculo::getProprietario)
                            .toList()) {

                if (proprietario.equals(utilizador)) {
                    return false;
                }
            }
        }

        // Impedir remoção se tiver estacionamento activo
        if (estacionamentoService != null &&
                estacionamentoService.utilizadorEstaEstacionado(codigo)) {

            return false;
        }

        return utilizadores.remover(utilizador);
    }

    /**
     * Actualiza os dados comuns de um utilizador.
     */
    public boolean actualizar(String codigo,
                              String nome,
                              String telefone,
                              String email) {

        Utilizador utilizador = procurarPorCodigo(codigo);

        if (utilizador == null) {
            return false;
        }

        utilizador.setNome(nome);
        utilizador.setTelefone(telefone);
        utilizador.setEmail(email);

        return true;
    }

    /**
     * Devolve todos os utilizadores registados.
     */
    public List<Utilizador> listarTodos() {

        return utilizadores.listar();
    }

    /**
     * Devolve a quantidade de utilizadores registados.
     */
    public int quantidade() {

        return utilizadores.tamanho();
    }

    /**
     * Verifica se não existem utilizadores registados.
     */
    public boolean estaVazio() {

        return utilizadores.estaVazio();
    }
}
