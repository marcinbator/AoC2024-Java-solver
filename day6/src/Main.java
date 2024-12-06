import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    record Cord(int x, int y) {
    }

    enum Direction {UP, DOWN, LEFT, RIGHT}

    record RouteElem(Cord cord, Direction direction) {

    }

    public static void main(String[] args) throws FileNotFoundException {
        List<List<Character>> map = new ArrayList<>();
        var guardCords = new Cord(0, 0);
        var guardDirection = Direction.UP;

        int sizeY = 0;
        var scanner = new Scanner(new File("input.txt"));
        while (scanner.hasNextLine()) {
            var line = scanner.nextLine();
            map.add(new ArrayList<>());
            for (int i = 0; i < line.length(); i++) {
                map.get(sizeY).add(line.charAt(i));
                var c = map.get(sizeY).get(i);
                if (c == '^' || c == 'v' || c == '<' || c == '>') {
                    guardDirection = getDirection(c);
                    guardCords = new Cord(i, sizeY);
                }
            }
            sizeY++;
        }

        var guardCordsCp = guardCords;
        var guardDirectionCp = guardDirection;
        var visited = new HashSet<>(Collections.singleton(guardCordsCp));
        while (guardCordsCp.x >= 0 && guardCordsCp.y >= 0 && guardCordsCp.x < map.getFirst().size() && guardCordsCp.y < map.size()) {
            var lookingAt = getLookingAt(guardCordsCp, guardDirectionCp);
            if (lookingAt.x >= 0 && lookingAt.y >= 0 && lookingAt.y < sizeY && lookingAt.x < map.getFirst().size() &&
                    map.get(lookingAt.y).get(lookingAt.x) == '#') guardDirectionCp = turn(guardDirectionCp);
            else guardCordsCp = lookingAt;
            visited.add(guardCordsCp);
        }

        int loops = 0;
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.getFirst().size(); j++) {
                if (map.get(i).get(j) != '.') continue;

                List<List<Character>> mapCopy = new ArrayList<>();
                for (List<Character> row : map) {
                    mapCopy.add(new ArrayList<>(row));
                }
                mapCopy.get(i).set(j, '#');
                if (isLoop(mapCopy, guardCords, guardDirection)) loops++;
            }
        }
        System.out.println(visited.size() - 1);
        System.out.println(loops);
    }

    private static Direction turn(Direction direction) {
        return switch (direction) {
            case UP -> Direction.RIGHT;
            case RIGHT -> Direction.DOWN;
            case DOWN -> Direction.LEFT;
            case LEFT -> Direction.UP;
        };
    }

    private static Direction getDirection(Character c) {
        return switch (c) {
            case '^' -> Direction.UP;
            case 'v' -> Direction.DOWN;
            case '<' -> Direction.LEFT;
            case '>' -> Direction.RIGHT;
            default -> throw new IllegalStateException("Unexpected value: " + c);
        };
    }

    private static boolean isLoop(List<List<Character>> map, Cord guardCords, Direction guardDirection) {
        var visitedList = new HashSet<>(Collections.singleton(new RouteElem(guardCords, guardDirection)));
        while (guardCords.x >= 0 && guardCords.y >= 0 && guardCords.x < map.getFirst().size() && guardCords.y < map.size()) {
            var lookingAt = getLookingAt(guardCords, guardDirection);
            if (lookingAt.x >= 0 && lookingAt.y >= 0 && lookingAt.y < map.size() && lookingAt.x < map.getFirst().size() &&
                    map.get(lookingAt.y).get(lookingAt.x) == '#') guardDirection = turn(guardDirection);
            else guardCords = lookingAt;

            if (visitedList.contains(new RouteElem(guardCords, guardDirection))) return true;

            visitedList.add(new RouteElem(guardCords, guardDirection));
        }
        return false;
    }

    private static Cord getLookingAt(Cord guardCords, Direction guardDirection) {
        return switch (guardDirection) {
            case UP -> new Cord(guardCords.x, guardCords.y - 1);
            case DOWN -> new Cord(guardCords.x, guardCords.y + 1);
            case LEFT -> new Cord(guardCords.x - 1, guardCords.y);
            case RIGHT -> new Cord(guardCords.x + 1, guardCords.y);
        };
    }
}