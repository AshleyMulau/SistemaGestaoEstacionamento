
package mz.academia.grupo8.view;

import mz.academia.grupo8.model.*;
import mz.academia.grupo8.service.VeiculoService;
import mz.academia.grupo8.service.UtilizadorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VeiculoView extends JFrame {

    // Serviços necessários
    private final VeiculoService veiculoService;
    private final UtilizadorService utilizadorService;


    // Componentes da tabela
    private JTable tabela;
    private DefaultTableModel modeloTabela;


    // Campos do formulário
    private JTextField campoMatricula;
    private JTextField campoMarca;
    private JTextField campoModelo;
    private JTextField campoCor;
    private JTextField campoProprietario;

    public VeiculoView(
            VeiculoService veiculoService,
            UtilizadorService utilizadorService) {

        this.veiculoService = veiculoService;
        this.utilizadorService = utilizadorService;

        setTitle("Gestão de Veículos");
        setSize(1050, 750);
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

        // Título
        JLabel titulo = new JLabel("Gestão de Veículos");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        principal.add(titulo, BorderLayout.NORTH);

        // Formulário
        JPanel formulario = new JPanel(new GridLayout(0, 2, 10, 10));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(
                BorderFactory.createTitledBorder("Dados do Veículo")
        );

        campoMatricula = new JTextField();
        campoMarca = new JTextField();
        campoModelo = new JTextField();
        campoCor = new JTextField();
        campoProprietario = new JTextField();

        formulario.add(new JLabel("Matrícula:"));
        formulario.add(campoMatricula);

        formulario.add(new JLabel("Marca:"));
        formulario.add(campoMarca);

        formulario.add(new JLabel("Modelo:"));
        formulario.add(campoModelo);

        formulario.add(new JLabel("Cor:"));
        formulario.add(campoCor);

        formulario.add(new JLabel("Código do Proprietário:"));
        formulario.add(campoProprietario);

        // Painel dos botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        painelBotoes.setBackground(new Color(245, 247, 250));

        JButton botaoRegistar = new JButton("Registar");
        botaoRegistar.addActionListener(e -> registarVeiculo());
        JButton botaoActualizar = new JButton("Actualizar");
        JButton botaoRemover = new JButton("Remover");
        JButton botaoLimpar = new JButton("Limpar");

// Adicionar os botões ao painel
        painelBotoes.add(botaoRegistar);
        painelBotoes.add(botaoActualizar);
        painelBotoes.add(botaoRemover);
        painelBotoes.add(botaoLimpar);

// Posicionar os botões
        principal.add(painelBotoes, BorderLayout.SOUTH);

        // Modelo da tabela
        String[] colunas = {
                "Matrícula",
                "Marca",
                "Modelo",
                "Cor",
                "Proprietário"
        };

        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

// Criar tabela
        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(28);
        tabela.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );
        // Permitir seleccionar apenas um veículo
        tabela.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

// Detectar a selecção de uma linha
        tabela.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int linha = tabela.getSelectedRow();

                if (linha != -1) {

                    String matricula = tabela.getValueAt(
                            linha, 0
                    ).toString();

                    Veiculo veiculo =
                            veiculoService.procurarPorMatricula(matricula);

                    if (veiculo != null) {

                        campoMatricula.setText(veiculo.getMatricula());
                        campoMarca.setText(veiculo.getMarca());
                        campoModelo.setText(veiculo.getModelo());
                        campoCor.setText(veiculo.getCor());

                        campoProprietario.setText(
                                veiculo.getProprietario().getCodigo()
                        );
                    }
                }
            }
        });

// Scroll da tabela
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(
                BorderFactory.createTitledBorder("Veículos Registados")
        );

// Dividir formulário e tabela
        JSplitPane divisao = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                formulario,
                scroll
        );

        divisao.setResizeWeight(0.45);
        divisao.setDividerLocation(300);
        divisao.setOneTouchExpandable(true);
        divisao.setContinuousLayout(true);

        principal.add(divisao, BorderLayout.CENTER);

// Adicionar ao painel principal

        setContentPane(principal);
    }

    private void carregarTabela() {

        // Limpar os dados anteriores da tabela
        modeloTabela.setRowCount(0);

        // Obter todos os veículos registados
        for (Veiculo veiculo : veiculoService.listarTodos()) {

            // Adicionar os dados do veículo à tabela
            modeloTabela.addRow(new Object[]{
                    veiculo.getMatricula(),
                    veiculo.getMarca(),
                    veiculo.getModelo(),
                    veiculo.getCor(),
                    veiculo.getProprietario().getNome()
            });
        }
    }

    private void registarVeiculo() {

        try {

            // Recolher os dados do formulário
            String matricula = campoMatricula.getText().trim();
            String marca = campoMarca.getText().trim();
            String modelo = campoModelo.getText().trim();
            String cor = campoCor.getText().trim();
            String codigoProprietario = campoProprietario.getText().trim();

            // Procurar o proprietário pelo código
            Utilizador proprietario =
                    utilizadorService.procurarPorCodigo(codigoProprietario);

            if (proprietario == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "O proprietário indicado não está registado!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Criar o veículo
            Veiculo veiculo = new Veiculo(
                    matricula,
                    marca,
                    modelo,
                    cor,
                    proprietario
            );

            // Registar através do serviço
            boolean registado = veiculoService.registar(veiculo);

            if (registado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Veículo registado com sucesso!"
                );

                carregarTabela();
                limparCampos();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível registar o veículo. Verifique se a matrícula já existe.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (IllegalArgumentException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    erro.getMessage(),
                    "Dados inválidos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limparCampos() {

        campoMatricula.setText("");
        campoMarca.setText("");
        campoModelo.setText("");
        campoCor.setText("");
        campoProprietario.setText("");

        campoMatricula.requestFocus();
    }
}
