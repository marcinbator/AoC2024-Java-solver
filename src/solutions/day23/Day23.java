package solutions.day23;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.*;

public class Day23 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        Map<String, List<String>> connections = new HashMap<>();
        while (inputScanner.hasNextLine()) {
            var pair = inputScanner.nextLine().split("-");
            if (connections.containsKey(pair[0])) {
                connections.get(pair[0]).add(pair[1]);
            } else {
                connections.put(pair[0], new ArrayList<>(List.of(pair[1])));
            }
            if (connections.containsKey(pair[1])) {
                connections.get(pair[1]).add(pair[0]);
            } else {
                connections.put(pair[1], new ArrayList<>(List.of(pair[0])));
            }
        }

        var sets = new HashSet<Set<String>>();
        for (var con1 : connections.keySet()) {
            if (!con1.startsWith("t")) continue;
            for (var con2 : connections.get(con1)) {
                for (var con3 : connections.get(con2)) {
                    if (connections.get(con1).contains(con2) && connections.get(con1).contains(con3)) {
                        sets.add(new HashSet<>(Set.of(con1, con2, con3)));
                    }
                }
            }
        }

        System.out.println(sets.size());

        //

        Set<String> largestClique = new HashSet<>();
        Set<String> potentialClique = new HashSet<>();
        Set<String> alreadyFound = new HashSet<>();
        Set<String> remainingNodes = new HashSet<>(connections.keySet());
        findAllConnected(connections, potentialClique, remainingNodes, alreadyFound, largestClique);

        var response = (largestClique.stream().sorted().toList().toString());
        response = String.join("", response.substring(1, response.length() - 1).split(" "));
        System.out.println(response);

        return new SolutionResponse(0, 0);
    }

    private static void findAllConnected( //Bron-Kerbosch algorithm
                                          Map<String, List<String>> connections,
                                          Set<String> potentialClique,
                                          Set<String> remainingNodes,
                                          Set<String> alreadyFound,
                                          Set<String> largestClique
    ) {
        if (remainingNodes.isEmpty() && alreadyFound.isEmpty()) {
            if (potentialClique.size() > largestClique.size()) {
                largestClique.clear();
                largestClique.addAll(potentialClique);
            }
            return;
        }

        for (String node : new HashSet<>(remainingNodes)) {
            potentialClique.add(node);

            Set<String> newRemainingNodes = new HashSet<>(remainingNodes);
            newRemainingNodes.retainAll(connections.get(node));

            Set<String> newAlreadyFound = new HashSet<>(alreadyFound);
            newAlreadyFound.retainAll(connections.get(node));

            findAllConnected(connections, potentialClique, newRemainingNodes, newAlreadyFound, largestClique);

            potentialClique.remove(node);
            remainingNodes.remove(node);
            alreadyFound.add(node);
        }
    }
}

