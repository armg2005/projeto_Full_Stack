package backend.model;

public class Jogador implements Comparable<Jogador> {
    private String nome;
    private int pontuacao;
    private String dataJogo;

    public Jogador(String nome, int pontuacao, String dataJogo) {
        this.nome = nome;
        this.pontuacao = pontuacao;
        this.dataJogo = dataJogo;
    }


    public String getNome() { return nome; }
    public int getPontuacao() { return pontuacao; }
    public String getDataJogo() { return dataJogo; }

    // Implementação do Comparable para ordenar do MAIOR para o MENOR
    @Override
    public int compareTo(Jogador outro) {
        return Integer.compare(outro.pontuacao, this.pontuacao);
    }

    @Override
    public String toString() {
        return nome + " - " + pontuacao + " pts - " + dataJogo;
    }
}