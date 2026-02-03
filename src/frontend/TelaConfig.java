package frontend;

import javax.swing.*;
import java.awt.*;

public class TelaConfig extends JDialog {
    private JSpinner spinnerQp;
    private JButton btnSalvar;
    private int quantidadePerguntas = 10; // Valor padrão

    public TelaConfig(Frame parent) {
        super(parent, "Configurações do Quiz", true); // true para ser modal (trava a tela de trás)
        setSize(300, 200);
        setLayout(null);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(new Color(60, 63, 65));

        JLabel lblInfo = new JLabel("Quantidade de Perguntas (qp):", SwingConstants.CENTER);
        lblInfo.setBounds(20, 30, 260, 25);
        lblInfo.setForeground(Color.WHITE);
        add(lblInfo);

        // Spinner configurado entre 5 e 20 (Regra do Projeto)
        SpinnerModel model = new SpinnerNumberModel(10, 5, 20, 1);
        spinnerQp = new JSpinner(model);
        spinnerQp.setBounds(110, 70, 80, 30);
        add(spinnerQp);

        btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(100, 120, 100, 30);
        btnSalvar.addActionListener(e -> {
            quantidadePerguntas = (int) spinnerQp.getValue();
            setVisible(false);
        });
        add(btnSalvar);
    }

    public int getQuantidadePerguntas() {
        return quantidadePerguntas;
    }
}
