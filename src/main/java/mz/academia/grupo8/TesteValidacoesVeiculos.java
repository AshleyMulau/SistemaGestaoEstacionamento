
package mz.academia.grupo8;

import mz.academia.grupo8.model.*;
import mz.academia.grupo8.service.*;

public class TesteValidacoesVeiculos {

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

        // 3. Criar utilizadores
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

        Estudante joao = new Estudante(
                "U003", "João Manuel",
                "841234569", "joao@email.com",
                "Informática", 1
        );

        // 4. Registar apenas Maria e Ana
        utilizadorService.registar(maria);
        utilizadorService.registar(ana);

        // João não será registado.

        // 5. Criar veículos
        Veiculo carroMaria = new Veiculo(
                "ABC-123-MC", "Toyota",
                "Corolla", "Branco", maria
        );

        Veiculo carroJoao = new Veiculo(
                "DEF-456-MC", "Honda",
                "Civic", "Preto", joao
        );

        // 6. Testar registo
        System.out.println("Veículo da Maria: "
                + veiculoService.registar(carroMaria));

        System.out.println("Veículo do João: "
                + veiculoService.registar(carroJoao));

        // 7. Testar actualização com proprietário inexistente
        System.out.println("Actualizar para João: "
                + veiculoService.actualizar(
                "ABC-123-MC",
                "Toyota",
                "Corolla",
                "Azul",
                joao
        ));

        // 8. Testar actualização com proprietário registado
        System.out.println("Actualizar para Ana: "
                + veiculoService.actualizar(
                "ABC-123-MC",
                "Toyota",
                "Corolla",
                "Azul",
                ana
        ));

        // 9. Mostrar estado final
        System.out.println("\n=== ESTADO FINAL ===");

        System.out.println("Total de veículos: "
                + veiculoService.quantidade());

        System.out.println("Proprietário actual: "
                + veiculoService.procurarPorMatricula(
                "ABC-123-MC"
        ).getProprietario().getNome());
    }
}
