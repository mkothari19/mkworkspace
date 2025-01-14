package org.lucky.demo.joins;

import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.lucky.demo.modles.Location;
import org.lucky.demo.modles.Person;
import org.lucky.demo.modles.PersonLocation;

public class JoinDemoCollectionObject {

    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSource<Person> personDs = env.fromElements(
                new Person(1,"lucky"),
                new Person(2,"prajakta"),
                new Person(3,"umesh"),
                new Person(4,"praful"),
                new Person(6,"Tushar")
        );

        DataSource<Location> locationDs = env.fromElements(
                new Location(1,"Pune"),
                new Location(2,"Wadasa"),
                new Location(3,"Mouda"),
                new Location(4,"Godhani"),
                new Location(5,"Godhani")
        );

        JoinFunction<Person, Location, PersonLocation> joinFunction = new JoinFunction<Person, Location, PersonLocation>() {
            @Override
            public PersonLocation join(Person person, Location location) throws Exception {
                return new PersonLocation(person, location);
            }
        };

        System.out.println("******** LEFT OUTER JOIN ****************");
        personDs.leftOuterJoin(locationDs)
                .where("id")
                .equalTo("id")
                .with(joinFunction)
                .print();
        System.out.println("******** RIGHT OUTER JOIN ****************");
        personDs.rightOuterJoin(locationDs)
                .where("id")
                .equalTo("id")
                .with(joinFunction)
                .print();
    }
}
