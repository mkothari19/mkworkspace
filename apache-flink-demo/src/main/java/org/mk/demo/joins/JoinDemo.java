package org.mk.demo.joins;

import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.MapOperator;
import org.mk.demo.modles.Location;
import org.mk.demo.modles.Person;
import org.mk.demo.modles.PersonLocation;

public class JoinDemo {

    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSource<String> textFile = env.readTextFile("src/main/resources/dataset/person/person");
        MapOperator<String, Person> personDs = textFile.map(
                line -> {
                    String[] split = line.split(",");
                    return new Person(Integer.valueOf(split[0]), split[1]);
                });

        DataSource<String> textFileLoc = env.readTextFile("src/main/resources/dataset/person/location");
        MapOperator<String, Location> locationDs = textFileLoc.map(
                line -> {
                    String[] split = line.split(",");
                    return new Location(Integer.valueOf(split[0]), split[1]);
                });

        personDs
                .join(locationDs)
                .where("id")
                .equalTo("id")
                .with(new JoinFunction<Person, Location, PersonLocation>() {
                    @Override
                    public PersonLocation join(Person person, Location location) throws Exception {
                        return new PersonLocation(person,location);
                    }
                })
                .filter(personLocation -> personLocation.getLocation().getLoc().equalsIgnoreCase("LA") )
                .map(personLocation -> personLocation.getPerson())
                .print();
    }
}
