package backend.model;

public class Frase {
    private int id;
    private String frase;

    public Frase(int id, String frase){
        this.id = id;
        this.frase = frase;
    }

    public int getId() {
        return this.id;
    }

    public String getrase() {
        return this.frase;
    }

    @Override

    public String toString(){
        return "id -->: " + id + "\nfrase --> " + frase;
    }
}
