package frontend;

import javax.swing.*;
import java.awt.*;

public class TelaFinal extends JFrame {
    public TelaFinal(String nick, int pontos) {
        setTitle("Resultado Final");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(43, 45, 48));

        JLabel lblParabens = new JLabel("Parabéns, " + nick + "!", SwingConstants.CENTER);
        lblParabens.setBounds(50, 50, 400, 30);
        lblParabens.setForeground(Color.WHITE);
        lblParabens.setFont(new Font("Arial", Font.BOLD, 22));
        add(lblParabens);

        JLabel lblPontos = new JLabel("Sua Pontuação: " + pontos, SwingConstants.CENTER);
        lblPontos.setBounds(50, 120, 400, 40);
        lblPontos.setForeground(Color.GREEN);
        lblPontos.setFont(new Font("Arial", Font.BOLD, 28));
        add(lblPontos);

        JButton btnVoltar = new JButton("Voltar ao Início");
        btnVoltar.setBounds(150, 250, 200, 40);
        btnVoltar.addActionListener(e -> {
            new TelaPrincipal().setVisible(true);
            this.dispose();
        });
        add(btnVoltar);
    }
}
