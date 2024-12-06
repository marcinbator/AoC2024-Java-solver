package solutions.day1;

import solutions.Solution;
import solutions.SolutionResponse;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Day1 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        var col1 = new PriorityQueue<Integer>();
        var col2 = new PriorityQueue<Integer>();

        readFile(col1, col2, inputScanner);

        int sum = 0;
        var occurrences = new HashMap<Integer, Integer>();

        var col1copy = new PriorityQueue<>(col1);
        var col2copy = new PriorityQueue<>(col2);

        while (!col1copy.isEmpty()) {
            int min1 = col1copy.poll();
            assert !col2copy.isEmpty();
            int min2 = col2copy.poll();

            occurrences.put(min1, occurrences.getOrDefault(min1,
                    Math.toIntExact(col2.stream()
                            .filter(l -> l.equals(min1))
                            .count())
            ));

            sum += Math.abs(min1 - min2);
        }
        
        return new SolutionResponse(sum, col1.stream()
                .map(l -> l * occurrences.get(l))
                .mapToInt(Integer::intValue)
                .sum());
    }

    private static void readFile(PriorityQueue<Integer> col1, PriorityQueue<Integer> col2, Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int l1 = Integer.parseInt(line.split("\\s+")[0]);
            int l2 = Integer.parseInt(line.split("\\s+")[1]);
            col1.add(l1);
            col2.add(l2);
        }
    }
}

