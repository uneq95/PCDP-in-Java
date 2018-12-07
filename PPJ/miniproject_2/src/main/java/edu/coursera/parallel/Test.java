package edu.coursera.parallel;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {

    public static void main(String[] args) {
        String[] arr = {"1-12-123,234,345"
                , "2-22-213,421,314","3-12-123,234,345"
                , "4-22-213,421,314","5-12-123,234,345"
                , "6-22-213,421,314","17-12-123,234,345"
                ,"18-12-123,234,345"
                , "8-22-213,421,314","19-12-123,234,345"
                , "9-22-213,421,314","20-12-123,234,345"
                , "10-22-213,421,314","21-12-123,234,345"
                , "11-22-213,421,314","22-12-123,234,345"
                , "12-22-213,421,314","23-12-123,234,345"
                , "13-22-213,421,314","24-12-123,234,345"
                , "14-22-213,421,314","25-12-123,234,345"
                , "15-22-213,421,314","26-12-123,234,345"
                , "16-22-213,421,314",};
        long startTime = System.currentTimeMillis();
        Map<String, String> mapSeq = new HashMap<>();

        for(String line: arr){
            String [] arg = line.split("-");
            mapSeq.put(arg[0]+"-"+arg[1],arg[2]);
        }
        System.out.println("Seq time : "+ (System.currentTimeMillis()-startTime));
        startTime = System.currentTimeMillis();
        Map<String, String> mapPar = Stream.of(arr).parallel().map(line->{
            String [] arg = line.split("-");
            return arg[0]+"-"+arg[1]+"|"+arg[2];
        }).collect(Collectors.toMap(line->(line.split("|"))[0], line->(line.split("|"))[1]));
        System.out.println("Par time : "+ (System.currentTimeMillis()-startTime));
    }
}
