package org.lucky.demo.datagen.sockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lucky.demo.modles.TimeAndTemp;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ServerSocket;
import java.net.Socket;

public class JsonGenerator {

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ServerSocket listener = new ServerSocket(9999);
        Socket socket = listener.accept();
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        while (true){
            TimeAndTemp timeAndTemp = TimeAndTemp.builder()
                    .timeStamp(System.currentTimeMillis())
                    .temp(round(Math.random() * 100,2))
                    .build();
            String asString = mapper.writeValueAsString(timeAndTemp);
//            System.out.println(asString);
            out.println(asString);
            Thread.sleep(50);
        }

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
