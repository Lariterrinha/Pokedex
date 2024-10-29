package Scr;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class NameIndex{

    static String arq_nomes = "data/indice_nomes.db";  // Arquivo de dados em binario
    static String arq_ids = "data/indice_id.db";         // Arquivo de indices com Ids e endereços
    static String arq_dados = "data/Pokemons_dados.db";  // Arquivo de dados em binario
    
    /**
     * Adiociona um registro de nome e posição no final do arquivo de indice de nomes;
     * @param nome_String do novo pokemon
     * @param position posição no arquivo de ids
     * @throws IOException 
     */
    public static void create(String nome_String, long position) throws IOException{

        try{
            RandomAccessFile arq_nom = new RandomAccessFile(arq_nomes, "rw");
   
            // Adiciona ao final do arquivo (lapide, tamanho do registro, String, posição)
            arq_nom.seek(arq_nom.length());
            arq_nom.writeByte(0x00);                                     // Lapide vazia
            arq_nom.writeInt(toByteArray(nome_String, position).length);       // Tamanho registro
            arq_nom.write(toByteArray(nome_String, position));             // Registro   
            arq_nom.close();

        }catch(IOException e){
            throw e;
        }
        
    }

    /**
     * Lê um registro de pokemon no arquivo de dados a partir de um nome
     * Não é case sensitive e compara os nomes parcialmente, ou seja, utilizando contains
     * @param nome do pokemon a ser lido
     * @return Pokemon lido
     */
    public static Pokemon readPokemon(String nome){

        Pokemon pokemon = new Pokemon();

        try{
            RandomAccessFile arq_pokemons = new RandomAccessFile(arq_dados,"rw");
            RandomAccessFile arq_id = new RandomAccessFile(arq_ids,"rw");
            RandomAccessFile arq_nom = new RandomAccessFile(arq_nomes, "rw");
            
            long pos_nome = getPosition(nome);      // Obtem a posição no arquivo de indice
            long pos_id;
            long pos_pokemons;
            int tam_reg_pokemon;
            byte []b;

            // Verifica se registro existe mesmo
            if(pos_nome > 0){      

                // LEITURA DO ENDEREÇO DO ID NO ARQUIVO DE NOMES
                pos_nome += 1 + 4;                     // Pula lapide e tamanho do registro
                arq_nom.seek(pos_nome);
                arq_nom.readUTF();                     // Pula nome
                pos_id = arq_nom.readLong();           // Leitura do endereço no arquivo de IDs


                // LEITURA DO ENDEREÇO DO DADO NO ARQUIVO DE IDS
                pos_id += 1 + 4;                       // Pula lapide e id
                arq_id.seek(pos_id);
                pos_pokemons = arq_id.readLong();        // Leitura do endereço no arquivo de dados
                

                /*  LEITURA DO REGISTRO NO ARQUIVO DE DADOS */
                arq_pokemons.seek(pos_pokemons);

                // Verifica se o registro não foi excluido
                if(arq_pokemons.readByte() == 0x00){

                    // Lê pokemon e o retorna
                    tam_reg_pokemon = arq_pokemons.readInt();           // Lê tamanho do registro
                    b = new byte[tam_reg_pokemon];                      // Aloca bytes necessarios
                    arq_pokemons.readFully(b);                          // Lê bytes
                    pokemon.fromByteArray(b);                           // Transforma bytes em pokemon

                    // Fecha arquivos
                    arq_pokemons.close();
                    arq_id.close();
                    arq_nom.close();
                    return pokemon;    

                }else{
                    // Fecha arquivos
                    arq_pokemons.close();
                    arq_id.close();
                    arq_nom.close();
                    System.out.println("(Erro arquivo apagado)");
                }
            }
            
        }catch(EOFException e){
            System.out.println("(EOF)");

        }catch(IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
        
        return pokemon;

    }

    /**
     * Apaga um indice referente a um pokemon
     * @param id do pokemon a ser deletado
     * @return bolleano indicando se deu certo a exclusão
     */
    public static boolean delete(String nome){

        try{
            RandomAccessFile arq_nom = new RandomAccessFile(arq_nomes,"rw");
            
            long pos_index = getPosition(nome);        // Obtem a posição no arquivo de nomes
            
            // Verifica se registro existe mesmo
            if(pos_index >= 0){      
                arq_nom.seek(pos_index);                    
                arq_nom.writeByte(0xFF);                 // Marca lapide
                arq_nom.close();
                return true;
            }
            arq_nom.close();

        }catch(IOException e){
            e.printStackTrace();
        }
        
        return false;
    }

    public static void update(String nome_antigo, String novo_nome){
        try{
            RandomAccessFile arq = new RandomAccessFile(arq_nomes,"rw");
            long pos = getPosition(nome_antigo);
            long pos_id;

            arq.seek(pos + 5 + nome_antigo.length() + 2);      // Pula lapide, tamanho do registro e nome
            pos_id = arq.readLong();
            arq.seek(pos);                                    // Volta para o inicio do registro
                

            if(novo_nome.length() <= nome_antigo.length()){
                arq.skipBytes(5);                    // Pula lapide e indicador de tamanho do registro
                arq.writeUTF(novo_nome);
                arq.writeLong(pos_id);
                arq.close();
            }else{
                arq.write(0xFF);                    // Apaga registro atual
                arq.close();
                create(novo_nome, pos_id);
            }
           
                        
        }catch(IOException e){
            System.out.println("Erro ao atualizar o arquivo de nomes");

        }
    }

    /**
     * Procura a primeira ocorrencia (em qual byte começa) o registro com o nome requerido
     * Não é case sensitive e compara os nomes parcialmente, ou seja, utilizando contains
     * @param nome do pokemon do registro procurado
     * @return long indicando a posição do nome no arquivo de ids (se não encontrado retorna -1)
     */
    public static long getPosition(String nome) throws IOException{

        try{

            RandomAccessFile arq = new RandomAccessFile(arq_nomes,"rw");
            int tam_registro;
            byte lapide;
            String nome_lido;
            long pos_arq;

            nome = nome.toLowerCase();      // sem case sesitive

            arq.seek(0);

            try{
                while(true){    // Vai procurando até EOF

                    // Leitura de arquivos cabeçalho
                    lapide = arq.readByte();
                    tam_registro = arq.readInt();

                    // Lapide não prenchida == 0x00
                    if (lapide == 0x00) {                                        

                        nome_lido = arq.readUTF();
                        pos_arq = arq.readLong();

                        if((nome_lido.toLowerCase()).contains(nome)){                   // Verifica se é o registro certo  
                            pos_arq = arq.getFilePointer();
                            arq.close();     
                            return ( pos_arq - tam_registro - 1 - 4);          // Retorna posição do registro no arquivo de nomes
                        }

                    }else{
                        arq.skipBytes(tam_registro);

                    }
                }

            }catch(EOFException e){
                arq.close();
                System.out.println("\nNenhum registro de indice corresponde a sua pesquisa (EOF)");
    
            }catch(IOException e){
                System.out.println(e.getMessage());
                e.printStackTrace();
                throw e;
            }
        
        }catch(IOException e){
            e.printStackTrace();
        }
        
        return -1;

    }

    /**
     * Transforma um nome e posição em um array de bytes
     * @param nome do pokemon
     * @param position no arquivo de ids
     * @return array de bytes
     * @throws IOException
     */
    public static byte[] toByteArray(String nome, long position) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(nome);
        dos.writeLong(position);
        return baos.toByteArray();
    }

}
