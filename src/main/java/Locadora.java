import java.util.ArrayList;
import java.util.HashMap;

public class Locadora {
    private String nome;
    private HashMap<String, Float> tabelaDePrecos;
    private Categoria categoria;
    private ArrayList<Carro> carros;

    public Locadora(String nome, Categoria categoria) {
        this.nome = nome;
        this.tabelaDePrecos = new HashMap<String, Float>();
        this.categoria = categoria;
        this.carros = new ArrayList<Carro>();
    }

    public Locadora() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public HashMap<String, Float> getTabelaDePrecos() {
        return tabelaDePrecos;
    }

    public void setTabelaDePrecos(HashMap<String, Float> tabelaDePrecos) {
        this.tabelaDePrecos = tabelaDePrecos;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public ArrayList<Carro> getCarros() {
        return carros;
    }

    public void setCarros(ArrayList<Carro> carros) {
        this.carros = carros;
    }

    public void addPreco(String key, float valor){
        this.tabelaDePrecos.put(key, valor);
    }
}
