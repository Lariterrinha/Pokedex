package Scr;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

// CREATE READ UPDATE E DELETE (Dados e indices) 
public class CRUD{

    static String arq_csv = "Pokemons.csv";         // Arquivo de dados csv
    static String arq_dados = "Pokemons_dados.db";  // Arquivo de dados em binario
    static String clear = "\033[H\033[2J";          // clear no windowns
     
    /**
     * Carrega base de dados ".csv" para novo arquivo binario ".db", o nome dos arquivos são estaticos dentro da classe   
     * @Entradas    Nome arquivo csv (String statica) + Nome arquivo binario (String statica)
     * @Saidas      Arquivo binario com dados do csv  
     */
     public static void carregarBaseDados(){
        try{

            // Arquivos
            RandomAccessFile arq = new RandomAccessFile(arq_csv, "rw");
            RandomAccessFile arq_bin = new RandomAccessFile(arq_dados, "rw");


            // Variaveis auxiliares
            String linha = new String();
            String []dados;
            String []dados_aux;
            int tam;
            Pokemon pokemon = new Pokemon();   


            //1º Parte - Escreve o cabeçalho (ultimo id salvo) inicando em 0
            long t = arq_bin.length();
            if(t < 1){
                arq_bin.writeInt(0);
            }

            arq.readLine(); // Pula primeira linha csv (titulos)

            //2º parte Lê a linha enquanto ela não está em branco e escreve no arquivo binario
            while(!((linha = arq.readLine()) ==  null)){ 

                //Divide a linha em varios dados e constroe objeto "Pokemon" item a item
                dados = linha.split(",");                   
                
                pokemon.setId_pokedex(Integer.parseInt(dados[0]));
                pokemon.setName(dados[1]);
                pokemon.setAttributes(Integer.parseInt(dados[2]), Integer.parseInt(dados[3]) ,Integer.parseInt(dados[4]));
                pokemon.setClassfication(dados[5]);
                pokemon.setGeneration(Byte.parseByte(dados[6]));
                
                dados_aux = dados[7].split("/");    // split de string de data (dd/mm/aaaa)
                pokemon.setDate(Integer.parseInt(dados_aux[1]),Integer.parseInt(dados_aux[0]), Integer.parseInt(dados_aux[2]));

                dados_aux = dados[8].split("-");    // split de string de abilidades com tamanho variavel

                tam = dados_aux.length;
                dados_aux[0] = dados_aux[0].substring(1, (dados_aux[0].length() ));                            // remove o [ inicial
                dados_aux[tam - 1] = dados_aux[tam - 1].substring(0, (dados_aux[tam - 1].length() - 1));       // remove o ] final


                for(int i = 0; i< dados_aux.length;i++){
                    dados_aux[i] = dados_aux[i].substring(1, dados_aux[i].length() - 1);                      // remove o aspas das palavras
                }
                pokemon.setAbilities(dados_aux);

                // Escreve no arquivo binario pokemon lido
                criarRegistro(pokemon);
            }
            
            // fecha arquivos
            arq.close();
            arq_bin.close();
            System.out.print("Operação concluida");
            
        
        }catch(IOException e){
            e.printStackTrace();
        }     
        
    }

    
    /**
     * Criar registro com dados inseridos pelo usuario   
     * @Entradas    Dados de um Pokemon via Scanner do console
     * @Saidas      Novo registro no arquivo de dados (criado pelo metodo de mesmo nome deste)  
     */
    public static void criarRegistro(){

        Scanner sc = new Scanner(System.in);        // Abre scanner

        Pokemon pokemon = new Pokemon();            // Pokemon a ser lido

        // Leituras do atributos do objeto "Pokemon"

        //NOME
        System.out.print("Digite o nome do Pokémon: ");
        pokemon.setName(sc.nextLine());

        //ATRIBUTOS
        int []aux = new int[3];
        System.out.print("Digite o ataque: ");
        aux[0] = Integer.parseInt(sc.nextLine());
        System.out.print("Digite a defesa: ");
        aux[1] = Integer.parseInt(sc.nextLine());
        System.out.print("Digite o HP: ");
        aux[2] = Integer.parseInt(sc.nextLine());
        pokemon.setAttributes(aux[0], aux[1],aux[2]);

        //CLASSIFICAÇÃO
        System.out.print("Digite a classificação do Pokémon: ");
        pokemon.setClassfication(sc.nextLine());

        // GERAÇÃO
        System.out.print("Digite a geração do Pokémon (1-7): ");
        pokemon.setGeneration(Byte.parseByte(sc.nextLine()));

        // DATA DE APARECIMENTO
        System.out.println("Qual a data de aparecimento do pokemon?: ");

        // Dia
        System.out.print("Dia: ");
        aux[0] = Integer.parseInt(sc.nextLine());

        // Mês
        do{
            System.out.print("Mês (1/12): ");
            aux[1] = Integer.parseInt(sc.nextLine());
        }while(aux[1] > 12);

        // Ano
        do{
        System.out.print("Ano (Depois de 1970): ");
            aux[2] = Integer.parseInt(sc.nextLine());
            pokemon.setDate(aux[0], aux[1], aux[2]);
        } while(aux[2] < 1970);


        // ARRAY DE ABILIDADES
        System.out.print("Quantas habilidades o Pokémon possui? ");
        int numAbilities = Integer.parseInt(sc.nextLine());
        String[] abilities = new String[numAbilities];

        for (int i = 0; i < numAbilities; i++) {
            System.out.print("Digite a habilidade " + (i + 1) + ": ");
            abilities[i] = sc.nextLine();
        }
        pokemon.setAbilities(abilities);

        System.out.println();

        try{
            criarRegistro(pokemon);
            System.out.println("Registro criado com sucesso");
                
        }catch(IOException e){
            System.out.println("Não foi possivel criar registro pokemon");
        }

        //sc.close();  // Fechando o scanner o main não consegue usar
    
    }

