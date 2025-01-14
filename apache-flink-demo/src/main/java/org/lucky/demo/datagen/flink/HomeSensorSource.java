package org.lucky.demo.datagen.flink;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.lucky.demo.modles.HomeTempSensor;
import java.util.concurrent.ThreadLocalRandom;
import static org.lucky.demo.utils.Util.round;

public class HomeSensorSource implements SourceFunction<HomeTempSensor> {

    volatile boolean isRunning = true;

    @Override
    public void run(SourceContext<HomeTempSensor> ctx) throws Exception {

        int counter =1;
//        long eventTime = System.currentTimeMillis() - 20000; // 20 seconds behind flink program's start time
        long eventTime = System.currentTimeMillis();
        HomeTempSensor timeAndTemp = HomeTempSensor.builder()
                .timeStamp(eventTime)
                .temp(round(Math.random() * 100,2))
                .sensorNumber((int)(Math.random()*5))
                .counter(1)
                .build();
        while (isRunning){
            ctx.collect(timeAndTemp);
            System.out.println(timeAndTemp.toString());
            // create elements and assign timestamp with randomness so that they are not same as current system clock time
            /*timeAndTemp.setTimeStamp(
                    timeAndTemp.getTimeStamp() + ThreadLocalRandom.current().nextLong( 1000, 6000 )
            );*/
            timeAndTemp.setTimeStamp(System.currentTimeMillis());
            timeAndTemp.setCounter(counter++);
            timeAndTemp.setSensorNumber((int)(Math.random()*1));
            timeAndTemp.setTemp(round(Math.random() * 100,2));
            Thread.sleep( 1000 );
        }

    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
