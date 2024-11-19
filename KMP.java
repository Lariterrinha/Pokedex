import java.util.Scanner;

public class KMP {
    //fução de prefixo kmp melhorado em java (usando -1)
    /**
     * Recebe uma string via scanner e mostra a função de prefixo (pi) dela
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        int[] pi = new int[s.length()];
        pi[0] = 0;
        int k = 0;
        for (int i = 1; i < s.length(); i++) {
            while (k > 0 && s.charAt(k) != s.charAt(i)) {
                k = pi[k - 1];
            }
            if (s.charAt(k) == s.charAt(i)) {
                k++;
            }
            pi[i] = k;
        }
        for (int i = 0; i < s.length(); i++) {
            System.out.print(pi[i] + " ");
        }
        System.out.println();
        //melhoraPi(pi, s);
    }
        /**
     * recebe a função de prefico Pi e maz a melhora nela, (pi linha) e imprime no console
     */
    public static void melhoraPi(int[] pi, String s) {
        
        for (int i = 0; i < pi.length; i++) {
            System.out.print(pi[i] + " ");
        }
        System.out.println();
    }
}