    /**
     * Criar registro apartir de um objeto pokemon   
     * @param       Pokemon a ser registrado
     * @Saidas      Novo registro no arquivo de dados  
     */
    public static void criarRegistro(Pokemon pokemon) throws IOException{
        try{

            RandomAccessFile arq = new RandomAccessFile(arq_dados, "rw");     // Arquivo de dados

            arq.seek(0);
            int last_id = arq.readInt();
            
            pokemon.setId_pokedex(last_id + 1);

            // Atualiza o cabeçalho de ultimo pokemon salvo
            arq.seek(0);
            arq.writeInt(pokemon.getId_pokedex());

            // Cria registro e escreve no fim do arquivo
            long pos = arq.length();
            arq.seek(pos);
            arq.writeByte(0);                            // Lapide não marcada
            arq.writeInt(pokemon.toByteArray().length);    // Tamanho do registro
            arq.write(pokemon.toByteArray());              // Registro

            DirectIndexCRUD.createIndexId(pokemon.getId_pokedex(),pos);
            arq.close();
            
        }
        catch (IOException e){
            e.printStackTrace();
            throw(e);
        }
    }

    
    /**
     * Lê um registro a partir de um ID de pokemon   
     * @param       registro_id que indica o id do pokemon a ser lido
     * @Saidas      Pokemon encontrado no arquivo de dados e impressaão do console do toString do pokemon
     */
    public static Pokemon lerRegistro(int registro_id){
        try{
            RandomAccessFile arq_bin = new RandomAccessFile(arq_dados, "rw");         
            byte[] b;
            byte lapide;
            int tam_reg;
            Pokemon pokemon = new Pokemon();
            
            arq_bin.seek(0);
            arq_bin.readInt();          // pular cabeçalho
            
            // Vai lendo até exceção EOF
            try{
                while(true){           
                    // Leitura de arquivos cabeçalho
                    lapide = arq_bin.readByte();
                    tam_reg = arq_bin.readInt();
                    b = new byte[tam_reg];

                    if (lapide == 0x00) {                          // Lapide não prenchida ???
                        arq_bin.readFully(b);
                        pokemon.fromByteArray(b);                 // (No) Lê pokemon
                        if(pokemon.getId_pokedex() == registro_id){
                            //System.out.println(pokemon.toString());
                            arq_bin.close();
                            return pokemon;
                        }
                    }else{ 
                        arq_bin.skipBytes(tam_reg);               // (Yes) Pula arquivo
                        
                    }

                }
            }catch(IOException e){
                System.out.println("Nenhum arquivo corresponde a sua pesquisa (EOF)");
            }

        }catch(IOException e){
            e.printStackTrace();
        }
        
        return null;
    }

    
    /***************************** Lê conjunto de aquivos **********************************/
    public static Pokemon[] lerConjuntoRegistros(int [] IDs){
        try{
            RandomAccessFile arq_bin = new RandomAccessFile(arq_dados, "rw");         
            byte[] b;
            byte lapide;
            int tam_reg;
            Pokemon pokemon_tmp = new Pokemon();
            Pokemon pokemon[] = new Pokemon[IDs.length];
            int i = 0;
            
            arq_bin.seek(4);    // Inicio (Pulando cabeçalho)    
            
            // Vai lendo até exceção EOF
            try{
                while(i < IDs.length){           
                    // Leitura de arquivos cabeçalho
                    lapide = arq_bin.readByte();
                    tam_reg = arq_bin.readInt();
                    b = new byte[tam_reg];

                    if (lapide == 0x00) {                             // É lapide não prenchida ???
                        arq_bin.readFully(b);
                        pokemon_tmp.fromByteArray(b);                 // (No) Lê pokemon
                        if(pokemon_tmp.getId_pokedex() == IDs[i]){
                            System.out.println(pokemon_tmp.toString());
                            pokemon[i] = pokemon_tmp;
                            i++;
                            arq_bin.seek(4);                      // volta no começo do arquivo
                        }
                    }else{ 
                        arq_bin.skipBytes(tam_reg);                   // (Yes) Pula arquivo
                    }

                }
            }catch(IOException e){
                System.out.println("(EOF)");                        // End of File
                arq_bin.close();
            }

        }catch(IOException e){
            e.printStackTrace();
        }
        
        return null;
    }
    

