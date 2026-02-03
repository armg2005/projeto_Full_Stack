package frontend;

import javax.swing.*;
import java.awt.*;

public class TelaDoJogo extends JFrame {
    private JLabel lblTimer, lblPergunta, lblContador;
    private JButton[] btnOpcoes = new JButton[4];
    private int tempoRestante = 60;
    private int perguntaAtual = 1;
    private int totalPerguntas;
    private Timer cronometro;

    public TelaDoJogo(int qpSelecionado) {
        this.totalPerguntas = qpSelecionado; // Recebe o valor da TelaConfig

        setTitle("Quiz em Andamento - 2025");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(43, 45, 48));

        // 1. Contador de Perguntas (Ex: 1/10)
        lblContador = new JLabel("Pergunta: " + perguntaAtual + "/" + totalPerguntas);
        lblContador.setBounds(30, 20, 150, 30);
        lblContador.setForeground(Color.LIGHT_GRAY);
        add(lblContador);

        // 2. Cronômetro de 60s (Regra 1.4)
        lblTimer = new JLabel("Tempo: 60s", SwingConstants.RIGHT);
        lblTimer.setBounds(450, 20, 120, 30);
        lblTimer.setForeground(Color.RED);
        lblTimer.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTimer);

        // 3. Área da Pergunta (Regra 1.2)
        lblPergunta = new JLabel("<html><div style='text-align: center;'>Carregando pergunta do back-end...</div></html>", SwingConstants.CENTER);
        lblPergunta.setBounds(50, 80, 500, 100);
        lblPergunta.setForeground(Color.WHITE);
        lblPergunta.setFont(new Font("Arial", Font.PLAIN, 20));
        add(lblPergunta);

        // 4. Botões de Opções (Layout Fixo)
        for (int i = 0; i < 4; i++) {
            btnOpcoes[i] = new JButton("Opção " + (char)('A' + i));
            btnOpcoes[i].setBounds(100, 200 + (i * 60), 400, 45);
            btnOpcoes[i].setFocusPainted(false);

            // Evento ao clicar na resposta
            btnOpcoes[i].addActionListener(e -> proximaPergunta());
            add(btnOpcoes[i]);
        }

        iniciarCronometro();
    }

    private void iniciarCronometro() {
        if (cronometro != null) cronometro.stop();

        tempoRestante = 60; // Reinicia para 60 segundos
        lblTimer.setText("Tempo: 60s");
        lblTimer.setForeground(Color.RED);

        cronometro = new Timer(1000, e -> {
            tempoRestante--;
            lblTimer.setText("Tempo: " + tempoRestante + "s");

            if (tempoRestante <= 10) {
                // Efeito visual: pisca ou muda cor quando está acabando
                lblTimer.setForeground(tempoRestante % 2 == 0 ? Color.YELLOW : Color.RED);
            }

            if (tempoRestante <= 0) {
                proximaPergunta(); // Pula se o tempo acabar (Regra 1.4)
            }
        });
        cronometro.start();
    }

    private void proximaPergunta() {
        if (perguntaAtual < totalPerguntas) {
            perguntaAtual++;
            lblContador.setText("Pergunta: " + perguntaAtual + "/" + totalPerguntas);
            // Aqui você daria o 'refresh' no texto da lblPergunta com o back-end
            iniciarCronometro();
        } else {
            finalizarQuiz();
        }
    }

    private void finalizarQuiz() {
        cronometro.stop();
        JOptionPane.showMessageDialog(this, "Quiz concluído! Indo para os resultados.");
        // Navega para a tela final (Passe o nickname e pontos aqui depois)
        // new TelaFinal("Nome", pontos).setVisible(true);
        this.dispose();
    }
}

