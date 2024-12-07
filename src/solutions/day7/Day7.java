package solutions.day7;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day7 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        long sum1 = 0;
        long sum2 = 0;

        while (inputScanner.hasNextLine()) {
            var split = inputScanner.nextLine().split(":");
            long result = Long.parseLong(split[0]);

            List<Long> numbers = new ArrayList<>(
                    Arrays.stream(split[1].trim().split(" "))
                            .map(Long::parseLong)
                            .toList()
            );

            List<List<Character>> operatorsSets = new ArrayList<>();
            generateCombinations(List.of('+', '*', '|'), new ArrayList<>(), numbers.size() - 1, operatorsSets);

            for (var set : operatorsSets) {
                long testResult1 = numbers.getFirst();
                long testResult2 = numbers.getFirst();

                for (int i = 1; i < numbers.size(); i++) {
                    if (set.get(i - 1) == '+') {
                        testResult1 += numbers.get(i);
                        testResult2 += numbers.get(i);
                    } else if (set.get(i - 1) == '*') {
                        testResult1 *= numbers.get(i);
                        testResult2 *= numbers.get(i);
                    } else {
                        testResult2 = Long.parseLong(testResult2 + numbers.get(i).toString());
                    }
                }
                if (testResult1 == result) sum1 += result;
                if (testResult2 == result) {
                    sum2 += result;
                    break;
                }
            }
        }

        return new SolutionResponse(sum1, sum2);
    }

    private static void generateCombinations(List<Character> list, List<Character> presentCombination,
                                             int size, List<List<Character>> targetList
    ) {
        if (presentCombination.size() == size) {
            targetList.add(new ArrayList<>(presentCombination));
            return;
        }

        for (var element : list) {
            presentCombination.add(element);
            generateCombinations(list, presentCombination, size, targetList);
            presentCombination.removeLast();
        }
    }

}

