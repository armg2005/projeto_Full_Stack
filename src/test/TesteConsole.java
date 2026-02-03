package test;

import backend.Questao;
import backend.QuizAPI;

import java.util.Scanner;

public class TesteConsole {

    public static void main(String[] args) {
        System.out.println("--- INICIANDO TESTE DO QUIZ ---");
        String nomeArquivo = "efeito-estufa.csv";

        try {
            QuizAPI api = new QuizAPI(nomeArquivo);
            System.out.println("Arquivo carregado com sucesso!");

            // 2. Configurações iniciais
            api.setQuantidadePerguntas(5); // Vamos testar com apenas 5 perguntas

            // 3. Inicia o jogo com um Nickname
            System.out.print("Digite seu Nickname para começar: ");
            Scanner scanner = new Scanner(System.in);
            String nick = scanner.nextLine();

            api.iniciarJogo(nick);
            System.out.println("\nJogo iniciado para o jogador: " + api.getNickname());
            System.out.println("-------------------------------------------------");

            // 4. Loop do jogo: Pega perguntas uma a uma até acabar
            Questao questaoAtual;
            while ((questaoAtual = api.getProximaQuestao()) != null) {
                // Mostra a pergunta
                System.out.println("PERGUNTA: " + questaoAtual.getFrase());
                System.out.println("Nível: " + questaoAtual.getNivelDificuldade());
                System.out.print("Sua resposta (V ou F): ");

                String respostaTexto = scanner.nextLine();
                boolean respostaBoolean = respostaTexto.equalsIgnoreCase("V");

                int tempoGasto = 10;

                boolean acertou = api.responderQuestao(questaoAtual, respostaBoolean, tempoGasto);

                if (acertou) {
                    System.out.println(">> ACERTOU! Pontuação atual: " + api.getPontuacaoTotal());
                } else {
                    System.out.println(">> ERROU! A resposta correta era: " + (questaoAtual.isResposta() ? "V" : "F"));
                }
                System.out.println("-------------------------------------------------");
            }

            // 5. Fim do jogo
            System.out.println("FIM DO JOGO!");
            System.out.println("Pontuação Final: " + api.getPontuacaoTotal());

        } catch (Exception e) {
            // Aqui capturamos qualquer erro que a API lance (Arquivo não achado, etc)
            System.out.println("ERRO CRÍTICO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}