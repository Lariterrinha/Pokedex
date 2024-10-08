package Scr;


public class Data {
    int dia;
    int mes;
    int ano;
    int tempo_dias;

    Data(){}

    // Construtor passando dia mes e ano (chama o set data)
    Data(int dd, int mm, int aaaa) {
        if(aaaa < 1970){
            System.out.println("Não foi possivel criar objeto Data");
            System.out.println("Data anterior a 1970");
        }
        else{
            this.setData(dd,mm,aaaa);
        }
    }

    // Construtor passando tempo em dias (chama o set data)
    Data(int dias) {
        if(dias<0){
            System.out.println("Não foi possivel criar objeto Data");
            System.out.println("Data anterior a 1970");
        }
        this.setData(dias);
    }
    
    // verifica se o ano é bissexto
    private boolean isAnoBissexto(int ano) {
        return (ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0);    // calculo de ano bissexto
    }

    /*********** Controi uma data a partir de dia mes e ano ***********/
    public void setData(int dd, int mm, int aaaa){
        tempo_dias = 0;

        // Somas dos dias do ano até ano requerido
        for (int i = 1970; i < aaaa; i++) {
            this.tempo_dias += isAnoBissexto(i) ? 366 : 365;
        }

        int[] diasPorMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (isAnoBissexto(aaaa)) diasPorMes[1] = 29;

        // Soma dos dias do mês até chegar no mes
        for (int i = 0; i < mm - 1; i++) {
            this.tempo_dias += diasPorMes[i];
        }

        this.tempo_dias += dd - 1;
        this.dia = dd;
        this.mes = mm;
        this.ano = aaaa;   
    }


    /*********** Constroi a data a partir dos dias até 1970 ***********/
    public void setData(int dias){
        this.tempo_dias = dias;
        int[] diasPorMes;
        int diasNoAno;

        // Remove dias e add +1 ao ano de acordo com o ano
        int ano_aux = 1970;
        while (true) {
            diasNoAno = isAnoBissexto(ano_aux) ? 366 : 365;
            if (dias >= diasNoAno) {
                dias -= diasNoAno;
                ano_aux++;
            } else {
                break;
            }
        }

        diasPorMes = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (isAnoBissexto(ano_aux)) diasPorMes[1] = 29;

        // Remove dias e add +1 mes de acordo com array dias por mes
        this.mes = 1;
        for (int i = 0; i < 12; i++) {
            if (dias >= diasPorMes[i]) {
                dias -= diasPorMes[i];
                mes++;
            } else {
                break;
            }
        }

        // Atualiza variaveis
        this.dia = dias + 1;
        this.ano = ano_aux;
    }


    @Override
    public String toString() {
        return String.format("%02d/%02d/%04d", dia, mes, ano);
    }


    public int getDia() {
        return dia;
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }

    public int getTempo_dias() {
        return tempo_dias;
    }
    
    
}
