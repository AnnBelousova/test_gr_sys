package com.gridline.testing;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        List<Flight> flightList = FlightBuilder.createFlights();
        Predicate<Flight> moreThanOneSegment = flight -> flight.getSegments().size() > 1;
        LocalDateTime currentTime = LocalDateTime.now();

        Predicate<Flight> flightsDepartureBeforeCurrentTime = flight -> flight
                .getSegments()
                .stream()
                .anyMatch(segment -> segment.getDepartureDate().isBefore(currentTime));
        FilterFlights filterFlights = new FilterFlights();

        Predicate<Flight>  flightArrivalBeforeDepartureTime = flight -> flight
                .getSegments()
                .stream()
                .anyMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate()));

        Predicate<Flight> timeMoreThanTwoHoursBetweenSegments = flight -> {
            long totalMinutes = 0;
            for (int i = 0; i < flight.getSegments().size()-1; i++) {
                totalMinutes += Math.abs(ChronoUnit.MINUTES.between(flight.getSegments().get(i+1).getDepartureDate(), flight.getSegments().get(i).getArrivalDate()));
            }
            return totalMinutes/60 > 2;
        };

        List <Flight> filteredListDep = filterFlights.filter(flightList, List.of(flightsDepartureBeforeCurrentTime));
        List <Flight> filteredListArr = filterFlights.filter(flightList, List.of(flightArrivalBeforeDepartureTime));
        List <Flight> filteredListDifferent = filterFlights.filter(flightList,
                List.of(moreThanOneSegment, flightArrivalBeforeDepartureTime.negate(),timeMoreThanTwoHoursBetweenSegments));

        System.out.println("List of flights:\n" + flightList);
        System.out.println("\n Flights before current time:\n" + filteredListDep);
        System.out.println("\nSegments with date before departure date:\n" + filteredListArr);
        System.out.println("\nTotal time between segments is on the ground more than 2 hours:\n" + filteredListDifferent);

    }
}