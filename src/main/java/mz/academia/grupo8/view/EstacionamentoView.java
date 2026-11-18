package mz.academia.grupo8.view;

import mz.academia.grupo8.model.*;
import mz.academia.grupo8.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EstacionamentoView extends JFrame {

    // Serviços
    private final EstacionamentoService estacionamentoService;
    private final UtilizadorService utilizadorService;
    private final VeiculoService veiculoService;
    private final LugarService lugarService;
    private JTextField campoCodigoRegisto;
    private JTextField campoCodigoPesquisa;

    // Tabela
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    // Campos
    private JTextField campoMatricula;
    private JTextField campoCodigoUtilizador;
    private JTextField campoNumeroLugar;

    // Filtro
    private JComboBox<String> filtroTipo;

    public EstacionamentoView(
            EstacionamentoService estacionamentoService,
            UtilizadorService utilizadorService,
            VeiculoService veiculoService,
            LugarService lugarService) {

        this.estacionamentoService = estacionamentoService;
        this.utilizadorService = utilizadorService;
        this.veiculoService = veiculoService;
        this.lugarService = lugarService;

        setTitle("Gestão de Estacionamento");
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        criarInterface();
        carregarTabela();
    }


    private void criarInterface() {

        // Painel principal
        JPanel principal = new JPanel(new BorderLayout(15, 15));
        principal.setBackground(new Color(245, 247, 250));
        principal.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Título
        JLabel titulo = new JLabel("Gestão de Estacionamento");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(40, 55, 75));

        principal.add(titulo, BorderLayout.NORTH);

        // Formulário de entrada
        JPanel formulario = new JPanel(new GridLayout(0, 2, 10, 10));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(
                BorderFactory.createTitledBorder("Registar Entrada")
        );

        campoCodigoRegisto = new JTextField();
        campoMatricula = new JTextField();
        campoCodigoUtilizador = new JTextField();
        campoNumeroLugar = new JTextField();

        formulario.add(new JLabel("Código do Registo:"));
        formulario.add(campoCodigoRegisto);

        formulario.add(new JLabel("Matrícula:"));
        formulario.add(campoMatricula);

        formulario.add(new JLabel("Código do Proprietário:"));
        formulario.add(campoCodigoUtilizador);

        formulario.add(new JLabel("Número do Lugar:"));
        formulario.add(campoNumeroLugar);

        // Botões
        JPanel painelBotoes = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 10)
        );
        painelBotoes.setBackground(new Color(245, 247, 250));

        JButton botaoEntrada = new JButton("Registar Entrada");
        JButton botaoSaida = new JButton("Registar Saída");
        JButton botaoLimpar = new JButton("Limpar");

        painelBotoes.add(botaoEntrada);
        painelBotoes.add(botaoSaida);
        painelBotoes.add(botaoLimpar);

        // Modelo da tabela
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

        // Tabela
        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(28);
        tabela.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabela.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        JScrollPane scrollTabela = new JScrollPane(tabela);

        // Filtros e pesquisa
        JPanel painelFiltro = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );
        painelFiltro.setBackground(Color.WHITE);

        filtroTipo = new JComboBox<>(new String[]{
                "Activos",
                "Histórico",
                "Por Utilizador"
        });

        campoCodigoPesquisa = new JTextField(10);

        JButton botaoPesquisar = new JButton("Pesquisar");

        painelFiltro.add(new JLabel("Visualizar:"));
        painelFiltro.add(filtroTipo);
        painelFiltro.add(new JLabel("Código do Utilizador:"));
        painelFiltro.add(campoCodigoPesquisa);
        painelFiltro.add(botaoPesquisar);

        JPanel painelTabela = new JPanel(new BorderLayout(5, 5));
        painelTabela.setBackground(Color.WHITE);
        painelTabela.setBorder(
                BorderFactory.createTitledBorder(
                        "Registos de Estacionamento"
                )
        );

        painelTabela.add(painelFiltro, BorderLayout.NORTH);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);

        // Divisão entre formulário e tabela
        JSplitPane divisao = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                formulario,
                painelTabela
        );

        divisao.setResizeWeight(0.35);
        divisao.setDividerLocation(250);
        divisao.setOneTouchExpandable(true);
        divisao.setContinuousLayout(true);


