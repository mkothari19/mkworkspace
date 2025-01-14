package org.mk.demo.datagen.sockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mk.demo.modles.HomeTempSensor;
import org.mk.demo.utils.Util;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HomeSensorGenerator {

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ServerSocket listener = new ServerSocket(9999);
        Socket socket = listener.accept();
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        while (true){
            HomeTempSensor timeAndTemp = HomeTempSensor.builder()
                    .timeStamp(System.currentTimeMillis())
                    .temp(Util.round(Math.random() * 100,2))
                    .sensorNumber((int)(Math.random()*5))
                    .counter(1)
                    .build();
            String asString = mapper.writeValueAsString(timeAndTemp);
            System.out.println(timeAndTemp.toString());
            out.println(asString);
            Thread.sleep(1000); // 1000 millie means 1 seconds
        }
    }
    /*public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }*/
}
