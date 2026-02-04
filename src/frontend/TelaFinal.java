package frontend;

import backend.api.QuizAPI;
import javax.swing.*;
import java.awt.*;

public class TelaFinal extends JFrame {
    public TelaFinal(QuizAPI api) {
        // 1. Configurações da Janela
        setTitle("Resultado Final");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(43, 45, 48));

        // 2. Título
        JLabel lblParabens = new JLabel("QUIZ FINALIZADO!", SwingConstants.CENTER);
        lblParabens.setBounds(50, 30, 300, 30);
        lblParabens.setForeground(Color.ORANGE);
        lblParabens.setFont(new Font("Arial", Font.BOLD, 22));
        add(lblParabens);

        // 3. Resultado (Certifique-se que getNickname e getPontuacaoTotal existem na API)
        JLabel lblResultado = new JLabel("<html><center>" + api.getNickname() + "<br>Pontuação: " + api.getPontuacaoTotal() + "</center></html>", SwingConstants.CENTER);
        lblResultado.setBounds(50, 80, 300, 80);
        lblResultado.setForeground(Color.WHITE);
        lblResultado.setFont(new Font("Arial", Font.PLAIN, 18));
        add(lblResultado);

        // 4. Botão Voltar (AQUI ESTÁ O SEGREDO DO RANKING)
        JButton btnVoltar = new JButton("VOLTAR AO INÍCIO");
        btnVoltar.setBounds(100, 200, 200, 40);
        btnVoltar.addActionListener(e -> {
            // Abre uma nova TelaPrincipal que carregará o Ranking atualizado
            new TelaPrincipal().setVisible(true);
            this.dispose();
        });
        add(btnVoltar);

        // IMPORTANTE: NÃO PODE TER "new TelaFinal(api)" AQUI DENTRO!
    }
}