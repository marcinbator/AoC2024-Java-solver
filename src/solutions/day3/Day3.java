package solutions.day3;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Day3 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        var dataB = new StringBuilder();
        while (inputScanner.hasNextLine()) {
            dataB.append(inputScanner.nextLine());
        }
        String data = dataB.toString();

        Pattern pattern = Pattern.compile("mul\\([0-9]*,[0-9]*\\)|do\\(\\)|don't\\(\\)");
        var matcher = pattern.matcher(data);

        int sum1 = 0;
        int sum2 = 0;
        boolean enabled = true;

        while (matcher.find()) {
            String found = matcher.group();
            if (found.equals("do()")) enabled = true;
            else if (found.equals("don't()")) enabled = false;

            else {
                String[] mul = (matcher.group().substring(4, matcher.group().length() - 1).split(","));
                if (enabled) {
                    sum2 += Integer.parseInt(mul[0]) * Integer.parseInt(mul[1]);
                }
                sum1 += Integer.parseInt(mul[0]) * Integer.parseInt(mul[1]);
            }
        }

        return new SolutionResponse(sum1, sum2);
    }
}
