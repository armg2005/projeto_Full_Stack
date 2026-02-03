package backend.api;

import backend.model.Questao;
import backend.persistence.GerenciadorArquivos;
import backend.model.Jogador;
import backend.persistence.GerenciadorRanking;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizAPI {
    private List<Questao> bancoTodasQuestoes;
    private List<Questao> questoesDaRodada;
    private int pontuacaoTotal;
    private String nickname;
    private int quantidadePerguntas = 10;


    public QuizAPI(String caminhoArquivo) throws Exception {
        GerenciadorArquivos gerenciador = new GerenciadorArquivos();
        this.bancoTodasQuestoes = gerenciador.lerQuestoes(caminhoArquivo);
        this.pontuacaoTotal = 0;
    }


    public void setQuantidadePerguntas(int qp) throws Exception {
        if (qp < 5 || qp > 20) {
            throw new Exception("A quantidade de perguntas deve ser entre 5 e 20.");
        }
        this.quantidadePerguntas = qp;
    }

    public int getQuantidadePerguntas() {
        return quantidadePerguntas;
    }

    public void iniciarJogo(String nickname) throws Exception {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new Exception("É necessário informar um Nickname para começar.");
        }

        if (bancoTodasQuestoes.size() < quantidadePerguntas) {
            throw new Exception("Não há perguntas suficientes no arquivo para iniciar o quiz com " + quantidadePerguntas + " questões.");
        }

        this.nickname = nickname;
        this.pontuacaoTotal = 0;
        this.questoesDaRodada = new ArrayList<>(bancoTodasQuestoes);
        // Sorteia aleatoriamente (embaralha a lista)
        Collections.shuffle(this.questoesDaRodada);
        this.questoesDaRodada = this.questoesDaRodada.subList(0, quantidadePerguntas);
    }

    public Questao getProximaQuestao() {
        if (questoesDaRodada.isEmpty()) {
            return null;
        }
        // Remove e retorna a primeira questão da lista atual
        return questoesDaRodada.remove(0);
    }

    public boolean responderQuestao(Questao questao, boolean respostaUsuario, int segundos) {
        if (segundos > 60) return false;
        //evita dividir 0
        if (segundos < 1) segundos = 1;

        if (respostaUsuario == questao.isResposta()) {
            // Aplica a fórmula: 1200 * nd / (s * qp)
            int nd = questao.getNivelNumerico();
            int s = segundos;
            int qp = this.quantidadePerguntas;
            int pontosCalculados = (1200 * nd) / (s * qp);

            this.pontuacaoTotal += pontosCalculados;
            return true;
        }

        return false;
    }

    public int getPontuacaoTotal() {
        return pontuacaoTotal;
    }

    public String getNickname() {
        return nickname;
    }
    /**
     * CORRETO: Agora usa a classe Jogador do pacote model
     */
    public List<Jogador> getTop3Ranking() throws Exception {
        GerenciadorRanking ranking = new GerenciadorRanking();
        return ranking.getTop3(); // O método getTop3 já retorna List<Jogador>
    }

    /**
     * Este aqui estava certo, mas certifique-se de que o método salvarPontuacao
     * lá no GerenciadorRanking aceita (String, int).
     */
    public void salvarRanking() throws Exception {
        GerenciadorRanking ranking = new GerenciadorRanking();
        ranking.salvarPontuacao(this.nickname, this.pontuacaoTotal);
    }
}