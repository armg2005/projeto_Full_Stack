/**
 * Autores: Alisson Ricady e Gustavo Rodrigues
 */

package test;

import backend.api.QuizAPI;
import backend.model.Jogador;
import backend.model.Questao;

import java.util.List;
import java.util.Scanner;

public class TesteConsole {

    public static void main(String[] args) {
        System.out.println("--- INICIANDO TESTE DO QUIZ (COM RANKING) ---");


        String nomeArquivo = "efeito-estufa.csv";

        try {
            QuizAPI api = new QuizAPI(nomeArquivo);
            System.out.println("Arquivo carregado com sucesso!");

            api.setQuantidadePerguntas(5);

            Scanner scanner = new Scanner(System.in);
            System.out.print("Digite seu Nickname: ");
            String nick = scanner.nextLine();

            api.iniciarJogo(nick);

            Questao questaoAtual;
            while ((questaoAtual = api.getProximaQuestao()) != null) {
                System.out.println("\nPERGUNTA: " + questaoAtual.getFrase());
                System.out.println("Nível: " + questaoAtual.getNivelDificuldade());
                System.out.print("Sua resposta (V ou F): ");

                String respostaTexto = scanner.nextLine();
                boolean respostaBoolean = respostaTexto.equalsIgnoreCase("V");

                int tempoGasto = 5;

                boolean acertou = api.responderQuestao(questaoAtual, respostaBoolean, tempoGasto);

                if (acertou) {
                    System.out.println(">> ACERTOU! Pontos acumulados: " + api.getPontuacaoTotal());
                } else {
                    System.out.println(">> ERROU!");
                }
            }

            // 5. Fim do Jogo e Teste do Ranking
            System.out.println("\n-------------------------------------------------");
            System.out.println("FIM DO JOGO!");
            System.out.println("Pontuação Final de " + api.getNickname() + ": " + api.getPontuacaoTotal());

            System.out.println("\n>>> Salvando no Ranking... <<<");

            // Chama o método da API que usa o GerenciadorRanking
            api.salvarRanking();
            System.out.println("Sucesso! Verifique se criou o arquivo 'ranking.txt' na pasta do projeto.");

            System.out.println("\n--- TOP 3 MELHORES JOGADORES ---");
            List<Jogador> top3 = api.getTop3Ranking();

            if (top3.isEmpty()) {
                System.out.println("O ranking ainda está vazio.");
            } else {
                for (int i = 0; i < top3.size(); i++) {
                    // Aqui o método toString() do Jogador é chamado automaticamente
                    System.out.println((i + 1) + "º Lugar: " + top3.get(i));
                }
            }

        } catch (Exception e) {
            System.out.println("ERRO NO TESTE: " + e.getMessage());
            e.printStackTrace();
        }
    }
}