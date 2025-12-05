package br.com.meuconsultorio.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ValidadorHora {

    public static boolean isHoraValida(String hora) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setLenient(false); // O Grande Juiz: Não aceita 25:00 ou 10:90
            sdf.parse(hora);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}