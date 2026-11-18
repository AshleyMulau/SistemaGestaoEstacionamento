
package mz.academia.grupo8.view;

import mz.academia.grupo8.model.*;
import mz.academia.grupo8.service.LugarService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LugarView extends JFrame {

    private final LugarService lugarService;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private JTextField campoNumero;
    private JTextField campoLocalizacao;

    private JComboBox<TipoLugar> campoTipo;
    private JComboBox<String> filtroEstado;

    private JLabel labelLivres;
    private JLabel labelOcupados;

    public LugarView(LugarService lugarService) {

        this.lugarService = lugarService;

        setTitle("Gestão de Lugares de Estacionamento");
        setSize(1050, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        criarInterface();
        carregarTabela();
    }

    // Construir a interface
    private void criarInterface() {

        JPanel principal = new JPanel(new BorderLayout(15, 15));
        principal.setBackground(new Color(245, 247, 250));
        principal.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel titulo = new JLabel("Gestão de Lugares");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(40, 55, 75));

        principal.add(titulo, BorderLayout.NORTH);

        // Formulário
        JPanel formulario = new JPanel(new GridLayout(0, 2, 10, 10));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(
                BorderFactory.createTitledBorder("Dados do Lugar")
        );

        campoNumero = new JTextField();
        campoLocalizacao = new JTextField();
        campoTipo = new JComboBox<>(TipoLugar.values());

        formulario.add(new JLabel("Número do Lugar:"));
        formulario.add(campoNumero);

        formulario.add(new JLabel("Localização:"));
        formulario.add(campoLocalizacao);

        formulario.add(new JLabel("Tipo de Lugar:"));
        formulario.add(campoTipo);

        // Botões
        JPanel painelBotoes = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 10)
        );
        painelBotoes.setBackground(new Color(245, 247, 250));

        JButton botaoRegistar = new JButton("Registar");
        JButton botaoActualizar = new JButton("Actualizar");
        JButton botaoRemover = new JButton("Remover");
        JButton botaoLimpar = new JButton("Limpar");

        botaoRegistar.addActionListener(e -> registarLugar());
        botaoActualizar.addActionListener(e -> actualizarLugar());
        botaoRemover.addActionListener(e -> removerLugar());
        botaoLimpar.addActionListener(e -> limparCampos());

        painelBotoes.add(botaoRegistar);
        painelBotoes.add(botaoActualizar);
        painelBotoes.add(botaoRemover);
        painelBotoes.add(botaoLimpar);

        // Modelo da tabela
        String[] colunas = {
                "Número",
                "Localização",
                "Tipo",
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
                new Font("Arial", Font.BOLD, 13)
        );

        // Seleccionar lugar e preencher formulário
        tabela.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int linha = tabela.getSelectedRow();

                if (linha != -1) {

                    int numero = Integer.parseInt(
                            tabela.getValueAt(linha, 0).toString()
                    );

                    Lugar lugar = lugarService.procurarPorNumero(numero);

                    if (lugar != null) {

                        campoNumero.setText(
                                String.valueOf(lugar.getNumero())
                        );

                        campoNumero.setEditable(false);

                        campoLocalizacao.setText(
                                lugar.getLocalizacao()
                        );

                        campoTipo.setSelectedItem(lugar.getTipo());
                    }
                }
            }
        });

        JScrollPane scrollTabela = new JScrollPane(tabela);

        // Painel da tabela e filtros
        JPanel painelTabela = new JPanel(new BorderLayout(5, 5));
        painelTabela.setBackground(Color.WHITE);
        painelTabela.setBorder(
                BorderFactory.createTitledBorder("Lugares Registados")
        );

        JPanel painelFiltro = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 15, 5)
        );
        painelFiltro.setBackground(Color.WHITE);

        filtroEstado = new JComboBox<>(new String[]{
                "Todos",
                "Livres",
                "Ocupados"
        });

        labelLivres = new JLabel();
        labelOcupados = new JLabel();

        painelFiltro.add(new JLabel("Filtrar por estado:"));
        painelFiltro.add(filtroEstado);
        painelFiltro.add(labelLivres);
        painelFiltro.add(labelOcupados);

        filtroEstado.addActionListener(e -> carregarTabela());

        painelTabela.add(painelFiltro, BorderLayout.NORTH);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);

        // Divisão entre formulário e tabela
        JSplitPane divisao = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                formulario,
                painelTabela
        );

        divisao.setResizeWeight(0.35);
        divisao.setDividerLocation(230);
        divisao.setOneTouchExpandable(true);
        divisao.setContinuousLayout(true);

        principal.add(divisao, BorderLayout.CENTER);
        principal.add(painelBotoes, BorderLayout.SOUTH);

        setContentPane(principal);
    }

    // Carregar tabela de acordo com o filtro
    private void carregarTabela() {

        modeloTabela.setRowCount(0);

        String estado = filtroEstado.getSelectedItem().toString();

        if (estado.equals("Todos")) {

            for (Lugar lugar : lugarService.listarTodos()) {
                adicionarLugarNaTabela(lugar);
            }

        } else if (estado.equals("Livres")) {

            for (Lugar lugar : lugarService.listarLivres()) {
                adicionarLugarNaTabela(lugar);
            }

        } else if (estado.equals("Ocupados")) {

            for (Lugar lugar : lugarService.listarOcupados()) {
                adicionarLugarNaTabela(lugar);
            }
        }

        actualizarIndicadores();
    }

    // Adicionar um lugar à tabela
    private void adicionarLugarNaTabela(Lugar lugar) {

        modeloTabela.addRow(new Object[]{
                lugar.getNumero(),
                lugar.getLocalizacao(),
                lugar.getTipo(),
                lugar.getEstado()
        });
    }

    // Actualizar indicadores
    private void actualizarIndicadores() {

        labelLivres.setText(
                "Livres: " + lugarService.contarLivres()
        );

        labelOcupados.setText(
                "Ocupados: " + lugarService.contarOcupados()
        );
    }

    // Registar lugar
    private void registarLugar() {

        String numeroTexto = campoNumero.getText().trim();
        String localizacao = campoLocalizacao.getText().trim();

        TipoLugar tipo = (TipoLugar) campoTipo.getSelectedItem();

        if (numeroTexto.isEmpty() || localizacao.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {

            int numero = Integer.parseInt(numeroTexto);

            Lugar lugar = new Lugar(numero, localizacao, tipo);

            boolean registado = lugarService.registar(lugar);

            if (registado) {

                JOptionPane.showMessageDialog(this,
                        "Lugar registado com sucesso!");

                carregarTabela();
                limparCampos();

            } else {

                JOptionPane.showMessageDialog(this,
                        "Não foi possível registar o lugar. " +
                                "Verifique se o número já existe.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "O número do lugar deve ser inteiro.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Actualizar lugar
    private void actualizarLugar() {

        String numeroTexto = campoNumero.getText().trim();
        String localizacao = campoLocalizacao.getText().trim();

        TipoLugar tipo = (TipoLugar) campoTipo.getSelectedItem();

        if (numeroTexto.isEmpty() || localizacao.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Seleccione um lugar e preencha os campos.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {

            int numero = Integer.parseInt(numeroTexto);

            boolean actualizado = lugarService.actualizar(
                    numero,
                    localizacao,
                    tipo
            );

            if (actualizado) {

                JOptionPane.showMessageDialog(this,
                        "Lugar actualizado com sucesso!");

                carregarTabela();
                limparCampos();

            } else {

                JOptionPane.showMessageDialog(this,
                        "Não foi possível actualizar o lugar. " +
                                "Verifique se existe e se está livre.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Número de lugar inválido.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Remover lugar
    private void removerLugar() {

        String numeroTexto = campoNumero.getText().trim();

        if (numeroTexto.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Seleccione um lugar na tabela.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Tem a certeza de que deseja remover este lugar?",
                "Confirmar remoção",
                JOptionPane.YES_NO_OPTION
        );

        if (resposta == JOptionPane.YES_OPTION) {

            try {

                int numero = Integer.parseInt(numeroTexto);

                boolean removido = lugarService.remover(numero);

                if (removido) {

                    JOptionPane.showMessageDialog(this,
                            "Lugar removido com sucesso!");

                    carregarTabela();
                    limparCampos();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Não foi possível remover o lugar. " +
                                    "Verifique se está ocupado.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(this,
                        "Número de lugar inválido.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Limpar formulário
    private void limparCampos() {

        campoNumero.setText("");
        campoNumero.setEditable(true);

        campoLocalizacao.setText("");
        campoTipo.setSelectedIndex(0);

        tabela.clearSelection();
        campoNumero.requestFocus();
    }
}
