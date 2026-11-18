
package mz.academia.grupo8.view;

import mz.academia.grupo8.model.*;
import mz.academia.grupo8.service.UtilizadorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UtilizadorView extends JFrame {

    private final UtilizadorService utilizadorService;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private JTextField campoCodigo;
    private JTextField campoNome;
    private JTextField campoTelefone;
    private JTextField campoEmail;
    private JTextField campoCurso;
    private JTextField campoAno;
    private JTextField campoDepartamento;
    private JComboBox<String> campoCategoria;

    private JComboBox<String> tipoUtilizador;
    private JPanel painelCamposEspecificos;
    private CardLayout layoutCampos;

    public UtilizadorView(UtilizadorService utilizadorService) {

        this.utilizadorService = utilizadorService;

        setTitle("Gestão de Utilizadores");
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

        JLabel titulo = new JLabel("Gestão de Utilizadores");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));

        principal.add(titulo, BorderLayout.NORTH);

        // Formulário
        JPanel formulario = new JPanel(new GridLayout(0, 2, 10, 10));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(
                BorderFactory.createTitledBorder("Dados do utilizador")
        );

        tipoUtilizador = new JComboBox<>(
                new String[]{"Estudante", "Docente/Funcionário"}
        );

        campoCodigo = new JTextField();
        campoNome = new JTextField();
        campoTelefone = new JTextField();
        campoEmail = new JTextField();
        campoCurso = new JTextField();
        campoAno = new JTextField();
        campoDepartamento = new JTextField();
        campoCategoria = new JComboBox<>(new String[]{
                "Docente",
                "Funcionário"
        });
        // Criar painel dinâmico
        layoutCampos = new CardLayout();
        painelCamposEspecificos = new JPanel(layoutCampos);

// Painel para estudantes
        JPanel painelEstudante = new JPanel(new GridLayout(2, 2, 10, 10));

        painelEstudante.add(new JLabel("Curso:"));
        painelEstudante.add(campoCurso);

        painelEstudante.add(new JLabel("Ano académico:"));
        painelEstudante.add(campoAno);

// Painel para docentes e funcionários
        JPanel painelDocente = new JPanel(new GridLayout(2, 2, 10, 10));

        painelDocente.add(new JLabel("Departamento:"));
        painelDocente.add(campoDepartamento);

        painelDocente.add(new JLabel("Categoria:"));
        painelDocente.add(campoCategoria);

// Adicionar os dois painéis
        painelCamposEspecificos.add(painelEstudante, "ESTUDANTE");
        painelCamposEspecificos.add(painelDocente, "DOCENTE");

// Adicionar os campos principais
        adicionarCampo(formulario, "Tipo:", tipoUtilizador);
        adicionarCampo(formulario, "Código:", campoCodigo);
        adicionarCampo(formulario, "Nome:", campoNome);
        adicionarCampo(formulario, "Telefone:", campoTelefone);
        adicionarCampo(formulario, "Email:", campoEmail);

// Adicionar os campos específicos
        formulario.add(new JLabel("Dados específicos:"));
        formulario.add(painelCamposEspecificos);

// Mostrar inicialmente os campos de estudante
        layoutCampos.show(painelCamposEspecificos, "ESTUDANTE");

        // Criar botões
        JButton botaoRegistar = new JButton("Registar utilizador");
        JButton botaoActualizar = new JButton("Actualizar utilizador");
        JButton botaoRemover = new JButton("Remover utilizador");
        JButton botaoLimpar = new JButton("Limpar campos");

// Adicionar botões ao formulário
        formulario.add(botaoRegistar);
        formulario.add(botaoActualizar);
        formulario.add(botaoRemover);
        formulario.add(botaoLimpar);

