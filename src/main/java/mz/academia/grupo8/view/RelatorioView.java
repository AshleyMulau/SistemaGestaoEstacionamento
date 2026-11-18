
package mz.academia.grupo8.view;

import mz.academia.grupo8.model.*;
import mz.academia.grupo8.service.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RelatorioView extends JFrame {

    private final UtilizadorService utilizadorService;
    private final VeiculoService veiculoService;
    private final LugarService lugarService;
    private final EstacionamentoService estacionamentoService;

    private JTextArea areaRelatorio;

    public RelatorioView(
            UtilizadorService utilizadorService,
            VeiculoService veiculoService,
            LugarService lugarService,
            EstacionamentoService estacionamentoService) {

        this.utilizadorService = utilizadorService;
        this.veiculoService = veiculoService;
        this.lugarService = lugarService;
        this.estacionamentoService = estacionamentoService;

        setTitle("Relatórios do Parque de Estacionamento");
        setSize(950, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        criarInterface();
        gerarRelatorio();
    }

    private void criarInterface() {

        JPanel principal = new JPanel(new BorderLayout(15, 15));
        principal.setBackground(new Color(245, 247, 250));
        principal.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel titulo = new JLabel("Relatórios do Sistema");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(40, 55, 75));

        principal.add(titulo, BorderLayout.NORTH);

        // Área onde será apresentado o relatório
        areaRelatorio = new JTextArea();
        areaRelatorio.setEditable(false);
        areaRelatorio.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaRelatorio.setBackground(Color.WHITE);
        areaRelatorio.setMargin(new Insets(15, 15, 15, 15));

        JScrollPane scroll = new JScrollPane(areaRelatorio);

        principal.add(scroll, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 10)
        );

        painelBotoes.setBackground(
                new Color(245, 247, 250)
        );

        JButton botaoActualizar = new JButton("Actualizar Relatório");
        JButton botaoExportar = new JButton("Exportar Relatório");
        JButton botaoFechar = new JButton("Fechar");

        painelBotoes.add(botaoActualizar);
        painelBotoes.add(botaoExportar);
        painelBotoes.add(botaoFechar);

        principal.add(painelBotoes, BorderLayout.SOUTH);

        setContentPane(principal);

        // Eventos dos botões
        botaoActualizar.addActionListener(e -> gerarRelatorio());

        botaoExportar.addActionListener(e -> exportarRelatorio());

        botaoFechar.addActionListener(e -> dispose());
    }

    private void gerarRelatorio() {

        StringBuilder relatorio = new StringBuilder();

        DateTimeFormatter formatoData =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        relatorio.append("====================================================\n");
        relatorio.append("       SISTEMA DE GESTÃO DE PARQUE DE ESTACIONAMENTO\n");
        relatorio.append("                    RELATÓRIO GERAL\n");
        relatorio.append("====================================================\n\n");

        relatorio.append("Data de geração: ")
                .append(LocalDateTime.now().format(formatoData))
                .append("\n\n");

        // 1. Estatísticas dos utilizadores
        List<Utilizador> utilizadores =
                utilizadorService.listarTodos();

        long estudantes = utilizadores.stream()
                .filter(u -> u instanceof Estudante)
                .count();

        long docentesFuncionarios = utilizadores.stream()
                .filter(u -> u instanceof DocenteFuncionario)
                .count();

        relatorio.append("---------------- UTILIZADORES ----------------\n");
        relatorio.append("Total de utilizadores: ")
                .append(utilizadores.size()).append("\n");

        relatorio.append("Estudantes: ")
                .append(estudantes).append("\n");

        relatorio.append("Docentes/Funcionários: ")
                .append(docentesFuncionarios).append("\n\n");

        // 2. Estatísticas dos veículos
        List<Veiculo> veiculos = veiculoService.listarTodos();

        relatorio.append("---------------- VEÍCULOS ----------------\n");
        relatorio.append("Total de veículos: ")
                .append(veiculos.size()).append("\n");

        relatorio.append("Veículos de estudantes: ")
                .append(veiculoService.listarVeiculosEstudantes().size())
                .append("\n");

        relatorio.append("Veículos de docentes/funcionários: ")
                .append(veiculoService.listarVeiculosDocentes().size())
                .append("\n\n");

        // 3. Estatísticas dos lugares
        relatorio.append("---------------- LUGARES ----------------\n");

        relatorio.append("Total de lugares: ")
                .append(lugarService.quantidade()).append("\n");

        relatorio.append("Lugares livres: ")
                .append(lugarService.contarLivres()).append("\n");

        relatorio.append("Lugares ocupados: ")
                .append(lugarService.contarOcupados()).append("\n\n");

        // 4. Estatísticas dos estacionamentos
        long totalRegistos =
                estacionamentoService.quantidadeRegistos();

        long activos =
                estacionamentoService.contarActivos();

        long terminados = totalRegistos - activos;

        relatorio.append("------------- ESTACIONAMENTOS -------------\n");

        relatorio.append("Total de registos: ")
                .append(totalRegistos).append("\n");

        relatorio.append("Estacionamentos activos: ")
                .append(activos).append("\n");

        relatorio.append("Estacionamentos terminados: ")
                .append(terminados).append("\n\n");

        // 5. Lista de veículos
        relatorio.append("------------- LISTA DE VEÍCULOS -------------\n");

        if (veiculos.isEmpty()) {
            relatorio.append("Não existem veículos registados.\n");
        } else {
            for (Veiculo veiculo : veiculos) {
                relatorio.append("Matrícula: ")
                        .append(veiculo.getMatricula())
                        .append(" | Marca: ")
                        .append(veiculo.getMarca())
                        .append(" | Modelo: ")
                        .append(veiculo.getModelo())
                        .append(" | Proprietário: ")
                        .append(veiculo.getProprietario().getNome())
                        .append("\n");
            }
        }

        relatorio.append("\n");

        // 6. Estado dos lugares
        relatorio.append("------------- ESTADO DOS LUGARES -------------\n");

        List<Lugar> lugares = lugarService.listarTodos();

        for (Lugar lugar : lugares) {
            relatorio.append("Lugar: ")
                    .append(lugar.getNumero())
                    .append(" | Localização: ")
                    .append(lugar.getLocalizacao())
                    .append(" | Tipo: ")
                    .append(lugar.getTipo())
                    .append(" | Estado: ")
                    .append(lugar.getEstado())
                    .append("\n");
        }

        relatorio.append("\n====================================================\n");
        relatorio.append("                FIM DO RELATÓRIO\n");
        relatorio.append("====================================================\n");

        areaRelatorio.setText(relatorio.toString());
        areaRelatorio.setCaretPosition(0);
    }

    private void exportarRelatorio() {

        JFileChooser escolha = new JFileChooser();

        escolha.setDialogTitle("Guardar Relatório");

        escolha.setSelectedFile(
                new java.io.File("Relatorio_Estacionamento.txt")
        );

        int resultado = escolha.showSaveDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {

            java.io.File ficheiro = escolha.getSelectedFile();

            try {
                Files.writeString(
                        ficheiro.toPath(),
                        areaRelatorio.getText(),
                        StandardCharsets.UTF_8
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Relatório exportado com sucesso!",
                        "Exportação",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (IOException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao exportar relatório: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
