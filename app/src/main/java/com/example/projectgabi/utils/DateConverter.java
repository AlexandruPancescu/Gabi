package com.example.projectgabi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//clasa cu rol de a manipula conversiile de la un obiect Date la un obiect String si vice-verse
public class DateConverter {
    //este o clasa din Java utilizata pentru conversia de la String la Date, respectiv de la Date la String
    //primeste un format de data pe care sa-l aplice in timpul conversie
    //constructorul conține și un al doilea parametru care identifică tipul regiunii în care urmează să fie folosită data
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    public static Date fromString(String value) {
        try {
            //metoda parse - utilizată pentru extragerea unui obiect Date dintr-un String.
            return formatter.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String fromDate(Date value) {
        if(value == null) {
            return null;
        }
        //format() - utilizata pentru transformarea unui obiect Date în String.
        return formatter.format(value);
    }
}