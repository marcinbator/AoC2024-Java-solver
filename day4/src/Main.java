import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    record Cord(int x, int y) {
    }

    public static void main(String[] args) throws FileNotFoundException {
        var scanner = new Scanner(new File("input.txt"));

        List<List<Character>> input = new ArrayList<>();
        List<Cord> cords1 = new ArrayList<>();
        List<Cord> cords2 = new ArrayList<>();
        int amount1 = 0;
        
        int y = 0;
        while (scanner.hasNextLine()) {
            var line = scanner.nextLine().chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            input.add(line);
            
            for (int i = 0; i < line.size(); i++) {
                if (line.get(i) == 'X')
                    cords1.add(new Cord(i, y));
                if (line.get(i) == 'A')
                    cords2.add(new Cord(i, y));
            }
            y++;
        }

        for (var cord : cords1) {
            var possible = getAllPossibleWordsForCord(cord, input.size(), y);
            for (var pos : possible) {
                var textB = new StringBuilder();
                for (int i = 0; i < 4; i++) {
                    textB.append(input.get(pos.get(i).y).get(pos.get(i).x));
                }
                if (textB.toString().equals("XMAS")) amount1++;
            }
        }
        
        var amount2 = cords2.stream()
                .filter(c->isXForCord(c, input, input.size(), input.getFirst().size()))
                .count();

        System.out.println(amount1);
        System.out.println(amount2);
    }

    private static List<List<Cord>> getAllPossibleWordsForCord(Cord cord, int sizeX, int sizeY) {
        List<List<Cord>> cords = new ArrayList<>();
        List<List<Integer>> mults = new ArrayList<>(List.of(
                List.of(1, 1),
                List.of(-1, -1),
                List.of(1, -1),
                List.of(-1, 1),
                List.of(1, 0),
                List.of(-1, 0),
                List.of(0, 1),
                List.of(0, -1)
        ));
        for (var mult : mults) {
            filterCords(cords, cord, mult.get(0), mult.get(1), sizeX, sizeY);
        }

        return cords;
    }

    private static void filterCords(List<List<Cord>> cords, Cord cord, int xMult, int yMult, int sizeX, int sizeY) {
        List<Cord> subCords = new ArrayList<>(List.of(cord));
        for (int i = 0; i < 3; i++) {
            int x = subCords.getLast().x + xMult;
            int y = subCords.getLast().y + yMult;
            if (isValid(x, y, sizeX, sizeY))
                subCords.add(new Cord(subCords.getLast().x + xMult, subCords.getLast().y + yMult));
        }
        if (subCords.size() == 4)
            cords.add(subCords);
    }

    private static boolean isXForCord(Cord cord, List<List<Character>> input, int sizeX, int sizeY) {
        List<Cord> pair1 = new ArrayList<>(
                List.of(cord, new Cord(cord.x - 1, cord.y - 1), new Cord(cord.x + 1, cord.y + 1))
        );
        List<Cord> pair2 = new ArrayList<>(
                List.of(cord, new Cord(cord.x - 1, cord.y + 1), new Cord(cord.x + 1, cord.y - 1))
        );

        HashSet<Character> chars1 = new HashSet<>();
        HashSet<Character> chars2 = new HashSet<>();
        for (var pair : pair1) {
            if (!isValid(pair.x, pair.y, sizeX, sizeY)) return false;
            chars1.add(input.get(pair.y).get(pair.x));
        }
        for (var pair : pair2) {
            if (!isValid(pair.x, pair.y, sizeX, sizeY)) return false;
            chars2.add(input.get(pair.y).get(pair.x));
        }
        return chars1.containsAll(List.of('M', 'A', 'S')) && chars2.containsAll(List.of('M', 'A', 'S'));
    }

    private static boolean isValid(int x, int y, int sizeX, int sizeY) {
        return x >= 0 && y >= 0 && x < sizeX && y < sizeY;
    }
}