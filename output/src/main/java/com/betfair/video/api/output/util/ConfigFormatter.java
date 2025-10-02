package com.betfair.video.api.output.util;

import java.util.HashSet;
import java.util.Set;

public class ConfigFormatter {

    private ConfigFormatter() {
    }

    public static Set<String> csvToStringSet(String csv) {
        Set<String> resultSet = new HashSet<>();

        if (csv == null || csv.trim().isEmpty()) {
            return resultSet;
        }

        String[] items = csv.split(",");
        for (String item : items) {
            resultSet.add(item.trim());
        }

        return resultSet;
    }

    public static Set<Integer> csvToIntegerSet(String csv) {
        Set<String> stringSet = csvToStringSet(csv);

        return stringSet.stream()
                .map(Integer::parseInt)
                .collect(java.util.stream.Collectors.toSet());
    }
}
