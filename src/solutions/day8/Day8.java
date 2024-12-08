package solutions.day8;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.*;

public class Day8 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        List<List<Character>> map = new ArrayList<>();
        List<Cord> antennas = new ArrayList<>();

        int sizeY = 0;
        while (inputScanner.hasNextLine()) {
            String line = inputScanner.nextLine();
            List<Character> row = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                var character = line.toCharArray()[i];
                row.add(character);
                if ((character >= 48 && character <= 57) || (character >= 65 && character <= 90) || (character >= 97 && character <= 122)) {
                    antennas.add(new Cord(i, sizeY));
                }
            }
            map.add(row);
            sizeY++;
        }

        Set<Cord> nodes1 = new HashSet<>();
        Set<Cord> nodes2 = new HashSet<>();

        for (int i = 0; i < antennas.size() - 1; i++) {
            for (int j = i + 1; j < antennas.size(); j++) {
                Cord antenna1 = antennas.get(i);
                Cord antenna2 = antennas.get(j);
                if (map.get(antenna1.y).get(antenna1.x) == map.get(antenna2.y).get(antenna2.x)) {
                    nodes1.addAll(getAllPossible1(antenna1, antenna2, map.getFirst().size(), sizeY));
                    nodes2.addAll(getAllPossible2(antenna1, antenna2, map.getFirst().size(), sizeY));
                }
            }
        }

        return new SolutionResponse(nodes1.size(), nodes2.size());
    }

    record Cord(int x, int y) {
    }

    private Set<Cord> getAllPossible1(Cord c1, Cord c2, int sizeX, int sizeY) {
        int distanceX = c1.x - c2.x;
        int distanceY = c1.y - c2.y;
        var set = new HashSet<>(Set.of(
                new Cord(c1.x + distanceX, c1.y + distanceY),
                new Cord(c1.x - distanceX, c1.y - distanceY),
                new Cord(c2.x + distanceX, c2.y + distanceY),
                new Cord(c2.x - distanceX, c2.y - distanceY))
        );
        var finalSet = new HashSet<Cord>();
        for (var e : set) {
            if (e.x >= 0 && e.y >= 0 && e.x < sizeX & e.y < sizeY & e.x != c1.x && e.x != c2.x & e.y != c1.y && e.y != c2.y)
                finalSet.add(e);
        }
        return finalSet;
    }

    private Set<Cord> getAllPossible2(Cord c1, Cord c2, int sizeX, int sizeY) {
        int distanceX = c1.x - c2.x;
        int distanceY = c1.y - c2.y;

        var set = new HashSet<>(Set.of(c1));

        Cord p = new Cord(c1.x + distanceX, c1.y + distanceY);
        while (p.x >= 0 && p.y >= 0 && p.x < sizeX && p.y < sizeY) {
            set.add(p);
            p = new Cord(p.x + distanceX, p.y + distanceY);
        }
        p = new Cord(c1.x - distanceX, c1.y - distanceY);
        while (p.x >= 0 && p.y >= 0 && p.x < sizeX && p.y < sizeY) {
            set.add(p);
            p = new Cord(p.x - distanceX, p.y - distanceY);
        }

        return set;
    }
}

