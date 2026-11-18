
package mz.academia.grupo8;

import mz.academia.grupo8.model.*;
import mz.academia.grupo8.service.*;

public class TesteValidacoesEstacionamento {

    public static void main(String[] args) {

        // 1. Criar os serviços
        UtilizadorService utilizadorService = new UtilizadorService();
        VeiculoService veiculoService = new VeiculoService();
        LugarService lugarService = new LugarService();

        EstacionamentoService estacionamentoService =
                new EstacionamentoService(
                        utilizadorService,
                        veiculoService,
                        lugarService
                );

        // 2. Ligar os serviços
        veiculoService.definirEstacionamentoService(estacionamentoService);

        utilizadorService.definirServicosRelacionados(
                veiculoService,
                estacionamentoService
        );
        veiculoService.definirUtilizadorService(utilizadorService);

        // 3. Criar dois estudantes e um docente
        Estudante maria = new Estudante(
                "U001", "Maria Alberto",
                "841234567", "maria@email.com",
                "Informática", 3
        );

        Estudante ana = new Estudante(
                "U002", "Ana Manuel",
                "841234568", "ana@email.com",
                "Informática", 2
        );

        DocenteFuncionario docente = new DocenteFuncionario(
                "U003", "Carlos Manuel",
                "841234569", "carlos@email.com",
                "Informática", "Docente"
        );

        // 4. Registar utilizadores
        utilizadorService.registar(maria);
        utilizadorService.registar(ana);
        utilizadorService.registar(docente);

        // 5. Criar três veículos
        Veiculo carroMaria = new Veiculo(
                "ABC-123-MC", "Toyota",
                "Corolla", "Branco", maria
        );

        Veiculo carroAna = new Veiculo(
                "DEF-456-MC", "Honda",
                "Civic", "Preto", ana
        );

        Veiculo carroDocente = new Veiculo(
                "GHI-789-MC", "Mazda",
                "Demio", "Azul", docente
        );

        veiculoService.registar(carroMaria);
        veiculoService.registar(carroAna);
        veiculoService.registar(carroDocente);

        // 6. Criar dois lugares
        Lugar lugarEstudante = new Lugar(
                1, "Bloco A", TipoLugar.ESTUDANTE
        );

        Lugar lugarDocente = new Lugar(
                2, "Bloco B", TipoLugar.DOCENTE_FUNCIONARIO
        );

        lugarService.registar(lugarEstudante);
        lugarService.registar(lugarDocente);

        // TESTE 1: Entrada válida da Maria
        System.out.println("Teste 1 - Entrada válida: "
                + estacionamentoService.registarEntrada(
                "REG001", "ABC-123-MC", "U001", 1
        ));

        // TESTE 2: Ana tenta ocupar o lugar da Maria
        System.out.println("Teste 2 - Lugar ocupado: "
                + estacionamentoService.registarEntrada(
                "REG002", "DEF-456-MC", "U002", 1
        ));

        // TESTE 3: Ana tenta ocupar lugar de docente
        System.out.println("Teste 3 - Tipo incompatível: "
                + estacionamentoService.registarEntrada(
                "REG003", "DEF-456-MC", "U002", 2
        ));

        // TESTE 4: Maria tenta entrar novamente
        System.out.println("Teste 4 - Veículo já estacionado: "
                + estacionamentoService.registarEntrada(
                "REG004", "ABC-123-MC", "U001", 2
        ));

        // TESTE 5: Docente ocupa o lugar correcto
        System.out.println("Teste 5 - Lugar de docente: "
                + estacionamentoService.registarEntrada(
                "REG005", "GHI-789-MC", "U003", 2
        ));

        // Estado final
        System.out.println("\n=== ESTADO FINAL ===");

        System.out.println("Veículos estacionados: "
                + estacionamentoService.contarActivos());

        System.out.println("Lugares livres: "
                + estacionamentoService.contarLugaresLivres());

        System.out.println("Lugares ocupados: "
                + estacionamentoService.contarLugaresOcupados());
    }
}
