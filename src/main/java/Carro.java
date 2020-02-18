import java.util.ArrayList;

public class Carro {
    private String descricao;
    private ArrayList<String> diasAlugados;
    private Categoria categoria;

    public Carro(String descricao, Categoria categoria) {
        this.descricao = descricao;
        this.diasAlugados = new ArrayList<String>();
        this.categoria = categoria;
    }

    public Carro() {
        diasAlugados = new ArrayList<String>();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ArrayList<String> getDiasAlugados() {
        return diasAlugados;
    }

    public void setDiasAlugados(ArrayList<String> diasAlugados) {
        this.diasAlugados = diasAlugados;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
