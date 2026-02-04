/**
 * Autores: Alisson Ricady e Gustavo Rodrigues
 */

package backend.model;

public class Questao {
    private int id;
    private String frase;
    private int categoria;
    private boolean resposta;
    private String nivelDificuldade;


    public Questao(int id, String frase, int categoria, boolean resposta, String nivelDificuldade) {
        this.id = id;
        this.frase = frase;
        this.categoria = categoria;
        this.resposta = resposta;
        this.nivelDificuldade = nivelDificuldade;
    }


    public int getId() { return id; }
    public String getFrase() { return frase; }
    public int getCategoria() { return categoria; }
    public boolean isResposta() { return resposta; }
    public String getNivelDificuldade() { return nivelDificuldade; }


    public int getNivelNumerico() {
        if (this.nivelDificuldade == null) {
            throw new IllegalArgumentException("O nível de dificuldade não pode ser nulo.");
        }

        switch (this.nivelDificuldade.toUpperCase()) {
            case "F":
                return 1;
            case "M":
                return 2;
            case "D":
                return 3;
            default:
                throw new IllegalArgumentException("Nível de dificuldade inválido encontrado: " + this.nivelDificuldade + ". Esperado: F, M ou D.");
        }
    }

    @Override
    public String toString() {
        return "Questão " + id + ": " + frase + " (" + nivelDificuldade + ")";
    }
}