package org.mk.demo.datagen.flink;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.mk.demo.modles.HomeTempSensor;
import org.mk.demo.utils.Util;

public class HomeSensorSessionWindowSource implements SourceFunction<HomeTempSensor> {

    volatile boolean isRunning = true;

    @Override
    public void run(SourceContext<HomeTempSensor> ctx) throws Exception {
        int counter =0;
        long eventTime = System.currentTimeMillis();
        HomeTempSensor timeAndTemp = HomeTempSensor.builder()
                .timeStamp(eventTime)
                .temp(Util.round(Math.random() * 100,2))
                .sensorNumber((int)(Math.random()*5))
                .counter(1)
                .build();
        while (isRunning){
            ctx.collect(timeAndTemp);
            System.out.println(timeAndTemp.toString());
            timeAndTemp.setTimeStamp(System.currentTimeMillis());
            timeAndTemp.setCounter(counter++);
            timeAndTemp.setSensorNumber((int)(Math.random()*1));
            timeAndTemp.setTemp(Util.round(Math.random() * 100,2));
            if(counter>=10){
                counter=0;
                System.out.println("******* slip for 5 seconds *************");
                Thread.sleep(5000);

            }
            Thread.sleep( 1000 );
        }

    }

    @Override
    public void cancel() {
        isRunning=false;

    }
}