// Registar entrada de um veículo
        botaoEntrada.addActionListener(e -> {

            String codigo = campoCodigoRegisto.getText().trim();
            String matricula = campoMatricula.getText().trim();
            String codigoUtilizador = campoCodigoUtilizador.getText().trim();
            String numeroTexto = campoNumeroLugar.getText().trim();

            if (codigo.isEmpty() || matricula.isEmpty()
                    || codigoUtilizador.isEmpty() || numeroTexto.isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Preencha todos os campos.",
                        "Campos obrigatórios",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {

                int numeroLugar = Integer.parseInt(numeroTexto);

                boolean sucesso = estacionamentoService.registarEntrada(
                        codigo,
                        matricula,
                        codigoUtilizador,
                        numeroLugar
                );

                if (sucesso) {

                    JOptionPane.showMessageDialog(this,
                            "Entrada registada com sucesso!");

                    limparCampos();
                    filtroTipo.setSelectedItem("Activos");
                    carregarTabela();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Não foi possível registar a entrada.\n"
                                    + "Verifique o veículo, proprietário e lugar.",
                            "Operação recusada",
                            JOptionPane.WARNING_MESSAGE);
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(this,
                        "O número do lugar deve ser um número inteiro.",
                        "Valor inválido",
                        JOptionPane.ERROR_MESSAGE);
            }
        });


// Registar saída de um veículo
        botaoSaida.addActionListener(e -> {

            String matricula = campoMatricula.getText().trim();

            if (matricula.isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Introduza a matrícula do veículo.",
                        "Campo obrigatório",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja registar a saída do veículo " + matricula + "?",
                    "Confirmar saída",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacao == JOptionPane.YES_OPTION) {

                boolean sucesso = estacionamentoService.registarSaida(matricula);

                if (sucesso) {

                    JOptionPane.showMessageDialog(this,
                            "Saída registada com sucesso!");

                    limparCampos();
                    filtroTipo.setSelectedItem("Activos");
                    carregarTabela();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Não foi possível registar a saída.\n"
                                    + "Verifique se o veículo está estacionado.",
                            "Operação recusada",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });


// Limpar formulário
        botaoLimpar.addActionListener(e -> limparCampos());


// Alterar o filtro de visualização
        filtroTipo.addActionListener(e -> carregarTabela());


// Pesquisar registos por utilizador
        botaoPesquisar.addActionListener(e -> {

            String codigo = campoCodigoPesquisa.getText().trim();

            if (codigo.isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Introduza o código do utilizador.",
                        "Campo obrigatório",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            filtroTipo.setSelectedItem("Por Utilizador");
            carregarTabela();
        });

        principal.add(divisao, BorderLayout.CENTER);
        principal.add(painelBotoes, BorderLayout.SOUTH);

        setContentPane(principal);
    }


    private void carregarTabela() {

        modeloTabela.setRowCount(0);

        String tipo = (String) filtroTipo.getSelectedItem();

        List<Estacionamento> registos;

        if ("Activos".equals(tipo)) {

            registos = estacionamentoService.listarActivos();

        } else if ("Histórico".equals(tipo)) {

            registos = estacionamentoService.listarHistorico();

        } else {

            String codigo = campoCodigoPesquisa.getText().trim();

            if (codigo.isEmpty()) {
                return;
            }

            registos = estacionamentoService.consultarPorUtilizador(codigo);
        }

        for (Estacionamento estacionamento : registos) {
            adicionarEstacionamentoNaTabela(estacionamento);
        }
    }

    private void adicionarEstacionamentoNaTabela(Estacionamento e) {

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        String entrada = e.getDataHoraEntrada().format(formato);

        String saida = e.getDataHoraSaida() == null
                ? "—"
                : e.getDataHoraSaida().format(formato);

        String estado = e.estaActivo()
                ? "Activo"
                : "Terminado";

        Object[] linha = {
                e.getCodigo(),
                e.getVeiculo().getMatricula(),
                e.getProprietario().getNome(),
                e.getLugar().getNumero(),
                entrada,
                saida,
                estado
        };

        modeloTabela.addRow(linha);
    }


    private void limparCampos() {

        campoCodigoRegisto.setText("");
        campoMatricula.setText("");
        campoCodigoUtilizador.setText("");
        campoNumeroLugar.setText("");

        campoMatricula.requestFocus();
    }


}