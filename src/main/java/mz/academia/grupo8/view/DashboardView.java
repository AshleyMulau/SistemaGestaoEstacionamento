
package mz.academia.grupo8.view;

import mz.academia.grupo8.service.*;
import mz.academia.grupo8.persistence.SistemaPersistencia;
import mz.academia.grupo8.view.LugarView;

import javax.swing.*;
import java.awt.*;


public class DashboardView extends JFrame {

    // Serviços do sistema
    private final UtilizadorService utilizadorService;
    private final VeiculoService veiculoService;
    private final LugarService lugarService;
    private final EstacionamentoService estacionamentoService;

    // Componentes principais
    private JPanel painelPrincipal;
    private JPanel menuLateral;
    private JPanel painelConteudo;

    public DashboardView(
            UtilizadorService utilizadorService,
            VeiculoService veiculoService,
            LugarService lugarService,
            EstacionamentoService estacionamentoService) {

        this.utilizadorService = utilizadorService;
        this.veiculoService = veiculoService;
        this.lugarService = lugarService;
        this.estacionamentoService = estacionamentoService;

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        configurarJanela();
        criarInterface();

        addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {

                int opcao = JOptionPane.showConfirmDialog(
                        DashboardView.this,
                        "Deseja guardar os dados e sair do sistema?",
                        "Confirmar saída",
                        JOptionPane.YES_NO_OPTION
                );

                if (opcao == JOptionPane.YES_OPTION) {

                    SistemaPersistencia persistencia =
                            new SistemaPersistencia();

                    persistencia.guardar(
                            utilizadorService,
                            veiculoService,
                            lugarService,
                            estacionamentoService
                    );

                    dispose();
                    System.exit(0);
                }
            }
        });


    }

    // Configurar a janela
    private void configurarJanela() {

        setTitle("Sistema de Gestão de Parque de Estacionamento");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
    }

    // Construir a interface
    private void criarInterface() {

        painelPrincipal = new JPanel(new BorderLayout());

        painelPrincipal.setBackground(new Color(245, 247, 250));

        criarMenuLateral();
        criarPainelConteudo();

        add(painelPrincipal);
    }

    // Criar menu lateral
    private void criarMenuLateral() {

        menuLateral = new JPanel();
        menuLateral.setPreferredSize(new Dimension(220, 700));
        menuLateral.setBackground(new Color(25, 45, 75));
        menuLateral.setLayout(new BoxLayout(menuLateral, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("  ESTACIONAMENTO");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 10, 35, 10));

        menuLateral.add(titulo);

        String[] opcoes = {
                "Dashboard",
                "Utilizadores",
                "Veículos",
                "Lugares",
                "Estacionamento",
                "Histórico",
                "Relatórios"
        };

        for (String opcao : opcoes) {

            JButton botao = new JButton(opcao);

            botao.addActionListener(e -> {

                if (opcao.equals("Utilizadores")) {

                    UtilizadorView janela = new UtilizadorView(
                            utilizadorService
                    );

                    janela.setVisible(true);

                } else if (opcao.equals("Veículos")) {

                    VeiculoView janela = new VeiculoView(
                            veiculoService,
                            utilizadorService
                    );

                    janela.setVisible(true);
                }
                else if (opcao.equals("Lugares")) {
                    LugarView janela = new LugarView(lugarService);
                    janela.setVisible(true);
                }


                else if (opcao.equals("Estacionamento")) {

                    EstacionamentoView janela = new EstacionamentoView(
                            estacionamentoService,
                            utilizadorService,
                            veiculoService,
                            lugarService
                    );

                    janela.setVisible(true);
                }

                else if (opcao.equals("Histórico")) {

                    HistoricoView janela = new HistoricoView(
                            estacionamentoService
                    );

                    janela.setVisible(true);
                }

                else if (opcao.equals("Relatórios")) {

                    RelatorioView janela = new RelatorioView(
                            utilizadorService,
                            veiculoService,
                            lugarService,
                            estacionamentoService
                    );

                    janela.setVisible(true);
                }
            });

            botao.setMaximumSize(new Dimension(220, 45));
            botao.setAlignmentX(Component.CENTER_ALIGNMENT);
            botao.setForeground(Color.WHITE);
            botao.setBackground(new Color(25, 45, 75));
            botao.setFont(new Font("Arial", Font.PLAIN, 14));
            botao.setFocusPainted(false);
            botao.setBorderPainted(false);
            botao.setHorizontalAlignment(SwingConstants.LEFT);

            menuLateral.add(botao);
        }

        painelPrincipal.add(menuLateral, BorderLayout.WEST);
    }

    // Criar conteúdo principal
    private void criarPainelConteudo() {

        painelConteudo = new JPanel();
        painelConteudo.setBackground(new Color(245, 247, 250));
        painelConteudo.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Dashboard");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));

        painelConteudo.add(titulo, BorderLayout.NORTH);

        JPanel painelIndicadores = new JPanel(new GridLayout(1, 4, 15, 15));
        painelIndicadores.setBackground(new Color(245, 247, 250));
        painelIndicadores.setBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        );

        painelIndicadores.add(criarCartao(
                "Utilizadores",
                utilizadorService.quantidade()
        ));

        painelIndicadores.add(criarCartao(
                "Veículos",
                veiculoService.quantidade()
        ));

        painelIndicadores.add(criarCartao(
                "Lugares livres",
                lugarService.contarLivres()
        ));

        painelIndicadores.add(criarCartao(
                "Lugares ocupados",
                lugarService.contarOcupados()
        ));

        painelConteudo.add(painelIndicadores, BorderLayout.CENTER);

        painelPrincipal.add(painelConteudo, BorderLayout.CENTER);
    }

    // Criar cartão de indicador
    private JPanel criarCartao(String titulo, long valor) {

        JPanel cartao = new JPanel(new BorderLayout());
        cartao.setBackground(Color.WHITE);
        cartao.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 225, 230)
                        ),
                        BorderFactory.createEmptyBorder(20, 15, 20, 15)
                )
        );

        JLabel textoTitulo = new JLabel(titulo);
        textoTitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        textoTitulo.setForeground(new Color(100, 110, 120));

        JLabel textoValor = new JLabel(String.valueOf(valor));
        textoValor.setFont(new Font("Arial", Font.BOLD, 30));
        textoValor.setForeground(new Color(25, 45, 75));

        cartao.add(textoTitulo, BorderLayout.NORTH);
        cartao.add(textoValor, BorderLayout.CENTER);

        return cartao;
    }
}
