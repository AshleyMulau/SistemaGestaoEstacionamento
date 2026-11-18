
package mz.academia.grupo8;

import mz.academia.grupo8.model.*;
import mz.academia.grupo8.service.*;

public class TesteIntegridadeVeiculos {

    public static void main(String[] args) {

        // Criar serviços
        UtilizadorService utilizadorService =
                new UtilizadorService();

        VeiculoService veiculoService =
                new VeiculoService();

        LugarService lugarService =
                new LugarService();

        EstacionamentoService estacionamentoService =
                new EstacionamentoService(
                        utilizadorService,
                        veiculoService,
                        lugarService
                );

        veiculoService.definirUtilizadorService(utilizadorService);

        // Ligar os serviços
        veiculoService.definirEstacionamentoService(
                estacionamentoService
        );

        utilizadorService.definirServicosRelacionados(
                veiculoService,
                estacionamentoService
        );

        // Criar utilizador
        Estudante estudante = new Estudante(
                "EST200",
                "Carlos Alberto",
                "841111222",
                "carlos@email.com",
                "Engenharia Informática",
                2
        );

        utilizadorService.registar(estudante);

        // Criar outro utilizador
        Estudante outroEstudante = new Estudante(
                "EST201",
                "Ana Pedro",
                "841111333",
                "ana@email.com",
                "Engenharia Informática",
                3
        );

        utilizadorService.registar(outroEstudante);


        // Registar veículo
        Veiculo veiculo = new Veiculo(
                "ABC-456-MP",
                "Toyota",
                "Corolla",
                "Branco",
                estudante
        );

        veiculoService.registar(veiculo);

        // Registar lugar
        Lugar lugar = new Lugar(
                20,
                "Bloco B",
                TipoLugar.ESTUDANTE
        );

        lugarService.registar(lugar);

        // Registar entrada
        boolean entrada = estacionamentoService.registarEntrada(
                "REG200",
                "ABC-456-MP",
                "EST200",
                20
        );

        System.out.println("=== TESTE DE INTEGRIDADE ===");

        System.out.println("Entrada registada: " + entrada);

        // Tentar remover veículo estacionado
        boolean remocao = veiculoService.remover("ABC-456-MP");

        System.out.println(
                "Remover veículo estacionado: " + remocao
        );

        // Tentar alterar proprietário enquanto estacionado
        boolean alteracaoProprietario =
                veiculoService.actualizar(
                        "ABC-456-MP",
                        "Toyota",
                        "Corolla",
                        "Preto",
                        outroEstudante
                );

        System.out.println(
                "Alterar proprietário estacionado: "
                        + alteracaoProprietario
        );

        // Actualizar apenas a cor enquanto estacionado
        boolean alteracaoCor =
                veiculoService.actualizar(
                        "ABC-456-MP",
                        "Toyota",
                        "Corolla",
                        "Azul",
                        estudante
                );

        System.out.println(
                "Actualizar cor enquanto estacionado: "
                        + alteracaoCor
        );

        // Registar saída
        boolean saida = estacionamentoService.registarSaida(
                "ABC-456-MP"
        );

        System.out.println("Saída registada: " + saida);

        // Agora tentar remover novamente
        boolean remocaoDepoisSaida =
                veiculoService.remover("ABC-456-MP");

        System.out.println(
                "Remover veículo depois da saída: "
                        + remocaoDepoisSaida
        );
    }
}
