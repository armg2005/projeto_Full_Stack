package backend.persistence;

import backend.model.Jogador;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GerenciadorRanking {
    private static final String ARQUIVO_RANKING = "ranking.txt";

    /**
     * Salva a pontuação do jogador no arquivo.
     */
    public void salvarPontuacao(String nome, int pontuacao) throws IOException {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataFormatada = agora.format(formatador);

        // Append = true para não apagar o arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_RANKING, true))) {
            writer.write(nome + ";" + pontuacao + ";" + dataFormatada);
            writer.newLine();
        }
    }

    /**
     * Lê o arquivo e retorna a lista de Jogadores ordenada.
     */
    public List<Jogador> getTop3() throws IOException {
        List<Jogador> listaJogadores = new ArrayList<>();
        File arquivo = new File(ARQUIVO_RANKING);

        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] partes = linha.split(";");
                if (partes.length >= 3) {
                    try {
                        String nome = partes[0];
                        int pontos = Integer.parseInt(partes[1]);
                        String data = partes[2];

                        listaJogadores.add(new Jogador(nome, pontos, data));

                    } catch (NumberFormatException e) {

                    }
                }
            }
        }

        // Ordena usando o compareTo da classe Jogador
        Collections.sort(listaJogadores);

        // Retorna apenas os 3 primeiros (Top 3)
        int limite = Math.min(3, listaJogadores.size());
        return listaJogadores.subList(0, limite);
    }
}