package Scr;

import java.io.IOException;
import java.io.RandomAccessFile;

public class DirectIndex {

    static String arq_ids = "indice_id.db";         // Arquivo de indices com Ids e endereços
    static String arq_dados = "Pokemons_dados.db";  // Arquivo de dados em binario
    

    /**
     * Adiciona um novo registro de indice no arquivo de indices
     * @param id do novo pokemon
     * @param pos posição no arquivo de indices
     * @throws IOException
     */
    public static long create(int id, long pos) throws IOException{
        try{
            RandomAccessFile arq_id = new RandomAccessFile(arq_ids, "rw");

            long position = arq_id.length();
            // Adiciona ao final do arquivo
            arq_id.seek(position);
            arq_id.writeByte(0x00);     // Lapide vazia
            arq_id.writeInt(id);            
            arq_id.writeLong(pos);
            arq_id.close();
            return position;             // Posição no arq de indices

        }
        catch(IOException e){
            throw e;
        }
        
    }

    /**
     * Atualiza o endereço(posição) de um id fornecido
     * @param id do registro
     * @param pos nova posição no arquivo de dados
     * @throws IOException  
     */
    public static void update(int id, long pos) throws IOException{
        
        try{
            RandomAccessFile arq_id = new RandomAccessFile(arq_ids,"rw");
            
            long pos_index = getPosition(id);      // Obtem a posição no arquivo de indice

            // Verifica se registro existe mesmo
            if(pos_index >0){      

                pos_index += (1+4);                     // Pula lapide e id
                arq_id.seek(pos_index);         
                arq_id.writeLong(pos);      // Sobrescreve endereço
                arq_id.close();
                System.out.println("Indice de IDs atualizado");
            }
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Procura um id de pokemon utilizando indice e retorna o mesmo
     * @param id do pokemon procurado
     * @return Pokemon procurado
     */
    public static Pokemon readPokemon(int id){

        Pokemon pokemon = new Pokemon();

        try{
            RandomAccessFile arq = new RandomAccessFile(arq_dados,"rw");
            RandomAccessFile arq_id = new RandomAccessFile(arq_ids,"rw");
            
            long pos_index = getPosition(id);      // Obtem a posição no arquivo de indice
            long pos_data;
            int tam_reg;
            byte []b;

            // Verifica se registro existe mesmo
            if(pos_index >= 0){      

                pos_index += (1+4);                     // Pula lapide e id
                arq_id.seek(pos_index);                    
                pos_data = arq_id.readLong();           // Lê qual a posição no arquivo de dados
                
                arq.seek(pos_data);
                
                // Verifica se o registro não foi excluido
                if(arq.readByte() == 0x00){

                    // Lê pokemon e o retorna
                    tam_reg = arq.readInt();            // Lê tamanho do registro
                    b = new byte[tam_reg];              // Aloca bytes necessarios
                    arq.readFully(b);                   // Lê bytes
                    pokemon.fromByteArray(b);           // Transforma bytes em pokemon

                    arq.close();
                    arq_id.close();
                    return pokemon;    


                }else{
                    arq.close();
                    arq_id.close();
                    System.out.println("(Erro arquivo apagado)");
                }
            }
            
        }catch(IOException e){
            e.printStackTrace();
        }
        
        return pokemon;

    }

    /**
     * Apaga um indice referente a um pokemon
     * @param id do pokemon a ser deletado
     * @return bolleano indicando se deu certo a exclusão
     */
    public static boolean delete(int id){

        try{
            RandomAccessFile arq_id = new RandomAccessFile(arq_ids,"rw");
            
            long pos_index = getPosition(id);        // Obtem a posição no arquivo de indice
            
            // Verifica se registro existe mesmo
            if(pos_index >0){      
                arq_id.seek(pos_index);                    
                arq_id.writeByte(0xFF);                 // Marca lapide
                arq_id.close();
                return true;
            }
            arq_id.close();

        }catch(IOException e){
            e.printStackTrace();
        }
        
        return false;
    }


    /**
     * Procura qual em qual byte começa o registro de indice do id requerido;
     * @param id do registro procurado
     * @return long indicando a posição do id no arquivo de index (se não encontrado retorna -1)
     */
    public static long getPosition(int id){

        try{
            RandomAccessFile arq_id = new RandomAccessFile(arq_ids,"rw");
            byte lapide;
            int id_lido;
            long pos_arq;   
                
            try{
                while(true){    // Vai procurando até EOF

                    // Leitura de arquivos cabeçalho
                    lapide = arq_id.readByte();
                    id_lido = arq_id.readInt();
                    pos_arq = arq_id.readLong();
                    
                    // Lapide não prenchida == 0x00
                    if (lapide == 0x00) {                                        

                        if(id_lido == id){             // Verifica se é o registro certo  
                            pos_arq = arq_id.getFilePointer();
                            arq_id.close();     
                            return ( pos_arq - (1+4+8)); // Retorna posição do arquivo de index
                        }
                        
                    }else{ 
                        arq_id.skipBytes(4+8);               // Pula indice
                    }
                }

            }catch(IOException e){
                arq_id.close();
                System.out.println("\nNenhum registro de indice corresponde a sua pesquisa (EOF)");
            }
        
        }catch(IOException e){
            e.printStackTrace();
        }
        
        return -1;  // Não achou

    }
}
