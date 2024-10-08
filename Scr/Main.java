package Scr;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

/*
 * TP01 - Algoritmos e Estrutura de Dados 3
 * Sistema de Armazenamento de dados de Pokemons
 * CRUD - Create, Read, Update e Delete 
 */

public class Main{

    static String arq_csv = "Pokemons.csv";
    static String arq_dados = "Pokemons_dados.db";  // Arquivo de dados
    static String indice_id = "indice_id.db";       // Arquivo de indices com Ids e endereços
    static String clear = "\033[H\033[2J";          // clear no windowns


    public static void main(String[] args){
        
         // Variaveis auxiliares
        int selecao = -1;      
        int ids[];    
        
        Scanner sc = new Scanner(System.in);
        
        System.out.print(clear);          // Limpa tela (windows)

        //MENU
        do{
                
            System.out.println();
            System.out.println("=====================================");
            System.out.println("          Sistema de Cadastro        ");
            System.out.println("=====================================");
            System.out.println("1. Carregar base de dados");
            System.out.println("2. Criar registro");
            System.out.println("3. Ler registro");
            System.out.println("4. Ler conjunto de registros");
            System.out.println("5. Atualizar registro");
            System.out.println("6. Deletar registro");
            System.out.println("7. Sair do Sistema");
            System.out.println("=====================================");
            
            System.out.print("Escolha uma opção: ");


            // Leitura da seleção
            try{
                selecao = Integer.parseInt(sc.nextLine());
            }
            catch(Exception e){
                selecao = 0; // Erro de leitura - Buffer cheio, repete operação
            }
            

            // MENU
            switch(selecao) {
                
                case 0:
                    // ERRO DE LEITURA
                    break;

                case 1:
                    // Carregar .csv para arquivo binario
                    CRUD.carregarBaseDados();
                    break;

                case 2:
                    // Inserir um registro no arquivo
                    CRUD.criarRegistro();
                    break;

                case 3:
                    // Procurar um unico registro
                    System.out.print("Qual o Id do registro a ser lido? ");
                    CRUD.lerRegistro( Integer.parseInt(sc.nextLine()));
                    break;

                case 4:
                    // Procurar um conjunto de "n" registros
                    System.out.print("Quantos Pokemons você deseja procurar? ");
                    ids = new int[ Integer.parseInt(sc.nextLine())];
                    for(int i = 0; i<ids.length; i++){
                        System.out.print("Qual o id do " + (i+1) + "º pokemon? ");
                        ids[i] =  Integer.parseInt(sc.nextLine()); 
                    }
                    CRUD.lerConjuntoRegistros(ids);
                    break;

                case 5:
                    // Atualizar algum registro
                    System.out.print("Qual o Id do registro a ser atualizado? ");
                    CRUD.atualizarRegistro( Integer.parseInt(sc.nextLine()));
                    break;

                case 6:                
                    // Deletar regitro (marcar lapide)
                    System.out.print("Qual o Id do registro a ser deletado?");
                    CRUD.deletarRegistro( Integer.parseInt(sc.nextLine()));
                     
                    break;

                
                case 7:
                    // Fim programa
                    System.out.println("Saindo... Até logo!");
                    break;

                default:
                    System.out.println("\nOpção inválida. Por favor, escolha um número entre 1 e 7.");
            }
            

            System.out.println("\n\nPessione qualquer tecla para continuar");
            sc.nextLine();
            System.out.print(clear);          // Limpa tela (windows)

        }while(selecao != 7);

        sc.close();     
    }

    

    // CRUD

    class CRUD{
        
        
        // Carregar base de dados ".csv" para novo arquivo binario ".db"
        /***************************** Carregar base de dados **********************************/
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

        /***************************** Criar registro com dados inseridos pelo usuario **********************************/
        public static void criarRegistro(){

            Scanner sc = new Scanner(System.in);     // Abre scanner

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

        /***************************** Criar registro apartir de um objeto pokemon **********************************/
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

                createIndexId(pokemon.getId_pokedex(),pos);
                arq.close();
                
            }
            catch (IOException e){
                e.printStackTrace();
                throw(e);
            }
        }

        

        /***************************** Lê um registro **********************************/
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
                                System.out.println(pokemon.toString());
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

                                    arq_bin.seek(arq_bin.length());             // Escreve registro atualizado no final do arquivo
                                    arq_bin.writeByte(0x00);                  // Lapide
                                    arq_bin.writeInt(pokemon_tmp.toByteArray().length);
                                    arq_bin.write(pokemon_tmp.toByteArray());
                                    System.out.println("Arquivo alterado");

                                    updateIndexId(pokemon_tmp.getId_pokedex(),lapide_pos); // Atualiza indice
                                    
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
    

        
        /***************************** ADICIONAR NOVO INDICE **********************************/
        public static void createIndexId(int id, long pos) throws IOException{
            try{
                RandomAccessFile arq_id = new RandomAccessFile(indice_id, "rw");

                // Adiciona ao final do arquivo
                arq_id.seek(arq_id.length());
                arq_id.writeByte(0x00);     // Lapide vazia
                arq_id.writeInt(id);            
                arq_id.writeLong(pos);

                arq_id.close();

            }
            catch(IOException e){
                throw e;
            }
            
        }

        public static void updateIndexId(int id, long pos) throws IOException{
            try{
                RandomAccessFile arq_id = new RandomAccessFile(indice_id, "rw");
                byte lapide;
                int tam_reg = 1+4+8 ;    // lapide + id + pos 

                
                // Procura registro
                while(true){        

                    // Leitura de arquivos cabeçalho
                    lapide = arq_id.readByte();

                    // Lapide não prenchida ???
                    if (lapide == 0x00) {                       // Verifica se é o registro certo                   
                        
                        if(arq_id.readInt() == id){                    
                            arq_id.writeLong(pos);
                            arq_id.close();
                        }
                    }else{ 
                        arq_id.skipBytes(tam_reg);               // Pula arquivo
                        
                    }

                }    
                
            }
            catch(IOException e){
                throw e;
            }
            
        }
    }
}

