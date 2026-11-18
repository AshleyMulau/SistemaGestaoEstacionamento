
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
    private JLabel labelTotalVeiculos;


    // Componentes da tabela
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JComboBox<String> filtroTipo;


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
        botaoActualizar.addActionListener(e -> actualizarVeiculo());
        JButton botaoRemover = new JButton("Remover");
        botaoRemover.addActionListener(e -> removerVeiculo());
        JButton botaoLimpar = new JButton("Limpar");
        botaoLimpar.addActionListener(e -> limparCampos());

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

        // Painel de filtro dos veículos
        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelFiltro.setBackground(Color.WHITE);

        JLabel labelFiltro = new JLabel("Filtrar por proprietário:");

        filtroTipo = new JComboBox<>(new String[]{
                "Todos",
                "Estudantes",
                "Docentes/Funcionários"
        });

        painelFiltro.add(labelFiltro);
        painelFiltro.add(filtroTipo);
        // Indicador do total de veículos
        labelTotalVeiculos = new JLabel("Total de veículos: 0");
        labelTotalVeiculos.setFont(new Font("Arial", Font.BOLD, 12));

        painelFiltro.add(labelTotalVeiculos);
        filtroTipo.addActionListener(e -> carregarTabela());

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

        JScrollPane scrollTabela = new JScrollPane(tabela);

        JPanel painelTabela = new JPanel(new BorderLayout(5, 5));
        painelTabela.setBackground(Color.WHITE);

        // Adicionar filtro e tabela ao painel da tabela
        painelTabela.add(painelFiltro, BorderLayout.NORTH);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);

// Dividir a interface entre formulário e tabela
        JSplitPane divisao = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                formulario,
                painelTabela
        );

        divisao.setResizeWeight(0.45);
        divisao.setDividerLocation(300);
        divisao.setOneTouchExpandable(true);
        divisao.setContinuousLayout(true);

        principal.add(divisao, BorderLayout.CENTER);
        divisao.setResizeWeight(0.45);
        divisao.setDividerLocation(300);
        divisao.setOneTouchExpandable(true);
        divisao.setContinuousLayout(true);

        principal.add(divisao, BorderLayout.CENTER);

// Adicionar ao painel principal

        setContentPane(principal);
    }

    private void carregarTabela() {

        modeloTabela.setRowCount(0);

        String tipoSelecionado = filtroTipo.getSelectedItem().toString();

        // Listar todos os veículos
        if (tipoSelecionado.equals("Todos")) {

            for (Veiculo veiculo : veiculoService.listarTodos()) {
                adicionarVeiculoNaTabela(veiculo);
            }

            // Listar apenas veículos de estudantes
        } else if (tipoSelecionado.equals("Estudantes")) {

            for (Veiculo veiculo : veiculoService.listarVeiculosEstudantes()) {
                adicionarVeiculoNaTabela(veiculo);
            }

            // Listar veículos de docentes e funcionários
        } else if (tipoSelecionado.equals("Docentes/Funcionários")) {

            for (Veiculo veiculo : veiculoService.listarVeiculosDocentes()) {
                adicionarVeiculoNaTabela(veiculo);
            }
        }
        // Actualizar o total de veículos apresentados
        labelTotalVeiculos.setText(
                "Total de veículos: " + modeloTabela.getRowCount()
        );
    }

    private void adicionarVeiculoNaTabela(Veiculo veiculo) {

        modeloTabela.addRow(new Object[]{
                veiculo.getMatricula(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getCor(),
                veiculo.getProprietario().getNome()
        });
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

    private void actualizarVeiculo() {

        String matricula = campoMatricula.getText().trim();
        String marca = campoMarca.getText().trim();
        String modelo = campoModelo.getText().trim();
        String cor = campoCor.getText().trim();
        String codigoProprietario = campoProprietario.getText().trim();

        if (matricula.isEmpty() || marca.isEmpty() ||
                modelo.isEmpty() || cor.isEmpty() ||
                codigoProprietario.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Procurar o novo proprietário pelo código
        Utilizador proprietario =
                utilizadorService.procurarPorCodigo(codigoProprietario);

        if (proprietario == null) {
            JOptionPane.showMessageDialog(this,
                    "O proprietário indicado não está registado.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {

            boolean actualizado = veiculoService.actualizar(
                    matricula,
                    marca,
                    modelo,
                    cor,
                    proprietario
            );

            if (actualizado) {

                JOptionPane.showMessageDialog(this,
                        "Veículo actualizado com sucesso!");

                carregarTabela();
                limparCampos();

            } else {

                JOptionPane.showMessageDialog(this,
                        "Não foi possível actualizar o veículo.\n" +
                                "Verifique se está estacionado ou se os dados são válidos.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerVeiculo() {

        String matricula = campoMatricula.getText().trim();

        if (matricula.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione um veículo na tabela.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Tem a certeza de que deseja remover este veículo?",
                "Confirmar remoção",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (resposta == JOptionPane.YES_OPTION) {

            boolean removido = veiculoService.remover(matricula);

            if (removido) {

                JOptionPane.showMessageDialog(this,
                        "Veículo removido com sucesso!");

                carregarTabela();
                limparCampos();

            } else {

                JOptionPane.showMessageDialog(this,
                        "Não foi possível remover o veículo.\n" +
                                "Verifique se está actualmente estacionado.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
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
