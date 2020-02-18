import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class Main {
    private ArrayList<Categoria> categorias;
    private ArrayList<Locadora> locadoras;

    public static void main(String[] args) {
        Main main = new Main();

        if(args.length > 0 && args[0] != null){
            main.lerArquivoEntrada(args[0]);
        }
        else{
            main.lerArquivoEntrada("Entrada.txt");
        }

    }

    public Main(){
        readCategorias();
        readLocadoras();
        readCarros();
    }

    public void lerArquivoEntrada(String file){
        try{
            FileReader fr = new FileReader(new File(file));
            BufferedReader br = new BufferedReader(fr);

            while(br.ready()){
                String linha = br.readLine();
                Object[] pair = this.resolveInstancia(linha);
                Locadora l = (Locadora) pair[0];
                Carro c = (Carro) pair[1];
                if(pair != null){
                    System.out.println(c.getDescricao() + ": " + l.getNome());
                }
            }
        }
        catch (FileNotFoundException ex){
            System.err.println("Arquivo de entrada não encontrado");
        }
        catch (IOException ex){
            System.err.println("Erro de I/O");
        }
    }

    public Object[] resolveInstancia(String instancia){
        String[] arr = instancia.split(":");
        String key = "";
        String[] datas = arr[2].split(",");

        if(arr[2].contains("sab") || arr[2].contains("dom")){
            key += "FimDeSemana";
        }
        else{
            key += "DiaSemana";
        }

        if(arr[0].equals("Normal")){
            key += "Regular";
        }
        else if(arr[0].equals("Premium")){
            key += "Premium";
        }

        ArrayList<Categoria> combCategoria = combinar(Integer.parseInt(arr[1].replace(" ", "")));
        Locadora locadoraBarata = null;
        Carro carroResultado = null;

        for (Categoria c: combCategoria) {
            for(Locadora l: locadoras){
                if(l.getCategoria() == c){
                    if(locadoraBarata == null){
                        locadoraBarata = l;
                    }
                    if(l.getCarros().size() > 0){
                        for(Carro carro: l.getCarros()){

                            //System.out.println(carro.getDescricao() + " => " + carro.getCategoria().getLimiteDePessoas());
                            boolean carroValido = true;
                            if(carro.getDiasAlugados() != null){
                                for(String diasAlugados: carro.getDiasAlugados()){
                                    for(String dia: datas){
                                        if(diasAlugados.equals(dia)){
                                            carroValido = false;
                                        }
                                    }
                                }
                            }
                            if(carroValido
                                &&
                                (c.getLimiteDePessoas() <= locadoraBarata.getCategoria().getLimiteDePessoas() ||
                                l.getTabelaDePrecos().get(key) <= locadoraBarata.getTabelaDePrecos().get(key))
                            ){
                                //System.out.println("CAIU NO IF");
                                locadoraBarata = l;
                                carroResultado = carro;
                            }

                        }
                    }
                    else{
                        System.err.println("Frota de carros vazia");
                    }
                }
            }
        }
        if(carroResultado != null){
            //System.out.println(carroResultado.getCategoria().getLimiteDePessoas());
            //System.out.println(locadoraBarata.getCarros().contains(carroResultado));
            for(String dia: datas){
                carroResultado.getDiasAlugados().add(dia);
            }
            return new Object[]{locadoraBarata, carroResultado};
            //System.out.println(carroResultado.getDescricao() + ": " + locadoraBarata.getNome());
        }
        else{
            System.out.println("Não existe carro disponivel que atenda aos requisitos");
        }
        return null;
    }

    public ArrayList<Categoria> combinar(int limPessoas){
        ArrayList<Categoria> combinacoes = new ArrayList<Categoria>();
        /***
         * Primeiro o algoritmo deve procurar as categorias que são
         * um match para o numero de pessoas em questão
         */
        for (Categoria c: categorias) {
            if(c.getLimiteDePessoas() >= limPessoas){
                //System.out.println(c.getLimiteDePessoas());
                combinacoes.add(c);
            }
        }
        return combinacoes;
    }

    public void readCarros(){
        FileReader fileReader = null;
        BufferedReader br = null;

        try{
            fileReader = new FileReader(this.getFileFromResources("Carros.txt"));
            br = new BufferedReader(fileReader);

            while(br.ready()){
                String data = br.readLine();
                String[] arr = data.split(" ");

                Carro c = new Carro();
                c.setDescricao(arr[0]);
                c.setCategoria(filterCategoria(arr[1]));
//                System.out.println(c.getDescricao());
                /***
                 * Filtra a locadora pela descrição fornecida e associa o carro a essa locadora
                 * em uma relação de muitos para muitos.
                 */
                Locadora l = this.filterLocadora(arr[2]);
                l.getCarros().add(c);
            }
        } catch (IOException ex){
            System.err.println(ex.getMessage());
        }
        finally {
            if(br != null){
                try{
                    br.close();
                    fileReader.close();
                }
                catch (IOException ex){
                    System.err.println(ex.getMessage());
                }
            }
        }
    }

    public void readLocadoras(){
        locadoras = new ArrayList<Locadora>();
        FileReader fileReader = null;
        BufferedReader br = null;
        try{
            fileReader = new FileReader(this.getFileFromResources("Locadoras.txt"));
            br = new BufferedReader(fileReader);

            // The first line is a comment defining the header of the text file
            br.readLine();
            while(br.ready()){
                String data = br.readLine();
                String[] arr = data.split(" ");
                Locadora l = new Locadora(arr[0], filterCategoria(arr[1]));
                l.addPreco("DiaSemanaRegular", Float.parseFloat(arr[2]));
                l.addPreco("DiaSemanaPremium", Float.parseFloat(arr[3]));
                l.addPreco("FimDeSemanaRegular", Float.parseFloat(arr[4]));
                l.addPreco("FimDeSemanaPremium", Float.parseFloat(arr[5]));
                locadoras.add(l);
            }
        } catch (IOException ex){
            System.err.println(ex.getMessage());
        }
        finally {
            if(br != null){
                try{
                    br.close();
                    fileReader.close();
                }
                catch (IOException ex){
                    System.err.println(ex.getMessage());
                }
            }
        }
    }

    public Locadora filterLocadora(String s) {
        Locadora locadora = null;
        for (Locadora l: locadoras) {
            if(l.getNome().equals(s)){
                locadora = l;
            }
        }
        return locadora;
    }

    public Categoria filterCategoria(String s) {
        Categoria categoria = null;
        for (Categoria c: categorias) {
            if(c.getDescricao().equals(s)){
                categoria = c;
            }
        }
        return categoria;
    }

    public void readCategorias(){
        categorias = new ArrayList<Categoria>();
        try{
            FileReader fileReader = new FileReader(this.getFileFromResources("Categorias.txt"));
            BufferedReader br = new BufferedReader(fileReader);

            while(br.ready()){
                String data = br.readLine();
                String[] arr = data.split(" ");
                Categoria c = new Categoria(arr[0], Integer.parseInt(arr[1]));
                categorias.add(c);
            }
        } catch (IOException ex){
            System.err.println(ex.getMessage());
        }
    }

    private File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }

    public int getLocadorasSize() {
        return locadoras.size();
    }

    public int getCategoriasSize(){
        return categorias.size();
    }

    public int getCarrosSize(){
        int size = 0;
        for (Locadora l: locadoras) {
            size += l.getCarros().size();
        }
        return size;
    }
}