// Associar eventos
        botaoRegistar.addActionListener(e -> registarUtilizador());
        botaoLimpar.addActionListener(e -> limparCampos());
        botaoActualizar.addActionListener(e -> actualizarUtilizador());
        botaoRemover.addActionListener(e -> removerUtilizador());
        tipoUtilizador.addActionListener(e -> {

            if (tipoUtilizador.getSelectedIndex() == 0) {

                layoutCampos.show(painelCamposEspecificos, "ESTUDANTE");

            } else {

                layoutCampos.show(painelCamposEspecificos, "DOCENTE");

            }

        });

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(new Color(245, 247, 250));

        // Tabela
        String[] colunas = {
                "Código",
                "Nome",
                "Telefone",
                "Email",
                "Tipo"
        };

        modeloTabela = new DefaultTableModel(colunas, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(25);
        // Permitir seleccionar apenas uma linha de cada vez
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

// Carregar os dados do utilizador seleccionado
        tabela.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int linha = tabela.getSelectedRow();

                if (linha != -1) {

                    String codigo = modeloTabela.getValueAt(linha, 0).toString();

                    Utilizador utilizador =
                            utilizadorService.procurarPorCodigo(codigo);

                    if (utilizador != null) {
                        carregarDadosFormulario(utilizador);
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setPreferredSize(new Dimension(800, 200));
        scroll.setBorder(
                BorderFactory.createTitledBorder("Utilizadores registados")
        );

        JSplitPane divisao = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                formulario,
                scroll
        );

        divisao.setResizeWeight(0.60);
        divisao.setDividerLocation(420);
        divisao.setOneTouchExpandable(true);
        divisao.setContinuousLayout(true);

        centro.add(divisao, BorderLayout.CENTER);

        principal.add(centro, BorderLayout.CENTER);

        add(principal);
    }

    private void adicionarCampo(
            JPanel painel,
            String texto,
            JComponent campo) {

        painel.add(new JLabel(texto));
        painel.add(campo);
    }

    private void registarUtilizador() {

        try {

            String codigo = campoCodigo.getText().trim();
            String nome = campoNome.getText().trim();
            String telefone = campoTelefone.getText().trim();
            String email = campoEmail.getText().trim();

            Utilizador utilizador;

            if (tipoUtilizador.getSelectedIndex() == 0) {

                String curso = campoCurso.getText().trim();
                int ano = Integer.parseInt(campoAno.getText().trim());

                utilizador = new Estudante(
                        codigo, nome, telefone, email, curso, ano
                );

            } else {

                String departamento = campoDepartamento.getText().trim();
                String categoria = campoCategoria.getSelectedItem().toString();

                utilizador = new DocenteFuncionario(
                        codigo, nome, telefone, email,
                        departamento, categoria
                );
            }

            boolean registado = utilizadorService.registar(utilizador);

            if (registado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Utilizador registado com sucesso!"
                );

                carregarTabela();
                limparCampos();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Já existe um utilizador com esse código."
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "O ano académico deve ser um número válido."
            );

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage()
            );
        }
    }

    private void actualizarUtilizador() {

        String codigo = campoCodigo.getText().trim();
        String nome = campoNome.getText().trim();
        String telefone = campoTelefone.getText().trim();
        String email = campoEmail.getText().trim();

        if (codigo.isEmpty() || nome.isEmpty() ||
                telefone.isEmpty() || email.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Preencha todos os campos obrigatórios.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        boolean actualizado = utilizadorService.actualizar(
                codigo, nome, telefone, email
        );

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Utilizador actualizado com sucesso!"
            );

            carregarTabela();
            limparCampos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível actualizar o utilizador.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void removerUtilizador() {

        String codigo = campoCodigo.getText().trim();

        if (codigo.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione um utilizador na tabela para remover.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Tem a certeza de que deseja remover este utilizador?",
                "Confirmar remoção",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (opcao == JOptionPane.YES_OPTION) {

            boolean removido = utilizadorService.remover(codigo);

            if (removido) {

                JOptionPane.showMessageDialog(
                        this,
                        "Utilizador removido com sucesso!"
                );

                carregarTabela();
                limparCampos();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível remover o utilizador.\n" +
                                "Verifique se possui veículos registados.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void carregarTabela() {

        modeloTabela.setRowCount(0);

        for (Utilizador utilizador : utilizadorService.listarTodos()) {

            String tipo;

            if (utilizador instanceof Estudante) {
                tipo = "Estudante";
            } else {
                tipo = "Docente/Funcionário";
            }

            modeloTabela.addRow(new Object[]{
                    utilizador.getCodigo(),
                    utilizador.getNome(),
                    utilizador.getTelefone(),
                    utilizador.getEmail(),
                    tipo
            });
        }
        System.out.println(
                "Total de utilizadores apresentados: " +
                        modeloTabela.getRowCount()
        );
        System.out.println(
                "Utilizadores no serviço: " +
                        utilizadorService.quantidade()
        );

        System.out.println(
                "Linhas na tabela: " +
                        modeloTabela.getRowCount()
        );
    }


    private void limparCampos() {

        campoCodigo.setText("");
        campoNome.setText("");
        campoTelefone.setText("");
        campoEmail.setText("");
        campoCurso.setText("");
        campoAno.setText("");
        campoDepartamento.setText("");
        campoCategoria.setSelectedIndex(0);

        tipoUtilizador.setSelectedIndex(0);
    }

    private void carregarDadosFormulario(Utilizador utilizador) {

        campoCodigo.setText(utilizador.getCodigo());
        campoNome.setText(utilizador.getNome());
        campoTelefone.setText(utilizador.getTelefone());
        campoEmail.setText(utilizador.getEmail());

        // Verificar se é estudante
        if (utilizador instanceof Estudante estudante) {

            tipoUtilizador.setSelectedIndex(0);

            campoCurso.setText(estudante.getCurso());
            campoAno.setText(
                    String.valueOf(estudante.getAnoAcademico())
            );

        } else if (utilizador instanceof DocenteFuncionario docente) {

            tipoUtilizador.setSelectedIndex(1);

            campoDepartamento.setText(docente.getDepartamento());
            campoCategoria.setSelectedItem(docente.getCategoria());
        }
    }
}
