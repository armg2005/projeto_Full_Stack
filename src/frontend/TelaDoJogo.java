/**
 * Autores: Alisson Ricady e Gustavo Rodrigues
 */
package frontend;

import backend.api.QuizAPI;
import backend.model.Questao; // Importação necessária para a variável questaoAtual
import javax.swing.*;
import java.awt.*;

public class TelaDoJogo extends JFrame {
    private QuizAPI quizAPI;
    private Questao questaoAtual; // CORREÇÃO 1: Declarar a questão atual
    private JLabel lblTimer, lblPergunta, lblContador;
    private JButton[] btnOpcoes = new JButton[4];
    private int tempoRestante = 60;
    private int perguntaAtual = 1;
    private Timer cronometro;

    // CORREÇÃO 2: Construtor agora recebe a QuizAPI
    public TelaDoJogo(QuizAPI api) {
        this.quizAPI = api;

        setTitle("Quiz em Andamento - 2025");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(43, 45, 48));

        // 1. Contador de Perguntas
        lblContador = new JLabel("Pergunta: " + perguntaAtual + "/" + quizAPI.getQuantidadePerguntas());
        lblContador.setBounds(30, 20, 150, 30);
        lblContador.setForeground(Color.LIGHT_GRAY);
        add(lblContador);

        // 2. Cronômetro
        lblTimer = new JLabel("Tempo: 60s", SwingConstants.RIGHT);
        lblTimer.setBounds(450, 20, 120, 30);
        lblTimer.setForeground(Color.RED);
        lblTimer.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTimer);

        // 3. Área da Pergunta
        lblPergunta = new JLabel("", SwingConstants.CENTER);
        lblPergunta.setBounds(50, 80, 500, 100);
        lblPergunta.setForeground(Color.WHITE);
        lblPergunta.setFont(new Font("Arial", Font.PLAIN, 20));
        add(lblPergunta);

        // 4. Botões de Opções (Como o CSV é V ou F, usaremos apenas 2 botões)
        for (int i = 0; i < 4; i++) {
            btnOpcoes[i] = new JButton();
            btnOpcoes[i].setBounds(100, 200 + (i * 60), 400, 45);
            btnOpcoes[i].setFocusPainted(false);
            add(btnOpcoes[i]);
        }

        // Configura a lógica dos botões para V ou F
        btnOpcoes[0].setText("VERDADEIRO");
        btnOpcoes[0].addActionListener(e -> processarResposta(true));

        btnOpcoes[1].setText("FALSO");
        btnOpcoes[1].addActionListener(e -> processarResposta(false));

        // Esconde os outros 2 botões já que o arquivo é V/F
        btnOpcoes[2].setVisible(false);
        btnOpcoes[3].setVisible(false);

        proximaPergunta(); // Inicia a primeira pergunta
    }

    private void iniciarCronometro() {
        if (cronometro != null) cronometro.stop();
        tempoRestante = 60;
        lblTimer.setText("Tempo: 60s");
        lblTimer.setForeground(Color.RED);

        cronometro = new Timer(1000, e -> {
            tempoRestante--;
            lblTimer.setText("Tempo: " + tempoRestante + "s");
            if (tempoRestante <= 10) lblTimer.setForeground(tempoRestante % 2 == 0 ? Color.YELLOW : Color.RED);
            if (tempoRestante <= 0) processarResposta(false); // Se o tempo acabar, conta como erro
        });
        cronometro.start();
    }

    private void processarResposta(boolean respostaUsuario) {
        if (this.questaoAtual == null) {
            return;
        }

        if (cronometro != null) cronometro.stop();

        int tempoGasto = 60 - tempoRestante;

        // Agora é seguro chamar a API
        quizAPI.responderQuestao(questaoAtual, respostaUsuario, tempoGasto);

        perguntaAtual++;
        proximaPergunta();
    }

    private void proximaPergunta() {
        this.questaoAtual = quizAPI.getProximaQuestao();

        if (questaoAtual != null) {
            lblContador.setText("Pergunta: " + perguntaAtual + "/" + quizAPI.getQuantidadePerguntas());
            lblPergunta.setText("<html><center>" + questaoAtual.getFrase() + "</center></html>");
            iniciarCronometro();
        } else {
            finalizarQuiz();
        }
    }

    private void finalizarQuiz() {
        if (cronometro != null) cronometro.stop();
        try{
            quizAPI.salvarRanking();
        }catch (Exception e){
            System.out.println("erro no salvamento");;
        }
        // CORREÇÃO: Chama a TelaFinal passando a API antes de fechar a tela atual
        TelaFinal telaFinal = new TelaFinal(this.quizAPI);
        telaFinal.setVisible(true);

        this.dispose(); // Agora o programa não fecha porque a TelaFinal está aberta
    }
}