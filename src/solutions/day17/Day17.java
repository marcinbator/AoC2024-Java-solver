package solutions.day17;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.ArrayList;
import java.util.Scanner;

public class Day17 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        long regA = Long.parseLong(inputScanner.nextLine().split(":")[1].strip());
        long regB = Long.parseLong(inputScanner.nextLine().split(":")[1].strip());
        long regC = Long.parseLong(inputScanner.nextLine().split(":")[1].strip());
        inputScanner.nextLine();
        String program = String.join("", inputScanner.nextLine().split(":")[1].strip().split(","));

        var part1 = compute(regA, regB, regC, program);
        System.out.println(part1);

        var nums = new ArrayList<Long>();
        nums.add(0L);

        for (var i = program.length() - 1; i >= 0; i--) {
            var count = nums.size();
            for (var x = 0; x < count; x++) {
                var currentA = nums.get(x) * 8;

                for (var j = 0; j < 8; j++) {
                    var output = compute(currentA + j, 0L, 0L, program);
                    var outputX = String.join("", output.split(","));
                    if (program.substring(i).equals(outputX)) {
                        nums.add(currentA + j);
                    }
                }
            }

            for (int y = 0; y < count; y++) {
                nums.removeFirst();
            }
        }

        var part2 = nums.stream().min(Long::compareTo).get();
        System.out.println(part2);

        return new SolutionResponse(0, 0);
    }

    private String compute(long regA, long regB, long regC, String program) {
        int instPointer = 0;
        var outputB = new StringBuilder();
        while (instPointer < program.length()) {
            int instruction = program.charAt(instPointer) - '0';
            int operand = program.charAt(instPointer + 1) - '0';

            switch (instruction) {
                case 0 -> regA = (regA / powerN(2, getComboOperandValue(operand, regA, regB, regC)));
                case 1 -> regB = xor(regB, operand);
                case 2 -> {
                    String mod = Long.toBinaryString(getComboOperandValue(operand, regA, regB, regC) % 8);
                    regB = mod.length() > 3 ? Long.parseLong(mod.substring(0, 2), 2) : Long.parseLong(mod, 2);
                }
                case 3 -> {
                    if (regA != 0) {
                        instPointer = operand;
                        continue;
                    }
                }
                case 4 -> regB = xor(regB, regC);
                case 5 -> outputB.append(getComboOperandValue(operand, regA, regB, regC) % 8).append(",");
                case 6 -> regB = (regA / powerN(2, getComboOperandValue(operand, regA, regB, regC)));
                case 7 -> regC = (regA / powerN(2, getComboOperandValue(operand, regA, regB, regC)));
            }

            instPointer += 2;

        }
        return outputB.substring(0, outputB.length() - 1);
    }

    private long getComboOperandValue(long operand, long regA, long regB, long regC) {
        return switch ((int) operand) {
            case 0, 1, 2, 3 -> operand;
            case 4 -> regA;
            case 5 -> regB;
            case 6 -> regC;
            default -> -1;
        };
    }

    private long xor(long a, long b) {
        return a ^ b;
    }

    public static long powerN(
            long number,
            long power
    ) {
        if (power == 0) return 1;
        long result = number;

        while (power > 1) {
            result *= number;
            power--;
        }

        return result;
    }
}

