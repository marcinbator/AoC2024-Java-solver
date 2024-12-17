package solutions.day17;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.Scanner;

public class Day17 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        int regA = Integer.parseInt(inputScanner.nextLine().split(":")[1].strip());
        int regB = Integer.parseInt(inputScanner.nextLine().split(":")[1].strip());
        int regC = Integer.parseInt(inputScanner.nextLine().split(":")[1].strip());
        inputScanner.nextLine();
        String program = String.join("", inputScanner.nextLine().split(":")[1].strip().split(","));

        int instPointer = 0;
        var outputB = new StringBuilder();

        while (instPointer < program.length()) {
            int instruction = program.charAt(instPointer) - '0';
            int operand = program.charAt(instPointer + 1) - '0';

          
            switch (instruction) {
                case 0 -> regA = (int) (regA / Math.pow(2, getComboOperandValue(operand, regA, regB, regC)));
                case 1 -> {
                    String l1 = Integer.toBinaryString(regB);
                    String l2 = Integer.toBinaryString(operand);
                    regB = Integer.parseInt(xor(l1, l2), 2);
                }
                case 2 -> {
                    String mod = Integer.toBinaryString(getComboOperandValue(operand, regA, regB, regC) % 8);
                    regB = mod.length() > 3 ? Integer.parseInt(mod.substring(0, 2), 2) : Integer.parseInt(mod, 2);
                }
                case 3 -> {
                    if (regA != 0) {
                        instPointer = operand;
                        continue;
                    }
                }
                case 4 -> {
                    String l1 = Integer.toBinaryString(regB);
                    String l2 = Integer.toBinaryString(regC);
                    regB = Integer.parseInt(xor(l1, l2), 2);
                }
                case 5 -> outputB.append(getComboOperandValue(operand, regA, regB, regC) % 8).append(",");
                case 6 -> regB = (int) (regA / Math.pow(2, getComboOperandValue(operand, regA, regB, regC)));
                case 7 -> regC = (int) (regA / Math.pow(2, getComboOperandValue(operand, regA, regB, regC)));
            }


            instPointer += 2;
        }

        System.out.println(outputB);

        return new SolutionResponse(0, 0);
    }

    private int getComboOperandValue(int operand, int regA, int regB, int regC) {
        return switch (operand) {
            case 0, 1, 2, 3 -> operand;
            case 4 -> regA;
            case 5 -> regB;
            case 6 -> regC;
            default ->-1;
        };
    }

    private String xor(String a, String b) {
        StringBuilder ans = new StringBuilder();
        StringBuilder aBuilder = new StringBuilder(a);
        while (aBuilder.length() < b.length()) {
            aBuilder.insert(0, "0");
        }
        a = aBuilder.toString();
        StringBuilder bBuilder = new StringBuilder(b);
        while (bBuilder.length() < a.length()) {
            bBuilder.insert(0, "0");
        }
        b = bBuilder.toString();
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == b.charAt(i)) ans.append("0");
            else ans.append("1");
        }
        return ans.toString();
    }
}

