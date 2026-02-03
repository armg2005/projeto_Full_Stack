package backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GerenciadorArquivos {

    public List<Questao> lerQuestoes(String nomeArquivo) throws Exception {
        List<Questao> list = new ArrayList<>();
        File arquivo = new File(nomeArquivo);

        if (!arquivo.exists()) {
            throw new Exception("Arquivo " + nomeArquivo + " não foi encontrado!");
        }

        try (Scanner sc = new Scanner(arquivo)) {

            while (sc.hasNext()) {
                String line = sc.nextLine();

                if (line.trim().isEmpty()) continue;

                String[] data = line.split(";");

                if (data.length < 5) {
                    continue;
                }

                try {
                    int id = Integer.parseInt(data[0].trim());
                    String question = data[1].trim();
                    int category = Integer.parseInt(data[2].trim());
                    boolean answer = data[3].trim().equalsIgnoreCase("V");
                    String level = data[4].trim();
                    Questao quiz = new Questao(id, question, category, answer, level);
                    list.add(quiz);

                } catch (NumberFormatException e) {
                    throw new Exception("Erro ao converter número na linha: " + line);
                }
            }
        } catch (FileNotFoundException ex) {
            throw new Exception("Erro de leitura: " + ex.getMessage());
        }

        return list;
    }
}