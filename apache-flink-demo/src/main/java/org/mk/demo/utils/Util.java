package org.mk.demo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Util {

    private static ObjectMapper mapper = new ObjectMapper();
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String writeValueAsString(Object obj){
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public static Object readValueAsObject(String json, Class clazz) throws Exception {
        try {
            return mapper.readValue(json,clazz);
        } catch (JsonProcessingException e) {
            return clazz.getDeclaredConstructor().newInstance();
        }
    }

}
