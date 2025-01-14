package org.lucky.demo;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TestUtil {

    @org.junit.Test
    public void test1(){
        VoidFun fun = obj -> System.out.println(obj);
        fun.println("test");
        List<String> list = Arrays.asList("test1", "test2", "test3", "test4");
        list.forEach(TestUtil :: print);
        list.forEach(new TestUtil() :: println);

    }

    public static void print(Object obj){
        System.out.println(obj);
    }

    public  void println(Object obj){
        System.out.println(obj);
    }

    @Test
    public void test2() {
        Curring converter = new Curring();

        Function<Double, Double> mi2kmConverter = converter.curry1(1.609);
        System.out.println(mi2kmConverter.apply(2.3));
    }
}



@FunctionalInterface
interface VoidFun{
     void println(Object obj);
}

@FunctionalInterface
interface CurryingInterface<T,U,R> extends BiFunction<T,U,R>{

    default Function<U,R> curry1(T t){
        return u -> apply(t,u);
    }

    default Function<T,R> curry2(U u){
        return t -> apply(t,u);
    }
}

class  Curring implements CurryingInterface<Double, Double, Double>{
    @Override
    public Double apply(Double aDouble, Double aDouble2) {
        return aDouble*aDouble2;
    }
}