package solutions.day10;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.*;

public class Day10 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        List<List<Cord>> map = new ArrayList<>();
        List<Cord> currentCords = new ArrayList<>();

        int sizeY = 0;
        while (inputScanner.hasNextLine()) {
            String line = inputScanner.nextLine();
            List<Cord> cordsLine = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                int value = line.charAt(i) - '0';
                cordsLine.add(new Cord(i, sizeY, value, null, null));
                if (value == 0) {
                    var cord = cordsLine.getLast();
                    var cord2 = new Cord(cord.x, cord.y, cord.value, cord.x, cord.y);
                    currentCords.add(cord2);
                }
            }
            map.add(cordsLine);
            sizeY++;
        }

        for (int i = 0; i < 9; i++) {
            currentCords = getNextCords(currentCords, map);
        }

        var routesMap = new HashMap<Pair, Set<Cord>>();
        for (int i = 0; i < currentCords.size(); i++) {
            var cord = currentCords.get(i);
            var list = currentCords.stream()
                    .filter(c -> c.beginX.equals(cord.beginX) && c.beginY.equals(cord.beginY))
                    .toList();
            routesMap.put(new Pair(cord.beginX, cord.beginY), new HashSet<>(list));
        }
        long sum = routesMap.values().stream().map(Set::size).mapToInt(Integer::intValue).sum();

        return new SolutionResponse(sum, currentCords.size());
    }

    record Cord(int x, int y, int value, Integer beginX, Integer beginY) {
    }

    record Pair(Integer x, Integer y) {
    }

    List<Cord> getNextCords(List<Cord> cords, List<List<Cord>> map) {
        List<Cord> nextCords = new ArrayList<>();
        var directions = new ArrayList<>(List.of(
                new Pair(1, 0),
                new Pair(0, 1),
                new Pair(-1, 0),
                new Pair(0, -1)
        ));
        for (var cord : cords) {
            for (var dir : directions) {
                int newX = cord.x + dir.x;
                int newY = cord.y + dir.y;
                if (newX < 0 || newY < 0 || newY >= map.size() || newX >= map.getFirst().size()) continue;
                if (map.get(newY).get(newX).value - cord.value != 1) continue;
                nextCords.add(new Cord(newX, newY, map.get(newY).get(newX).value, cord.beginX, cord.beginY));
            }
        }
        return nextCords;
    }
}

