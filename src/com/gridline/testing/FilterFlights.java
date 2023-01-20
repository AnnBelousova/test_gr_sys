package com.gridline.testing;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterFlights {
    public List<Flight> filter(List<Flight> listFlight, List<Predicate<Flight>> rules){
        return listFlight.stream().filter(rules.stream().reduce(p -> true, Predicate::and)).collect(Collectors.toList());
    }
}
