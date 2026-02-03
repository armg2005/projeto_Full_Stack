package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaPrincipal extends JFrame {
    // Componentes que precisaremos acessar depois (Regra 1.1)
    private JPanel panel1;
    private JTextField txtNickname;
    private JTextArea areaTutorial;
    private JButton btnIniciar;
    private JButton btnConfig;
    private JTable tabelaRanking;
    private int qpSelecionado = 10;

    public TelaPrincipal() {
        // Configurações básicas da janela
        setTitle("Quiz POO1 - 2025");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Layout livre para posicionamento absoluto
        setLocationRelativeTo(null); // Centraliza no monitor
        getContentPane().setBackground(new Color(43, 45, 48)); // Cor de fundo do IntelliJ

        // 1. Título do Quiz
        JLabel titulo = new JLabel("QUIZ POO1 - 2025", SwingConstants.CENTER);
        titulo.setBounds(100, 20, 400, 40);
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE);
        add(titulo);

        // 2. Campo Nickname (Requisito 1.1)
        JLabel lblNick = new JLabel("Nickname do Aluno:");
        lblNick.setBounds(50, 90, 150, 25);
        lblNick.setForeground(Color.WHITE);
        add(lblNick);

        txtNickname = new JTextField();
        txtNickname.setBounds(200, 90, 200, 30);
        txtNickname.setBackground(new Color(60, 63, 65));
        txtNickname.setForeground(Color.WHITE);
        txtNickname.setCaretColor(Color.WHITE);
        add(txtNickname);

        // 3. Área de Tutorial e Regras (Requisito 1.1 e 1.4)
        areaTutorial = new JTextArea();
        areaTutorial.setText("BEM-VINDO AO TUTORIAL DO QUIZ!\n\n" +
                "• Responda perguntas de V/F ou Múltipla Escolha.\n" +
                "• Você tem no máximo 60 segundos por questão.\n" +
                "• Pontuação: 1200 * dificuldade / (tempo * questões).\n" +
                "• O Nickname é obrigatório para salvar no Ranking.\n\n" +
                "Boa sorte, futuro desenvolvedor!");
        areaTutorial.setLineWrap(true);
        areaTutorial.setWrapStyleWord(true);
        areaTutorial.setEditable(false);
        areaTutorial.setBackground(new Color(60, 63, 65));
        areaTutorial.setForeground(new Color(187, 187, 187));
        areaTutorial.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane scrollTutorial = new JScrollPane(areaTutorial);
        scrollTutorial.setBounds(50, 140, 500, 150);
        add(scrollTutorial);

        // 4. Botão Iniciar (Requisito 1.1)
        btnIniciar = new JButton("INICIAR QUIZ");
        btnIniciar.setBounds(225, 310, 150, 45);
        btnIniciar.setBackground(new Color(75, 110, 175));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFocusPainted(false);
        add(btnIniciar);

        // 5. Botão Configurações (Requisito 1.3)
        btnConfig = new JButton("Configurações");
        btnConfig.setBounds(400, 320, 150, 25);
        add(btnConfig);
        btnConfig.addActionListener(e -> {
            // 1. Cria o objeto da tela de configuração
            TelaConfig config = new TelaConfig(this);

            // 2. Manda ela aparecer na tela
            config.setVisible(true);

            this.qpSelecionado = config.getQuantidadePerguntas();
        });

        // 6. Ranking Top 3 (Requisito 1.6)
        JLabel lblRanking = new JLabel("RANKING - TOP 3", SwingConstants.CENTER);
        lblRanking.setBounds(50, 380, 500, 25);
        lblRanking.setForeground(Color.ORANGE);
        lblRanking.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblRanking);

        String[] colunas = {"Posição", "Jogador", "Pontuação"};
        Object[][] dados = {
                {"1º", "Aguardando...", "0"},
                {"2º", "Aguardando...", "0"},
                {"3º", "Aguardando...", "0"}
        };
        tabelaRanking = new JTable(dados, colunas);
        JScrollPane scrollRanking = new JScrollPane(tabelaRanking);
        scrollRanking.setBounds(50, 410, 500, 80);
        add(scrollRanking);

        // Lógica simples do Botão Iniciar
        btnIniciar.addActionListener(e -> {
            String nick = txtNickname.getText().trim();

            if (nick.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Erro: Digite seu Nickname antes de iniciar!");
            } else {
                // 1. Cria a nova tela de jogo
                TelaDoJogo telaJogo = new TelaDoJogo(qpSelecionado);

                // 2. Faz a tela de jogo aparecer
                telaJogo.setVisible(true);

                // 3. Fecha (ou esconde) a tela principal
                this.dispose();
            }
        });
    }

    public static void main(String[] args) {
        // Garante que a interface rode na thread correta do Swing
        SwingUtilities.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });
    }
}