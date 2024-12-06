package solutions.day2;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Day2 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        List<List<Integer>> data = new ArrayList<>();

        while (inputScanner.hasNextLine()) {
            data.add(Arrays.stream(inputScanner.nextLine().split(" ")).map(Integer::parseInt).toList());
        }

        var safe = data.stream()
                .filter(Day2::checkLine)
                .count();

        var safe2 = data.stream()
                .filter(line -> !checkLine(line))
                .filter(line -> IntStream.range(0, line.size())
                        .mapToObj(i -> {
                            var lineCopy = new ArrayList<>(line);
                            lineCopy.remove(i);
                            return lineCopy;
                        })
                        .anyMatch(Day2::checkLine))
                .count();

        return new SolutionResponse(safe, safe + safe2);
    }

    private static boolean checkLine(List<Integer> line) {
        boolean signed = line.get(0) > line.get(1);

        return IntStream.range(0, line.size() - 1)
                .map(i -> line.get(i + 1) - line.get(i))
                .allMatch(d -> (signed ? d < 0 : d > 0) && Math.abs(d) > 0 && Math.abs(d) <= 3);
    }
}

