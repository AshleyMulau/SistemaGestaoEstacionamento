
package mz.academia.grupo8;

import mz.academia.grupo8.model.*;
import mz.academia.grupo8.service.*;

public class TesteIntegridadeUtilizadores {

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

        veiculoService.definirUtilizadorService(utilizadorService);

        utilizadorService.definirServicosRelacionados(
                veiculoService,
                estacionamentoService
        );

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

        // 4. Registar utilizadores
        System.out.println("Maria registada: "
                + utilizadorService.registar(maria));

        System.out.println("Ana registada: "
                + utilizadorService.registar(ana));

        // 5. Criar veículo da Maria
        Veiculo veiculo = new Veiculo(
                "ABC-123-MC",
                "Toyota",
                "Corolla",
                "Branco",
                maria
        );

        veiculoService.registar(veiculo);

        // 6. Criar lugar
        Lugar lugar = new Lugar(
                10,
                "Bloco A",
                TipoLugar.ESTUDANTE
        );

        lugarService.registar(lugar);

        // 7. Registar entrada
        System.out.println("Entrada da Maria: "
                + estacionamentoService.registarEntrada(
                "REG001",
                "ABC-123-MC",
                "U001",
                10
        ));

        // 8. Testar remoção
        System.out.println("Remover Maria: "
                + utilizadorService.remover("U001"));

        System.out.println("Remover Ana: "
                + utilizadorService.remover("U002"));

        // 9. Registar saída da Maria
        System.out.println("Saída da Maria: "
                + estacionamentoService.registarSaida("ABC-123-MC"));

        // 10. Verificar novamente a remoção
        System.out.println("Remover Maria depois da saída: "
                + utilizadorService.remover("U001"));
    }
}
