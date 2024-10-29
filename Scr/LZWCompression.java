package Scr;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class LZWCompression{

    ArrayList<String> dicionario; 
    RandomAccessFile arq;
    String caminho_arq;
    final int TAMANHO_DICIONARIO = 256;

    LZWCompression(String caminho_arq){
        try {
            dicionario = new ArrayList<String>();
            this.arq = new RandomAccessFile(caminho_arq, "rw");
            this.caminho_arq = caminho_arq;
            inicializeDicionario();

        } catch (IOException e) {
            e.printStackTrace();
        }  
    }

    public void inicializeDicionario(){
        
        // Adiciona todos os caracteres da tabela ASCII (0 - 255)
        
        for(int i = 0; i < TAMANHO_DICIONARIO; i++){
            if(!dicionario.contains((char)i + ""))
                dicionario.add((char)i + "");
        }
        
        /*
        // Pega todos os caracteres presentes no arquivo
        String caracter_lido;
        try{

            arq.seek(0);
            while(arq.getFilePointer() < arq.length()){

                caracter_lido = (char)arq.read() + "";
                if(!dicionario.contains(caracter_lido)){
                    dicionario.add(caracter_lido);
                }
            }

        }catch(IOException e){
            e.printStackTrace();
        }
         */
        
    }

    /**
     * Comprime o arquivo de entrada desta instância
     */
    public void compress(){
        int ultima_pesquisa_bem_sucedida = 0;
        String caracter_lido;
        String combinado = "";

        try{
            RandomAccessFile arq_compactado = new RandomAccessFile(caminho_arq + ".lzw", "rw");
            arq.seek(0);

            // Compressão até o final do arquivo
            while(arq.getFilePointer() < arq.length()){

                // Lê um caracter e concatena ao combinado (caracter do dicionario)
                caracter_lido = (char)arq.read() + "";
                combinado += caracter_lido; 

                // Caso contenha o catacter combinado no dicionario continua concatenando e salva a ultima pesquisa bem sucedida
                if(dicionario.contains(combinado)){
                    ultima_pesquisa_bem_sucedida = dicionario.indexOf(combinado);
                }
                //  Caso não, adiociona e escreve a posição da ultima pesquisa valida no arq compactado 
                else{

                    if(dicionario.size() < 32767){      // Congelamento do dicionario em 2 bytes 
                        dicionario.add(combinado);
                    }
                    arq_compactado.writeShort(ultima_pesquisa_bem_sucedida);
                    combinado = combinado.charAt(combinado.length() - 1) + "";                     // Reinicia String combinada
                }
            }
            if(!combinado.isEmpty())    // Verifica se combinado não está vazio e adiciona o restante que falta
                arq_compactado.writeShort(ultima_pesquisa_bem_sucedida);

            arq_compactado.close();
            arq.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void decompress(){

        try{
            // Cria um novo arquivo para descompactar
            RandomAccessFile arq_descompactado = new RandomAccessFile(this.caminho_arq.substring(0,caminho_arq.length() - 10) + ".db" , "rw");

            this.arq.seek(0);

            // Lê o primeiro código do arquivo compactado
            int codigo_lido = arq.readShort();
            String sequencia_decodificada = dicionario.get(codigo_lido);
            String concatenada = sequencia_decodificada;

            // Como só tem um caracter inicialmente, escreve ele
            arq_descompactado.writeByte((byte)sequencia_decodificada.charAt(0));
            
             // Anda até o final do arquivo compactado (Enquanto não for o ultimo "short" continua)
            while(arq.getFilePointer() < arq.length() + 1 ){ 
                codigo_lido = arq.readShort();
                sequencia_decodificada = dicionario.get(codigo_lido);
                concatenada += sequencia_decodificada.charAt(0); 

                // verifica se tem no dicionario a string concatenada até então
                if(dicionario.contains(concatenada)){
                    
                    if(dicionario.size() < 32767){ // Congelamento do dicionario em 2 bytes
                        dicionario.add(concatenada);
                    }
                    
                    if (codigo_lido < dicionario.size()) {
                        concatenada = dicionario.get(codigo_lido - 1);
                    } else {
                        concatenada = sequencia_decodificada + sequencia_decodificada.charAt(0);
                    }
                    
                    concatenada = concatenada.charAt(concatenada.length() - 1) + "" ;       // Reinicia string concateda (mantem ultimo caracter)
                    
                    // Escreve todos os caracteres da sequência decodificada no arquivo
                    for(char c : sequencia_decodificada.toCharArray()){
                        arq_descompactado.writeByte((byte)c);
                    }
                }
            
                
            }

            arq.close();
            arq_descompactado.close();
            
        /* 
            Função LZW_Descompressao(arquivo_compactado):
        Inicializar dicionário com cada caractere único (códigos de 0 a 255)
        
        codigo_anterior ← Ler o primeiro código do arquivo_compactado
        sequência_decodificada ← dicionário[codigo_anterior]
        Adicionar sequência_decodificada ao resultado_final

        PARA cada proximo_codigo no arquivo_compactado FAÇA:
            SE proximo_codigo EXISTE no dicionário:
                sequência_atual ← dicionário[proximo_codigo]
            SENÃO:
                sequência_atual ← sequência_decodificada + primeiro caractere de sequência_decodificada

            Adicionar sequência_atual ao resultado_final
            Adicionar sequência_decodificada + primeiro caractere de sequência_atual ao dicionário, SE espaço disponível

            sequência_decodificada ← sequência_atual

        Retornar resultado_final
        */
           

            

        }catch(Exception e){
                e.printStackTrace();
        }
    }
        

}