package solutions.day12;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.*;
import java.util.stream.Stream;

public class Day12 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        List<List<Character>> map = new ArrayList<>();
        int sizeY = 0;

        while (inputScanner.hasNextLine()) {
            String line = inputScanner.nextLine();
            var mapLine = new ArrayList<Character>();
            for (char c : line.toCharArray()) {
                mapLine.add(c);
            }
            map.add(mapLine);
            sizeY++;
        }

        int sizeX = map.getFirst().size();
        boolean[][] visited = new boolean[sizeY][sizeX];
        long totalSum = 0;
        long corners = 0;

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                if (!visited[y][x]) {
                    long[] plotData = explorePlot(x, y, map, visited);
                    long size = plotData[0];
                    long perpSum = plotData[1];
                    long cornersSum = plotData[2];
                    totalSum += size * perpSum;
                    corners += size * cornersSum;
                }
            }
        }

        return new SolutionResponse(totalSum, corners);
    }

    private long[] explorePlot(int startX, int startY, List<List<Character>> map, boolean[][] visited) {
        int size = 0;
        long perpSum = 0;
        long cornersSum = 0;
        char targetChar = map.get(startY).get(startX);
        int sizeY = map.size();
        int sizeX = map.getFirst().size();

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0], y = current[1];

            if (visited[y][x]) continue;
            visited[y][x] = true;

            size++;
            perpSum += calcPerp(x, y, targetChar, map);
            cornersSum += calcCorners(x, y, targetChar, map);

            for (int[] dir : new int[][]{{0, -1}, {1, 0}, {0, 1}, {-1, 0}}) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (newX >= 0 && newY >= 0 && newX < sizeX && newY < sizeY
                        && !visited[newY][newX] && map.get(newY).get(newX) == targetChar) {
                    queue.add(new int[]{newX, newY});
                }
            }
        }

        return new long[]{size, perpSum, cornersSum};
    }

    private int calcPerp(int x, int y, char value, List<List<Character>> map) {
        int perp = 4;
        int sizeY = map.size();
        int sizeX = map.getFirst().size();

        for (int[] dir : new int[][]{{0, -1}, {1, 0}, {0, 1}, {-1, 0}}) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            if (newX >= 0 && newY >= 0 && newX < sizeX && newY < sizeY) {
                if (map.get(newY).get(newX) == value) {
                    perp--;
                }
            }
        }
        return perp;
    }

    private int calcCorners(int x, int y, char value, List<List<Character>> map) {
        int corners = 0;
        int sizeY = map.size();
        int sizeX = map.getFirst().size();

        boolean top = y - 1 >= 0 && map.get(y - 1).get(x) == value;
        boolean bottom = y + 1 < sizeY && map.get(y + 1).get(x) == value;
        boolean left = x - 1 >= 0 && map.get(y).get(x - 1) == value;
        boolean right = x + 1 < sizeX && map.get(y).get(x + 1) == value;
        boolean tl = y - 1 >= 0 && x - 1 >= 0 && map.get(y - 1).get(x - 1) == value;
        boolean tr = y - 1 >= 0 && x + 1 < sizeX && map.get(y - 1).get(x + 1) == value;
        boolean bl = y + 1 < sizeY && x - 1 >= 0 && map.get(y + 1).get(x - 1) == value;
        boolean br = y + 1 < sizeY && x + 1 < sizeX && map.get(y + 1).get(x + 1) == value;

        var list = new ArrayList<>(Stream.of(top, bottom, left, right, tl, tr, bl, br).filter(a -> a).toList());
        var neigh = new ArrayList<>(Stream.of(top, bottom, left, right).filter(a -> a).toList());

        if (list.isEmpty()) return 4;
        if (list.size() == 8) return 0;
        if (neigh.size() == 1) return 2;
        if (neigh.isEmpty()) return 4;
        if ((top && bottom && !left && !right) || !top && !bottom && left && right) return 0;

        if (top && left) {
            if (!tl) corners++;
            if (!bottom && !right) corners++;
        }
        if (top && right) {
            if (!tr) corners++;
            if (!bottom && !left) corners++;
        }
        if (bottom && left) {
            if (!bl) corners++;
            if (!top && !right) corners++;
        }
        if (bottom && right) {
            if (!br) corners++;
            if (!top && !left) corners++;
        }

        return corners;
    }
}

