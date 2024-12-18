package solutions.day11;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.*;

public class Day11 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        LinkedList<Long> stones = new LinkedList<>(Arrays.stream(inputScanner.nextLine().split(" ")).map(Long::parseLong).toList());

        var stonesCopy = new LinkedList<>(stones);
        for (int i = 0; i < 25; i++) {
            stonesCopy = newStones(stonesCopy);
        }

        return new SolutionResponse(stonesCopy.size(), part2(stones));
    }

    private static LinkedList<Long> newStones(LinkedList<Long> stones) {
        LinkedList<Long> result = new LinkedList<>();

        for (var stone : stones) {
            String stoneString = stone.toString();
            if (stone == 0L) {
                result.add(1L);
            } else if (stoneString.length() % 2 == 0) {
                int mid = stoneString.length() / 2;
                String part1 = String.valueOf(Long.parseLong(stoneString.substring(0, mid)));
                String part2 = String.valueOf(Long.parseLong(stoneString.substring(mid)));
                result.addLast(Long.parseLong(part1));
                result.addLast(Long.parseLong(part2));
            } else {
                var multiplied = stone * 2024L;
                result.addLast(multiplied);
            }
        }

        return result;
    }

    private long part2(List<Long> stones) {
        Map<Long, Long> stoneCounts = new HashMap<>();

        for (long stone : stones) {
            stoneCounts.put(stone, stoneCounts.getOrDefault(stone, 0L) + 1);
        }

        for (int i = 0; i < 75; i++) {
            Map<Long, Long> newStoneCounts = new HashMap<>();

            for (var stoneCount : stoneCounts.entrySet()) {
                long stone = stoneCount.getKey();
                long count = stoneCount.getValue();

                String stoneString = Long.toString(stone);
                if (stone == 0L) {
                    newStoneCounts.put(1L, newStoneCounts.getOrDefault(1L, 0L) + count);
                } else if (stoneString.length() % 2 == 0) {
                    int mid = stoneString.length() / 2;
                    long part1 = Long.parseLong(stoneString.substring(0, mid));
                    long part2 = Long.parseLong(stoneString.substring(mid));
                    newStoneCounts.put(part1, newStoneCounts.getOrDefault(part1, 0L) + count);
                    newStoneCounts.put(part2, newStoneCounts.getOrDefault(part2, 0L) + count);
                } else {
                    long multiplied = stone * 2024L;
                    newStoneCounts.put(multiplied, newStoneCounts.getOrDefault(multiplied, 0L) + count);
                }
            }

            stoneCounts = newStoneCounts;
        }

        return stoneCounts.values().stream().mapToLong(Long::longValue).sum();
    }
}

