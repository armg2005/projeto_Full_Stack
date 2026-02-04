/**
 * Autores: Alisson Ricady e Gustavo Rodrigues
 */
package frontend;

import backend.api.QuizAPI;
import backend.model.Questao;
import javax.swing.*;
import java.awt.*;

public class TelaDoJogo extends JFrame {
    private QuizAPI quizAPI;
    private Questao questaoAtual;
    private JLabel lblTimer, lblPergunta, lblContador;
    private JButton[] btnOpcoes = new JButton[4];
    private int tempoRestante = 60;
    private int perguntaAtual = 1;
    private Timer cronometro;

    //construtor da tela do jogo
    public TelaDoJogo(QuizAPI api) {
        this.quizAPI = api;

        setTitle("Quiz em Andamento - 2025");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(43, 45, 48));

        //Contador de Perguntas
        lblContador = new JLabel("Pergunta: " + perguntaAtual + "/" + quizAPI.getQuantidadePerguntas());
        lblContador.setBounds(30, 20, 250, 30);
        lblContador.setForeground(Color.LIGHT_GRAY);
        add(lblContador);

        //Cronômetro
        lblTimer = new JLabel("Tempo: 60s", SwingConstants.RIGHT);
        lblTimer.setBounds(450, 20, 120, 30);
        lblTimer.setForeground(Color.RED);
        lblTimer.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTimer);

        //Perguntas
        lblPergunta = new JLabel("", SwingConstants.CENTER);
        lblPergunta.setBounds(50, 80, 500, 100);
        lblPergunta.setForeground(Color.WHITE);
        lblPergunta.setFont(new Font("Arial", Font.PLAIN, 20));
        add(lblPergunta);

        //Botões de Opções --
        for (int i = 0; i < 2; i++) {
            btnOpcoes[i] = new JButton();
            btnOpcoes[i].setBounds(100, 200 + (i * 60), 400, 45);
            btnOpcoes[i].setFocusPainted(false);
            btnOpcoes[i].setBackground(Color.gray);
            add(btnOpcoes[i]);
        }

        //lógica dos botões para V ou F
        btnOpcoes[0].setText("VERDADEIRO");
        btnOpcoes[0].addActionListener(e -> processarResposta(true));

        btnOpcoes[1].setText("FALSO");
        btnOpcoes[1].addActionListener(e -> processarResposta(false));

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
            if (tempoRestante <= 0) processarResposta(false); // Se o tempo acabar conta como erro
        });
        cronometro.start();
    }

    private void processarResposta(boolean respostaUsuario) {
        if (this.questaoAtual == null) return;

        // 1. Para o relógio principal do jogo
        if (cronometro != null) cronometro.stop();

        int tempoGasto = 60 - tempoRestante;

        btnOpcoes[0].setEnabled(false);
        btnOpcoes[1].setEnabled(false);

        //Mostra quem era a certa e quem era a errada
        if(questaoAtual.isResposta()){
            btnOpcoes[0].setBackground(Color.green); //Verdadeiro era a certa
            btnOpcoes[1].setBackground(Color.red);
        } else {
            btnOpcoes[1].setBackground(Color.green); //Falso era a certa
            btnOpcoes[0].setBackground(Color.red);
        }

        //Timer de transição
        Timer cronometroP = new Timer(1000, e -> {
            //Volta as cores e reativa os botões para a próxima
            btnOpcoes[0].setBackground(Color.gray);
            btnOpcoes[1].setBackground(Color.gray);
            btnOpcoes[0].setEnabled(true);
            btnOpcoes[1].setEnabled(true);

            //Processa os dados na API e vai para a próxima
            quizAPI.responderQuestao(questaoAtual, respostaUsuario, tempoGasto);
            perguntaAtual++;
            proximaPergunta();
        });

        cronometroP.setRepeats(false);
        cronometroP.start();
    }

    private void proximaPergunta() {
        this.questaoAtual = quizAPI.getProximaQuestao();

        if (questaoAtual != null) {
            lblContador.setText("Pergunta: " + perguntaAtual + "/" + quizAPI.getQuantidadePerguntas() +
                    " | dificuldade: " + questaoAtual.getNivelDificuldade());
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
            JOptionPane.showMessageDialog(this, "Erro de salvameno");
        }
        //Chama a TelaFinal passando a API antes de fechar a tela atual
        TelaFinal telaFinal = new TelaFinal(this.quizAPI);
        telaFinal.setVisible(true);

        this.dispose();
    }
}