package solutions.day19;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Day19 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        List<String> targets = new ArrayList<>();
        List<String> patterns = Stream.of(inputScanner.nextLine().split(", ")).toList();
        inputScanner.nextLine();
        while (inputScanner.hasNextLine()) {
            targets.add(inputScanner.nextLine());
        }

        int possible = 0;
        long totalWays = 0;
        var cache = new HashMap<String, Long>();
        for (String target : targets) {
            long ways = countPossible(target, patterns, cache);
            if (ways > 0) possible++;
            totalWays += ways;
        }

        return new SolutionResponse(possible, totalWays);
    }

    private long countPossible(String target, List<String> patterns, HashMap<String, Long> cache) {
        if (cache.containsKey(target)) return cache.get(target);
        if (target.isEmpty()) return 1;

        long ways = 0;
        for (String pattern : patterns) {
            if (target.startsWith(pattern)) {
                ways += countPossible(target.substring(pattern.length()), patterns, cache);
            }
        }
        cache.put(target, ways);
        return ways;
    }
}
