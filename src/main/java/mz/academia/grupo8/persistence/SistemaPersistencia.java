
package mz.academia.grupo8.persistence;

import mz.academia.grupo8.service.UtilizadorService;
import mz.academia.grupo8.service.VeiculoService;
import mz.academia.grupo8.service.LugarService;
import mz.academia.grupo8.service.EstacionamentoService;

/**
 * Responsável por guardar os dados completos do sistema.
 */
public class SistemaPersistencia {

    private final FicheiroService ficheiroService;

    private static final String CAMINHO =
            "dados_estacionamento.dat";

    public SistemaPersistencia() {
        this.ficheiroService = new FicheiroService();
    }

    /**
     * Reúne os dados dos serviços e guarda-os num ficheiro.
     */
    public void guardar(
            UtilizadorService utilizadorService,
            VeiculoService veiculoService,
            LugarService lugarService,
            EstacionamentoService estacionamentoService) {

        DadosSistema dados = new DadosSistema(
                utilizadorService.obterDados(),
                veiculoService.obterDados(),
                lugarService.obterDados(),
                estacionamentoService.obterDados()
        );

        ficheiroService.guardar(CAMINHO, dados);

        System.out.println("Dados do sistema guardados com sucesso!");
    }

    /**
     * Carrega os dados guardados no ficheiro.
     */
    public DadosSistema carregar() {

        Object objeto = ficheiroService.carregar(CAMINHO);

        if (objeto == null) {
            return null;
        }

        if (!(objeto instanceof DadosSistema)) {
            throw new IllegalStateException(
                    "O ficheiro não contém dados válidos do sistema."
            );
        }

        return (DadosSistema) objeto;
    }

    /**
     * Recupera os dados do ficheiro e distribui-os
     * pelos respectivos serviços.
     */
    public boolean carregar(
            UtilizadorService utilizadorService,
            VeiculoService veiculoService,
            LugarService lugarService,
            EstacionamentoService estacionamentoService) {

        DadosSistema dados = carregar();

        if (dados == null) {
            System.out.println("Não existem dados guardados.");
            return false;
        }

        // Recuperar primeiro os utilizadores
        utilizadorService.carregarDados(
                dados.getUtilizadores()
        );

        // Recuperar os veículos
        veiculoService.carregarDados(
                dados.getVeiculos()
        );

        // Recuperar os lugares
        lugarService.carregarDados(
                dados.getLugares()
        );

        // Recuperar o histórico de estacionamento
        estacionamentoService.carregarDados(
                dados.getEstacionamentos()
        );

        System.out.println("Dados do sistema carregados com sucesso!");

        return true;
    }

    /**
     * Verifica se já existe um ficheiro de dados.
     */
    public boolean existeDados() {
        return ficheiroService.existe(CAMINHO);
    }
}
