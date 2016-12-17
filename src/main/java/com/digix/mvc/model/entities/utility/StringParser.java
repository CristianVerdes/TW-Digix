package com.digix.mvc.model.entities.utility;

/**
 * Created by Daniel Moniry on 03.06.2016.
 */

import java.text.Normalizer;

public class StringParser {

    public static String ToASCII(String text){
        return Normalizer.normalize(text,Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }
}
