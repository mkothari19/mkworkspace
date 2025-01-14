package org.mk.demo.dataset.assignments.cab;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FilterOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.mk.demo.modles.CabModel;

public class CabDemo {

    /**
     *Data is of the following schema
     * # cab id, cab number plate, cab type, cab driver name, ongoing trip/not, pickup location, destination,passenger count
     *
     * Using Datastream/Dataset transformations find the following for each ongoing trip.
     *
     * 1.) Popular destination.  | Where more number of people reach.
     * 2.) Average number of passengers from each pickup location.  | average =  total no. of passengers from a location / no. of trips from that location.
     * 3.) Average number of trips for each driver.  | average =  total no. of passengers drivers has picked / total no. of trips he made
     */
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSource<String> stringDataSource = env.readTextFile("src/main/resources/cab/cab.txt");

//        cab id, cab number plate, cab type, cab driver name, ongoing trip/not, pickup location, destination,passenger count
        FilterOperator<CabModel> cabDS = stringDataSource
                .map(new MapFunction<String, CabModel>() {
                    @Override
                    public CabModel map(String s) throws Exception {
                        String[] split = s.split(",");
                        return CabModel.builder()
                                .cabId(split[0])
                                .cabNumber(split[1])
                                .cabType(split[2])
                                .cabDriverName(split[3])
                                .ongoing(split[4].equalsIgnoreCase("yes") ? Boolean.TRUE : Boolean.FALSE)
                                .pickup(split[5])
                                .destination(split[6])
                                .passengerCount(split[7].equalsIgnoreCase("'null'") ? 0 : Integer.valueOf(split[7]))
                                .build();
                    }
                })
                .filter(elem -> elem.getOngoing())
                .returns(Types.POJO(CabModel.class));

        cabDS
                 .map(elem -> Tuple2.of(elem.getDestination(), elem.getPassengerCount()))
                 .returns(Types.TUPLE(Types.STRING, Types.INT))
                 .groupBy(0)
                 .sum(1)
                 .maxBy(1)
                 .print();


        cabDS
                .map(elem -> Tuple3.of(elem.getPickup(), elem.getPassengerCount(),1))
                .returns(Types.TUPLE(Types.STRING, Types.INT,Types.INT))
                .groupBy(0)
                .reduce(new CabReducer())
                .map(new CabMapper())
                .maxBy(1)
                .print();

        cabDS
                .map(elem -> Tuple3.of(elem.getCabDriverName(), elem.getPassengerCount(),1))
                .returns(Types.TUPLE(Types.STRING, Types.INT,Types.INT))
                .groupBy(0)
                .reduce(new CabReducer())
                .map(new CabMapper())
                .maxBy(1)
                .print();


    }

    public static class CabReducer implements ReduceFunction<Tuple3<String, Integer, Integer>>{
        @Override
        public Tuple3<String, Integer, Integer> reduce(Tuple3<String, Integer, Integer> t0, Tuple3<String, Integer, Integer> t1) throws Exception {
            return Tuple3.of(t0.f0, t0.f1+t1.f1, t0.f2+t1.f2);
        }
    }

    public static class CabMapper implements MapFunction<Tuple3<String, Integer, Integer>, Tuple2<String, Double>>{
        @Override
        public Tuple2<String, Double> map(Tuple3<String, Integer, Integer> t1) throws Exception {
            return Tuple2.of(t1.f0, Double.valueOf(t1.f1)/t1.f2);
        }
    }
}
