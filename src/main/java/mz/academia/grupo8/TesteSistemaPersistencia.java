
package mz.academia.grupo8;

import mz.academia.grupo8.model.*;
import mz.academia.grupo8.service.*;
import mz.academia.grupo8.persistence.*;

public class TesteSistemaPersistencia {

    public static void main(String[] args) {

        // Criar os serviços
        UtilizadorService utilizadorService = new UtilizadorService();
        VeiculoService veiculoService = new VeiculoService();
        LugarService lugarService = new LugarService();

        EstacionamentoService estacionamentoService =
                new EstacionamentoService(
                        utilizadorService,
                        veiculoService,
                        lugarService
                );
        veiculoService.definirEstacionamentoService(
                estacionamentoService
        );

        veiculoService.definirUtilizadorService(utilizadorService);

        utilizadorService.definirServicosRelacionados(
                veiculoService,
                estacionamentoService
        );

        // Registar utilizador
        Estudante estudante = new Estudante(
                "EST100",
                "Maria Alberto",
                "841111111",
                "maria@email.com",
                "Engenharia Informática",
                3
        );

        utilizadorService.registar(estudante);

        // Registar veículo
        Veiculo veiculo = new Veiculo(
                "ABC-123-MP",
                "Toyota",
                "Corolla",
                "Branco",
                estudante
        );

        veiculoService.registar(veiculo);

        // Registar lugar
        Lugar lugar = new Lugar(
                10,
                "Bloco A",
                TipoLugar.ESTUDANTE
        );

        lugarService.registar(lugar);

        // Registar entrada
        estacionamentoService.registarEntrada(
                "REG100",
                "ABC-123-MP",
                "EST100",
                10
        );

        // Guardar o sistema
        SistemaPersistencia persistencia =
                new SistemaPersistencia();

        System.out.println("=== GUARDAR SISTEMA ===");

        persistencia.guardar(
                utilizadorService,
                veiculoService,
                lugarService,
                estacionamentoService
        );

        // Verificar ficheiro
        System.out.println("\n=== VERIFICAR FICHEIRO ===");

        System.out.println(
                "Ficheiro existe: " + persistencia.existeDados()
        );

        // Carregar dados para verificar
        System.out.println("\n=== VERIFICAR DADOS GUARDADOS ===");

        DadosSistema dados = persistencia.carregar();

        System.out.println(
                "Utilizadores: " + dados.getUtilizadores().size()
        );

        System.out.println(
                "Veículos: " + dados.getVeiculos().size()
        );

        System.out.println(
                "Lugares: " + dados.getLugares().size()
        );

        System.out.println(
                "Registos de estacionamento: "
                        + dados.getEstacionamentos().size()
        );

        System.out.println(
                "Estado do lugar: "
                        + dados.getLugares().get(0).getEstado()
        );

        System.out.println(
                "Estacionamento activo: "
                        + dados.getEstacionamentos().get(0).estaActivo()
        );

        // ======================================
// SIMULAR REINÍCIO DA APLICAÇÃO
// ======================================

        System.out.println("\n=== SIMULAR REINÍCIO ===");

// Criar novos serviços vazios
        UtilizadorService novoUtilizadorService =
                new UtilizadorService();

        VeiculoService novoVeiculoService =
                new VeiculoService();

        LugarService novoLugarService =
                new LugarService();

        EstacionamentoService novoEstacionamentoService =
                new EstacionamentoService(
                        novoUtilizadorService,
                        novoVeiculoService,
                        novoLugarService
                );
        novoVeiculoService.definirEstacionamentoService(
                novoEstacionamentoService
        );

        novoVeiculoService.definirUtilizadorService(
                novoUtilizadorService
        );

        novoUtilizadorService.definirServicosRelacionados(
                novoVeiculoService,
                novoEstacionamentoService
        );

// Carregar os dados guardados
        System.out.println("\n=== CARREGAR SISTEMA ===");

        boolean carregamento = persistencia.carregar(
                novoUtilizadorService,
                novoVeiculoService,
                novoLugarService,
                novoEstacionamentoService
        );

        System.out.println("Carregamento realizado: " + carregamento);

// Verificar os dados recuperados
        System.out.println("\n=== DADOS RECUPERADOS ===");

        System.out.println(
                "Utilizadores: " + novoUtilizadorService.quantidade()
        );

        System.out.println(
                "Veículos: " + novoVeiculoService.quantidade()
        );

        System.out.println(
                "Lugares: " + novoLugarService.quantidade()
        );

        System.out.println(
                "Estacionamentos activos: "
                        + novoEstacionamentoService.contarActivos()
        );

        System.out.println(
                "Lugares ocupados: "
                        + novoLugarService.contarOcupados()
        );

        System.out.println("\n=== VERIFICAR RELAÇÕES ===");

        Estacionamento estacionamentoRecuperado =
                novoEstacionamentoService.listarActivos().get(0);

        System.out.println(
                "Proprietário do estacionamento: "
                        + estacionamentoRecuperado.getProprietario().getNome()
        );

        System.out.println(
                "Proprietário do veículo: "
                        + novoVeiculoService
                        .procurarPorMatricula("ABC-123-MP")
                        .getProprietario().getNome()
        );

        System.out.println(
                "Número do lugar: "
                        + estacionamentoRecuperado.getLugar().getNumero()
        );
    }
}
