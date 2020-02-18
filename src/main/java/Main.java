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


        //De acordo com os dados recebidos, cria um key de busca para encontrar o preço na tabela
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

        //Para cada categoria que é uma possivel solução verifica qual a melhor
        for (Categoria c: combCategoria) {
            // Verifica se a locadora combina com a categoria em questão
            for(Locadora l: locadoras){
                if(l.getCategoria() == c){
                    //Se nenhuma locadora foi selecionada, então é a primeira busca e seleciona a locadora
                    if(locadoraBarata == null){
                        locadoraBarata = l;
                    }
                    //Se existem carros nessa locadora então busca o primeiro disponivel
                    if(l.getCarros().size() > 0){
                        for(Carro carro: l.getCarros()){
                            //Se nenhum dia requisitado for igual aos ja existentes entao o aluguel pode ser realizado
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
                            //Se o aluguel for valido e limite pessoas for menor que o ja existente atualiza a locadora e o carro selecionado.
                            if(carroValido
                                &&
                                (c.getLimiteDePessoas() <= locadoraBarata.getCategoria().getLimiteDePessoas() ||
                                l.getTabelaDePrecos().get(key) <= locadoraBarata.getTabelaDePrecos().get(key))
                            ){
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
            //Adiciona os dias requeridos ao carro para que este esteja indisponivel na proxima consulta
            for(String dia: datas){
                carroResultado.getDiasAlugados().add(dia);
            }
            //Retorna uma "tupla" com os objetos encontrados
            return new Object[]{locadoraBarata, carroResultado};
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
        // Fecha o arquivo e o buffer após a execução das operações

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
                /***
                 * A tabela de preços é um Hash que usa uma string e um valor como key e value respectivamente.
                 */
                l.addPreco("DiaSemanaRegular", Float.parseFloat(arr[2]));
                l.addPreco("DiaSemanaPremium", Float.parseFloat(arr[3]));
                l.addPreco("FimDeSemanaRegular", Float.parseFloat(arr[4]));
                l.addPreco("FimDeSemanaPremium", Float.parseFloat(arr[5]));
                locadoras.add(l);
            }
        } catch (IOException ex){
            System.err.println(ex.getMessage());
        }
        // Fecha o arquivo e o buffer após a execução das operações
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

    /***
     * Filtra a cetegoria com base na string de descrição recebida via argumento
     * @param s
     * @return Categoria
     */
    public Categoria filterCategoria(String s) {
        Categoria categoria = null;
        for (Categoria c: categorias) {
            if(c.getDescricao().equals(s)){
                categoria = c;
            }
        }
        return categoria;
    }

    /***
     * Lê as categorias com base no arquivo de dados
     */
    public void readCategorias(){
        categorias = new ArrayList<Categoria>();
        FileReader fileReader = null;
        BufferedReader br = null;

        try{
            //Cria o buffer de leitura do arquivo
            fileReader = new FileReader(this.getFileFromResources("Categorias.txt"));
            br = new BufferedReader(fileReader);

            //Enquanto nao chegar ao fim do arquivo processa as informações
            while(br.ready()){
                String data = br.readLine();
                //Divide os dados da linha
                String[] arr = data.split(" ");
                //Cria nova categoria e adiciona na lista
                Categoria c = new Categoria(arr[0], Integer.parseInt(arr[1]));
                categorias.add(c);
            }
        } catch (IOException ex){
            System.err.println(ex.getMessage());
        }
        // Fecha o arquivo e o buffer após a execução das operações
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

    /***
     * Acessa a pasta de recursos no projeto e lê um arquivo que está na pasta
     * @param fileName
     * @return
     */
    private File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();
        //Verifica se o arquivo é valido antes de retornar a instancia criada
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

    /***
     * Conta a quantidade de carros lida e retorna para o test unitario
     * @return
     */
    public int getCarrosSize(){
        int size = 0;
        for (Locadora l: locadoras) {
            size += l.getCarros().size();
        }
        return size;
    }
}
