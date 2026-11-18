
package mz.academia.grupo8;

import mz.academia.grupo8.model.*;
import mz.academia.grupo8.service.*;

public class TesteEstacionamentoService {

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

        // =====================================
        // 1. REGISTAR UTILIZADORES
        // =====================================

        Estudante estudante = new Estudante(
                "EST001",
                "Ana Manuel",
                "841234567",
                "ana@email.com",
                "Engenharia Informática",
                3
        );

        DocenteFuncionario docente = new DocenteFuncionario(
                "DOC001",
                "Carlos Alberto",
                "821234567",
                "carlos@email.com",
                "Departamento de Informática",
                "Docente"
        );

        utilizadorService.registar(estudante);
        utilizadorService.registar(docente);

        // =====================================
        // 2. REGISTAR VEÍCULOS
        // =====================================

        Veiculo veiculo1 = new Veiculo(
                "ABC-123-MC",
                "Toyota",
                "Corolla",
                "Branco",
                estudante
        );

        Veiculo veiculo2 = new Veiculo(
                "XYZ-456-MC",
                "Honda",
                "Civic",
                "Preto",
                docente
        );

        veiculoService.registar(veiculo1);
        veiculoService.registar(veiculo2);

        // =====================================
        // 3. REGISTAR LUGARES
        // =====================================

        Lugar lugar1 = new Lugar(
                1,
                "Bloco A",
                TipoLugar.ESTUDANTE
        );

        Lugar lugar2 = new Lugar(
                2,
                "Bloco B",
                TipoLugar.DOCENTE_FUNCIONARIO
        );

        lugarService.registar(lugar1);
        lugarService.registar(lugar2);

        // =====================================
        // 4. TESTAR ENTRADAS
        // =====================================

        System.out.println("=== REGISTO DE ENTRADAS ===");

        System.out.println(
                "Entrada estudante: "
                        + estacionamentoService.registarEntrada(
                        "REG001",
                        "ABC-123-MC",
                        "EST001",
                        1
                )
        );

        System.out.println(
                "Entrada docente: "
                        + estacionamentoService.registarEntrada(
                        "REG002",
                        "XYZ-456-MC",
                        "DOC001",
                        2
                )
        );

        // =====================================
        // 5. TESTAR VALIDAÇÕES
        // =====================================

        System.out.println("\n=== TESTE DE VALIDAÇÕES ===");

        System.out.println(
                "Veículo já estacionado: "
                        + estacionamentoService.registarEntrada(
                        "REG003",
                        "ABC-123-MC",
                        "EST001",
                        2
                )
        );

        System.out.println(
                "Lugar ocupado: "
                        + estacionamentoService.registarEntrada(
                        "REG003",
                        "XYZ-456-MC",
                        "DOC001",
                        2
                )
        );

        System.out.println(
                "Tipo de lugar incompatível: "
                        + estacionamentoService.registarEntrada(
                        "REG003",
                        "XYZ-456-MC",
                        "DOC001",
                        1
                )
        );

        // =====================================
        // 6. CONSULTAR ESTADO ACTUAL
        // =====================================

        System.out.println("\n=== ESTACIONAMENTOS ACTIVOS ===");

        estacionamentoService.listarActivos()
                .forEach(System.out::println);

        System.out.println(
                "Veículos estacionados: "
                        + estacionamentoService.contarActivos()
        );

        System.out.println(
                "Lugares livres: "
                        + estacionamentoService.contarLugaresLivres()
        );

        System.out.println(
                "Lugares ocupados: "
                        + estacionamentoService.contarLugaresOcupados()
        );

        // =====================================
        // 7. REGISTAR SAÍDA
        // =====================================

        System.out.println("\n=== REGISTO DE SAÍDA ===");

        System.out.println(
                "Saída estudante: "
                        + estacionamentoService.registarSaida("ABC-123-MC")
        );

        System.out.println(
                "Segunda saída do mesmo veículo: "
                        + estacionamentoService.registarSaida("ABC-123-MC")
        );

        // =====================================
        // 8. CONSULTAR HISTÓRICO
        // =====================================

        System.out.println("\n=== HISTÓRICO COMPLETO ===");

        estacionamentoService.listarHistorico()
                .forEach(System.out::println);

        System.out.println(
                "Total de registos: "
                        + estacionamentoService.quantidadeRegistos()
        );

        // =====================================
        // 9. CONSULTAR POR UTILIZADOR
        // =====================================

        System.out.println("\n=== HISTÓRICO DO ESTUDANTE ===");

        estacionamentoService.consultarPorUtilizador("EST001")
                .forEach(System.out::println);

        // =====================================
        // 10. ESTADO FINAL
        // =====================================

        System.out.println("\n=== ESTADO FINAL DO PARQUE ===");

        System.out.println(
                "Veículos estacionados: "
                        + estacionamentoService.contarActivos()
        );

        System.out.println(
                "Lugares livres: "
                        + estacionamentoService.contarLugaresLivres()
        );

        System.out.println(
                "Lugares ocupados: "
                        + estacionamentoService.contarLugaresOcupados()
        );
    }
}
