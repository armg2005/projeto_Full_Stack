package frontend;

import javax.swing.Timer;
import javax.swing.*;
import java.util.List;
import java.awt.*;
import backend.api.QuizAPI;
import backend.model.Jogador;

public class TelaPrincipal extends JFrame {
    // Componentes que precisaremos acessar depois (Regra 1.1)

    private QuizAPI quizAPI;
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
        JLabel lblNick = new JLabel("Seu nome:");
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
        areaTutorial.setText("LEIA ISSO EMEDIATAMENTE!!!\n\n" +
                "• Responda perguntas de V/F ou Múltipla Escolha.\n" +
                "• Você tem no máximo 60 segundos por questão.\n" +
                "• Pontuação: 1200 * dificuldade / (tempo * questões).\n" +
                "• O Nickname é obrigatório para salvar no Ranking.");
        areaTutorial.setLineWrap(true);
        areaTutorial.setWrapStyleWord(true);
        areaTutorial.setEditable(false);
        areaTutorial.setBackground(new Color(60, 63, 65));
        areaTutorial.setForeground(Color.BLACK);
        areaTutorial.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaTutorial.setBackground(Color.blue);

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
                try {
                    // 1. Criamos a API passando o arquivo CSV que seu amigo mandou
                    QuizAPI api = new QuizAPI("efeito-estufa.csv");

                    // 2. Configuramos a quantidade escolhida e o nick
                    api.setQuantidadePerguntas(qpSelecionado);
                    api.iniciarJogo(nick);

                    // 3. Criamos a tela de jogo passando o objeto API completo
                    TelaDoJogo telaJogo = new TelaDoJogo(api);

                    telaJogo.setVisible(true);
                    this.dispose();

                } catch (Exception ex) {
                    // Caso o arquivo não exista ou o nick seja inválido
                    JOptionPane.showMessageDialog(this, "Erro ao iniciar quiz: " + ex.getMessage());
                }
            }
        });

        Timer pisca = new Timer(500, e -> {
            if(areaTutorial.getBackground().equals(Color.blue)){
                areaTutorial.setBackground(Color.red);
            }else{
                areaTutorial.setBackground(Color.blue);
            }
        });

        pisca.start();
        try {
            QuizAPI apiParaRanking = new QuizAPI("efeito-estufa.csv");
            atualizarTabelaRanking(apiParaRanking);
        } catch (Exception e) {
            System.out.println("Aviso: Não foi possível carregar o ranking inicial.");
        }

    }

    public void atualizarTabelaRanking(QuizAPI quizAPI) {
        try {
            // 1. Instancia a classe onde está o método getTop3 (ajuste o nome se necessário)
            List<Jogador> top3 = quizAPI.getTop3Ranking(); // Pega a lista do back-end
            // 2. Cria a matriz que o JTable exige (3 linhas, 3 colunas)
            Object[][] dadosRanking = new Object[3][3];

            // 3. Loop para preencher a matriz com os dados dos Jogadores
            for (int i = 0; i < 3; i++) {
                if (i < top3.size()) {
                    Jogador j = top3.get(i);
                    dadosRanking[i][0] = (i + 1) + "º"; // Posição
                    dadosRanking[i][1] = j.getNome();   // Nome
                    dadosRanking[i][2] = j.getPontuacao(); // Pontuação
                } else {
                    // Se não tiver 3 jogadores ainda, mantém o vazio
                    dadosRanking[i][0] = (i + 1) + "º";
                    dadosRanking[i][1] = "---";
                    dadosRanking[i][2] = "0";
                }
            }

            // 4. Aplica os novos dados na tabela que já existe na tela
            String[] colunas = {"Posição", "Jogador", "Pontuação"};
            tabelaRanking.setModel(new javax.swing.table.DefaultTableModel(dadosRanking, colunas));

        } catch (Exception e) {
            System.out.println("Erro ao carregar ranking: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Garante que a interface rode na thread correta do Swing
        SwingUtilities.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });
    }
}