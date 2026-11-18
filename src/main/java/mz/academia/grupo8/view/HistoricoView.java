
package mz.academia.grupo8.view;

import mz.academia.grupo8.model.Estacionamento;
import mz.academia.grupo8.service.EstacionamentoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoricoView extends JFrame {

    private final EstacionamentoService estacionamentoService;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private JComboBox<String> filtroEstado;
    private JTextField campoCodigoUtilizador;

    private JLabel labelTotal;
    private JLabel labelActivos;
    private JLabel labelTerminados;

    private final DateTimeFormatter formatoData =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public HistoricoView(EstacionamentoService estacionamentoService) {

        this.estacionamentoService = estacionamentoService;

        setTitle("Histórico de Estacionamento");
        setSize(1150, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        criarInterface();
        carregarTabela();
    }

    private void criarInterface() {

        JPanel principal = new JPanel(new BorderLayout(15, 15));
        principal.setBackground(new Color(245, 247, 250));
        principal.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel titulo = new JLabel("Histórico de Estacionamento");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(40, 55, 75));

        principal.add(titulo, BorderLayout.NORTH);

        // Painel de pesquisa e filtros
        JPanel painelPesquisa = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 10)
        );
        painelPesquisa.setBackground(Color.WHITE);
        painelPesquisa.setBorder(
                BorderFactory.createTitledBorder("Pesquisar Registos")
        );

        filtroEstado = new JComboBox<>(new String[]{
                "Todos",
                "Activos",
                "Terminados",
                "Por Utilizador"
        });

        campoCodigoUtilizador = new JTextField(12);

        JButton botaoPesquisar = new JButton("Pesquisar");
        JButton botaoActualizar = new JButton("Actualizar");

        painelPesquisa.add(new JLabel("Estado:"));
        painelPesquisa.add(filtroEstado);

        painelPesquisa.add(new JLabel("Código do Utilizador:"));
        painelPesquisa.add(campoCodigoUtilizador);

        painelPesquisa.add(botaoPesquisar);
        painelPesquisa.add(botaoActualizar);

        // Tabela
        String[] colunas = {
                "Código",
                "Matrícula",
                "Proprietário",
                "Lugar",
                "Data de Entrada",
                "Data de Saída",
                "Estado"
        };

        modeloTabela = new DefaultTableModel(colunas, 0) {

            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(28);
        tabela.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabela.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        JScrollPane scrollTabela = new JScrollPane(tabela);

        JPanel painelTabela = new JPanel(new BorderLayout(5, 5));
        painelTabela.setBackground(Color.WHITE);
        painelTabela.setBorder(
                BorderFactory.createTitledBorder("Registos")
        );

        painelTabela.add(painelPesquisa, BorderLayout.NORTH);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);

        // Indicadores
        JPanel painelIndicadores = new JPanel(
                new GridLayout(1, 3, 15, 10)
        );
        painelIndicadores.setBackground(
                new Color(245, 247, 250)
        );

        labelTotal = new JLabel("Total: 0");
        labelActivos = new JLabel("Activos: 0");
        labelTerminados = new JLabel("Terminados: 0");

        for (JLabel label : new JLabel[]{
                labelTotal, labelActivos, labelTerminados
        }) {
            label.setFont(new Font("Arial", Font.BOLD, 15));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBorder(
                    BorderFactory.createEmptyBorder(10, 5, 10, 5)
            );
            label.setOpaque(true);
            label.setBackground(Color.WHITE);
        }

        painelIndicadores.add(labelTotal);
        painelIndicadores.add(labelActivos);
        painelIndicadores.add(labelTerminados);

        JPanel painelCentro = new JPanel(new BorderLayout(10, 10));
        painelCentro.setBackground(new Color(245, 247, 250));
        painelCentro.add(painelPesquisa, BorderLayout.NORTH);
        painelCentro.add(painelTabela, BorderLayout.CENTER);
        painelCentro.add(painelIndicadores, BorderLayout.SOUTH);

        principal.add(painelCentro, BorderLayout.CENTER);

        setContentPane(principal);

        // Eventos
        botaoPesquisar.addActionListener(e -> carregarTabela());

        botaoActualizar.addActionListener(e -> {
            filtroEstado.setSelectedItem("Todos");
            campoCodigoUtilizador.setText("");
            carregarTabela();
        });

        filtroEstado.addActionListener(e -> carregarTabela());
    }

    private void carregarTabela() {

        modeloTabela.setRowCount(0);

        String filtro = (String) filtroEstado.getSelectedItem();

        List<Estacionamento> registos;

        switch (filtro) {

            case "Activos":
                registos = estacionamentoService.listarActivos();
                break;

            case "Terminados":
                registos = estacionamentoService.listarHistorico()
                        .stream()
                        .filter(e -> !e.estaActivo())
                        .toList();
                break;

            case "Por Utilizador":

                String codigo = campoCodigoUtilizador.getText().trim();

                if (codigo.isEmpty()) {
                    actualizarIndicadores();
                    return;
                }

                registos = estacionamentoService.consultarPorUtilizador(
                        codigo
                );
                break;

            default:
                registos = estacionamentoService.listarHistorico();
                break;
        }

        for (Estacionamento estacionamento : registos) {
            adicionarNaTabela(estacionamento);
        }

        actualizarIndicadores();
    }

    private void adicionarNaTabela(Estacionamento e) {

        String dataEntrada =
                e.getDataHoraEntrada().format(formatoData);

        String dataSaida =
                e.getDataHoraSaida() == null
                        ? "—"
                        : e.getDataHoraSaida().format(formatoData);

        String estado =
                e.estaActivo() ? "Activo" : "Terminado";

        modeloTabela.addRow(new Object[]{
                e.getCodigo(),
                e.getVeiculo().getMatricula(),
                e.getProprietario().getNome(),
                e.getLugar().getNumero(),
                dataEntrada,
                dataSaida,
                estado
        });
    }

    private void actualizarIndicadores() {

        List<Estacionamento> todos =
                estacionamentoService.listarHistorico();

        long total = todos.size();

        long activos = estacionamentoService.contarActivos();

        long terminados = total - activos;

        labelTotal.setText("Total: " + total);
        labelActivos.setText("Activos: " + activos);
        labelTerminados.setText("Terminados: " + terminados);
    }
}
