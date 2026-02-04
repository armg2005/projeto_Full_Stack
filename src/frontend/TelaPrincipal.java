/**
 * Autores: Alisson Ricady e Gustavo Rodrigues
 */
package frontend;

import javax.swing.Timer;
import javax.swing.*;
import java.util.List;
import java.awt.*;
import backend.api.QuizAPI;
import backend.model.Jogador;

public class TelaPrincipal extends JFrame {
    // Variaveis uteis

    private QuizAPI quizAPI;
    private JTextField txtNickname;
    private JTextArea areaTutorial;
    private JButton btnIniciar;
    private JButton btnConfig;
    private JTable tabelaRanking;
    private int qpSelecionado = 10;

    public TelaPrincipal() {
        // Configurações da janela
        setTitle("Quiz POO1 - 2025");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Layout livre para posicionamento absoluto
        setLocationRelativeTo(null); // Centraliza no monitor
        getContentPane().setBackground(new Color(43, 45, 48)); // Cor de fundo do IntelliJ

        //Título do Quiz
        JLabel titulo = new JLabel("QUIZ POO1 - 2025", SwingConstants.CENTER);
        titulo.setBounds(100, 20, 400, 40);
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE);
        add(titulo);

        //Campo do Nome
        JLabel lblNick = new JLabel("Seu nome:");
        lblNick.setBounds(125, 90, 150, 25);
        lblNick.setForeground(Color.WHITE);
        add(lblNick);

        //texto do campo nome

        txtNickname = new JTextField();
        txtNickname.setBounds(200, 90, 200, 30);
        txtNickname.setBackground(Color.gray);
        txtNickname.setForeground(Color.RED);
        txtNickname.setCaretColor(Color.red);
        add(txtNickname);

        //Área do Tutorial e Regras
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

        //Botão Iniciar Quiz
        btnIniciar = new JButton("INICIAR QUIZ");
        btnIniciar.setBounds(225, 310, 150, 45);
        btnIniciar.setBackground(new Color(75, 110, 175));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFocusPainted(false);
        add(btnIniciar);

        //Botão Configurações
        btnConfig = new JButton("Configurações");
        btnConfig.setBounds(400, 320, 150, 25);
        add(btnConfig);
        btnConfig.addActionListener(e -> {
            //Cria o objeto da tela de configuração
            TelaConfig config = new TelaConfig(this);

            //Faz o objeto aparecer na tela
            config.setVisible(true);

            this.qpSelecionado = config.getQuantidadePerguntas();
        });

        //Ranking
        JLabel lblRanking = new JLabel("RANKING DOS TOP DOS TOP", SwingConstants.CENTER);
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

        // Lógica do Botão Iniciar
        btnIniciar.addActionListener(e -> {
            String nick = txtNickname.getText().trim();

            if (nick.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Erro: Digite seu Nickname antes de iniciar!");
            } else {
                try {
                    //API que recebe o arquivo csv
                    QuizAPI api = new QuizAPI("efeito-estufa.csv");

                    //isso aficiona o a quantidade de perguntas escolhida em configuraçoes
                    api.setQuantidadePerguntas(qpSelecionado);
                    api.iniciarJogo(nick);

                    //isso cria a tela do jogo passando os dados da api pra ela
                    TelaDoJogo telaJogo = new TelaDoJogo(api);

                    telaJogo.setVisible(true);
                    this.dispose();

                } catch (Exception ex) {
                    //se o arquivo não exista ou o nick seja inválido
                    JOptionPane.showMessageDialog(this, "Erro ao iniciar quiz: " + ex.getMessage());
                }
            }
        });

        //sistema de pisca pisca
        Timer pisca = new Timer(500, e -> {
            if(areaTutorial.getBackground().equals(Color.blue)){
                areaTutorial.setBackground(Color.red);
            }else{
                areaTutorial.setBackground(Color.blue);
            }
        });

        pisca.start();
        //negocio pra atualizar o ranking
        try {
            QuizAPI apiParaRanking = new QuizAPI("efeito-estufa.csv");
            atualizarTabelaRanking(apiParaRanking);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: não foi possivel carregar o ranking");
        }

    }

    public void atualizarTabelaRanking(QuizAPI quizAPI) {
        try {
            List<Jogador> top3 = quizAPI.getTop3Ranking(); //lista dos brabo

            Object[][] dadosRanking = new Object[3][3];

            //Loop para preencher a matriz com os dados dos Jogadores
            for (int i = 0; i < 3; i++) {
                if (i < top3.size()) {
                    Jogador j = top3.get(i);
                    dadosRanking[i][0] = (i + 1) + "º"; // Posição
                    dadosRanking[i][1] = j.getNome();   // Nome
                    dadosRanking[i][2] = j.getPontuacao(); // Pontuação
                } else {
                    //se não tiver ninguem prenche tudo vaziu
                    dadosRanking[i][0] = (i + 1) + "º";
                    dadosRanking[i][1] = "---";
                    dadosRanking[i][2] = "0";
                }
            }

            //coloca os dados na tabela que já existe na tela
            String[] colunas = {"Posição", "Jogador", "Pontuação"};
            tabelaRanking.setModel(new javax.swing.table.DefaultTableModel(dadosRanking, colunas));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: não foi possivel carregar o ranking");
        }
    }

    public static void main(String[] args) {
        // Garante que a interface rode na thread correta do Swing
        SwingUtilities.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });
    }
}