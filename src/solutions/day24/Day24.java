package solutions.day24;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.*;

public class Day24 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        Map<String, Integer> values = new HashMap<>();
        List<Operation> operations = new ArrayList<>();

        var line = inputScanner.nextLine();
        while (!line.isEmpty()) {
            String[] tokens = line.split(": ");
            values.put(tokens[0], Integer.parseInt(tokens[1]));
            line = inputScanner.nextLine();
        }
        while (inputScanner.hasNextLine()) {
            var tokens = inputScanner.nextLine().split(" ");
            operations.add(new Operation(tokens[0], tokens[2], OpType.valueOf(tokens[1]), tokens[4]));
        }

        List<Operation> done = new ArrayList<>();
        while (done.size() < operations.size()) {
            for (var operation : operations) {
                if (done.contains(operation)) continue;
                if (values.containsKey(operation.a) && values.containsKey(operation.b)) {
                    done.add(operation);
                    values.put(operation.resultDest, calc(
                            values.get(operation.a),
                            values.get(operation.b),
                            operation.type
                    ));
                }
            }
        }

        var outputReg = values.keySet().stream().filter(v -> v.startsWith("z")).sorted(Comparator.reverseOrder()).toList();
        var outputBuilder = new StringBuilder();
        for (var key : outputReg) {
            outputBuilder.append(values.get(key));
        }
        var result = Long.parseLong(outputBuilder.toString(), 2);
        System.out.println(result);

        return new SolutionResponse(0, 0);
    }

    private enum OpType {AND, OR, XOR}

    private record Operation(String a, String b, OpType type, String resultDest) {
    }

    private int calc(int a, int b, OpType type) {
        return switch (type) {
            case AND -> a & b;
            case OR -> a | b;
            case XOR -> a ^ b;
        };
    }
}

