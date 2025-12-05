package br.com.meuconsultorio.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ValidadorData {

    public static boolean isDataValida(String data) {
        try {
            // Define o formato esperado
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            // O SEGREDO: Lenient = false (Não aceita datas "flexíveis" como 30/02)
            sdf.setLenient(false);

            // Tenta converter. Se a data for doida (ex: 16/16/2025), vai dar erro.
            sdf.parse(data);

            return true; // Se passou, é válida
        } catch (ParseException e) {
            return false; // Se deu erro, é inválida
        }
    }
}