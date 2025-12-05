package br.com.meuconsultorio.util;

public class ValidadorCpf {

    public static boolean isCPF(String CPF) {
        // Remove caracteres não numéricos
        CPF = CPF.replaceAll("[^0-9]", "");

        // Verifica tamanho e se é sequencia repetida (ex: 111.111.111-11)
        if (CPF.length() != 11 || CPF.matches("(\\d)\\1{10}")) return false;

        try {
            int sm, i, r, num, peso;
            char dig10, dig11;

            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) dig10 = '0';
            else dig10 = (char) (r + 48);

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) dig11 = '0';
            else dig11 = (char) (r + 48);

            // Verifica se os calculados batem com os digitados
            return (dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10));

        } catch (Exception e) {
            return false;
        }
    }
}