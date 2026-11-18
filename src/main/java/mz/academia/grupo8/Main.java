
package mz.academia.grupo8;

import mz.academia.grupo8.service.*;
import mz.academia.grupo8.persistence.SistemaPersistencia;
import mz.academia.grupo8.view.DashboardView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        // 1. Criar os serviços
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

        // 2. Ligar os serviços
        veiculoService.definirEstacionamentoService(
                estacionamentoService
        );

        veiculoService.definirUtilizadorService(
                utilizadorService
        );

        utilizadorService.definirServicosRelacionados(
                veiculoService,
                estacionamentoService
        );

        // 3. Preparar a persistência
        SistemaPersistencia persistencia =
                new SistemaPersistencia();

        // 4. Carregar dados existentes, se houver
        if (persistencia.existeDados()) {

            boolean carregado = persistencia.carregar(
                    utilizadorService,
                    veiculoService,
                    lugarService,
                    estacionamentoService
            );

            System.out.println(
                    "Dados carregados: " + carregado
            );
        }

        SwingUtilities.invokeLater(() -> {

            DashboardView dashboard = new DashboardView(
                    utilizadorService,
                    veiculoService,
                    lugarService,
                    estacionamentoService
            );

            dashboard.setVisible(true);

        });
    }
}