    /***************************** ATUALIZAR REGISTRO **********************************/
    public static boolean atualizarRegistro(int registro_id){

        // Abrir arquivo
        try(RandomAccessFile arq_bin = new RandomAccessFile(arq_dados, "rw")){
            byte[] b;                           // vetor de bytes a ser lido
            long lapide_pos;                    // posição inicial do registro (lapide)
            byte lapide;                        // valor da lapide
            int tam_reg;                        // tamanho do registro
            Pokemon pokemon_tmp = new Pokemon();
            
            arq_bin.seek(0);
            arq_bin.readInt();  // pula cabeçalho
            
            // Vai lendo até exceção EOF
            try{
                while(true){           
                    // Leitura de arquivos cabeçalho
                    lapide_pos = arq_bin.getFilePointer();                  // Posição da lapide
                    lapide = arq_bin.readByte();
                    tam_reg = arq_bin.readInt();
                    b = new byte[tam_reg];

                    if (lapide == 0) {                                      // É lapide não prenchida 
                        arq_bin.readFully(b);
                        pokemon_tmp.fromByteArray(b);                       // Extrair objeto do registro     
                        
                        if(pokemon_tmp.getId_pokedex() == registro_id){     // Verifica se encontrou pokemon
                            
                            pokemon_tmp.alterarPokemon();                    // Função de modificação de classe

                            // Escreve Pokemon alterado no registro
                            if(pokemon_tmp.toByteArray().length <= tam_reg){
                                arq_bin.seek(lapide_pos + 5 );              // Pula lapide (1 byte) e indicador de tamanho (4bytes)
                                arq_bin.write(pokemon_tmp.toByteArray());  // registro atualizado
                                System.out.println("Arquivo alterado");
                                

                            }else{
                                arq_bin.seek(lapide_pos);                   // "Apaga" registro
                                arq_bin.writeByte(0xff);

                                // Escreve registro atualizado no final do arquivo
                                long nova_pos = arq_bin.length();
                                arq_bin.seek(nova_pos);
                                arq_bin.writeByte(0x00);                  // Lapide
                                arq_bin.writeInt(pokemon_tmp.toByteArray().length);
                                arq_bin.write(pokemon_tmp.toByteArray());
                                
                                // Atualiza indice
                                DirectIndexCRUD.updateIndexId(pokemon_tmp.getId_pokedex(),nova_pos); 

                                System.out.println("Arquivo alterado");

                                
                                
                            }
                            return true;
                            
                        }
                    }else{ 
                        arq_bin.skipBytes(tam_reg);
                    }

                }
                
            }catch(IOException e){
                System.out.println("(EOF)");                        // End of File
                System.out.println("Nenhum arquivo corresponde a sua pesquisa (EOF)");
                arq_bin.close();
            }

        }catch(IOException e){
            e.printStackTrace();

        }

        return false;

    }


    
    /***************************** DELETAR REGISTRO **********************************/
    public static boolean deletarRegistro(int registro_id){

        try(RandomAccessFile arq_bin = new RandomAccessFile(arq_dados, "rw")){
            byte[] b;
            long lapide_pos;
            byte lapide;
            int tam_reg;
            Pokemon pokemon_tmp = new Pokemon();
            
            arq_bin.seek(0);
            arq_bin.readInt();
            
            // Vai lendo até exceção EOF
            try{
                while(true){           
                    // Leitura de arquivos cabeçalho
                    lapide_pos = arq_bin.getFilePointer();                      // Posição da lapide
                    lapide = arq_bin.readByte();
                    tam_reg = arq_bin.readInt();
                    b = new byte[tam_reg];

                    if (lapide == 0) {                                      // É lapide não prenchida 
                        arq_bin.readFully(b);
                        pokemon_tmp.fromByteArray(b);                   
                        if(pokemon_tmp.getId_pokedex() == registro_id){     // Encontra pokemon
                            arq_bin.seek(lapide_pos);
                            arq_bin.writeByte(0xff);                      // Escreve 0xFF na lapide
                            
                            deleteFromIndex(pokemon_tmp.getId_pokedex());  // Apaga do arquivo de indice

                            System.out.print(pokemon_tmp.toString());
                            System.out.println("  -  Delatado com sucesso");
                            return true;
                        }
                    }else{ 
                        arq_bin.skipBytes(tam_reg);
                    }

                }
                
            }catch(IOException e){
                System.out.println("(EOF)");                        // End of File
                arq_bin.close();
            }

        }catch(IOException e){
            e.printStackTrace();

        }

        return false;
    }
    

}