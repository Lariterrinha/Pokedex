package Scr;

public class Cifragem {
    

    public static String cifrar_CifraDeCesar(String texto, int chave) {
        String cifra = "";                              // Texto cifrado

        // Passa por cada caracter do texto
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);

            // Se for uma letra, cifra
            if (Character.isLetter(c)) {

                if (Character.isUpperCase(c)) {
                    // Se for maiúscula, cifra considerando a parte dos maiuculos
                    cifra += (char) ((c + chave - 'A') % 26 + 'A');
                } else {
                    // Se for minúscula, cifra considerando a parte dos minúsculos
                    cifra += (char) ((c + chave - 'a') % 26 + 'a');
                }
            } 
            // Se for espaço, mantém ele
            else {
                cifra += c;
            }
        }

        return cifra;
    }

    public static String decifrar_CifraDeCesar(String texto, int chave) {
        return cifrar_CifraDeCesar(texto, 26 - chave);      // 26 - chave =  deslocamento inverso
    }


   public static String cifrar_CifraDeSubstituicao(String texto) {
        // Vetor com o alfabeto normal
        char[] alfabeto = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        
        // Vetor com o alfabeto embaralho
        char[] alfabetoEmbaralhado = "qwertyuiopasdfghjklzxcvbnm".toCharArray();
        
        // Vetor com o texto cifrado
        String cifra = "";

        for(int i = 0; i < texto.length(); i++){
            char c = texto.charAt(i);
            boolean achou = false;
            for(int j = 0; j < alfabeto.length; j++){
                if(c == alfabeto[j]){
                    cifra += alfabetoEmbaralhado[j];
                    achou = true;
                    break;
                }
            }
            if(!achou){
                cifra += c;
            }
        }
        return cifra;
   }

   public static String decifrar_CifraDeSubstituicao(String texto) {
        // Vetor com o alfabeto normal
        char[] alfabeto = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        
        // Vetor com o alfabeto embaralho
        char[] alfabetoEmbaralhado = "qwertyuiopasdfghjklzxcvbnm".toCharArray();
        
        // Vetor com o texto cifrado
        String cifra = "";

        for(int i = 0; i < texto.length(); i++){
            char c = texto.charAt(i);
            boolean achou = false;
            for(int j = 0; j < alfabetoEmbaralhado.length; j++){
                if(c == alfabetoEmbaralhado[j]){
                    cifra += alfabeto[j];
                    achou = true;
                    break;
                }
            }
            if(!achou){
                cifra += c;
            }
        }
        return cifra;
    }
    
    public static String cifragemDupla(String texto){
        return cifrar_CifraDeSubstituicao(cifrar_CifraDeCesar(texto, 3));
    }

    public static String decifragemDupla(String texto){
        return decifrar_CifraDeCesar(decifrar_CifraDeSubstituicao(texto), 3);
    }

}
