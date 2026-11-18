
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

        principal.add(formulario, BorderLayout.CENTER);
        // Painel dos botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        painelBotoes.setBackground(new Color(245, 247, 250));

        JButton botaoRegistar = new JButton("Registar");
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

// Adicionar ao painel principal

        setContentPane(principal);
    }

    private void carregarTabela() {
        // A tabela será carregada nesta etapa seguinte.
    }
}
