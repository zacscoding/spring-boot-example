package io.spring.batch.database.domain;

import java.util.Random;

public final class CustomerGenerator {

    private static final Random generator = new Random();
    private static final String[] firstNames = {
            "Michael", "Warren", "Ann", "Terrence",
            "Erica", "Laura", "Steve", "Larry"
    };
    private static final String middleInitial = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String[] lastNames = {
            "Gates", "Darrow", "Donnelly", "Jobs",
            "Buffett", "Ellison", "Obama"
    };
    private static final String[] streets = {
            "4th Street", "Wall Street", "Fifth Avenue",
            "Mt. Lee Drive", "Jeopardy Lane",
            "Infinite Loop Drive", "Farnam Street",
            "Isabella Ave", "S. Greenwood Ave"
    };
    private static final String[] cities = {
            "Chicago", "New York", "Hollywood", "Aurora",
            "Omaha", "Atherton"
    };
    private static final String[] states = { "IL", "NY", "CA", "NE" };

    public static Customer createCustomer() {
        return Customer.builder()
                       .id((long) generator.nextInt(Integer.MAX_VALUE))
                       .firstName(selectRandomly(firstNames))
                       .middleInitial(String.valueOf(selectRandomly(middleInitial)))
                       .lastName(selectRandomly(lastNames))
                       .address(generator.nextInt(9999) + " " + selectRandomly(streets))
                       .city(selectRandomly(cities))
                       .state(selectRandomly(states))
                       .zipCode(String.valueOf(generator.nextInt(99999)))
                       .build();
    }

    private static String selectRandomly(String[] data) {
        return data[generator.nextInt(data.length)];
    }

    private static char selectRandomly(String data) {
        return data.charAt(generator.nextInt(data.length()));
    }

    private CustomerGenerator() {}
}
