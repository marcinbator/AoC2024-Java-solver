package solutions.day12;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.*;

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

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                if (!visited[y][x]) {
                    long[] plotData = explorePlot(x, y, map, visited);
                    long size = plotData[0];
                    long perpSum = plotData[1];
                    totalSum += size * perpSum;
                }
            }
        }

        System.out.println(totalSum);
        return new SolutionResponse(0, 0);
    }

    private long[] explorePlot(int startX, int startY, List<List<Character>> map, boolean[][] visited) {
        int size = 0;
        long perpSum = 0;
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

            for (int[] dir : new int[][]{{0, -1}, {1, 0}, {0, 1}, {-1, 0}}) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (newX >= 0 && newY >= 0 && newX < sizeX && newY < sizeY
                        && !visited[newY][newX] && map.get(newY).get(newX) == targetChar) {
                    queue.add(new int[]{newX, newY});
                }
            }
        }

        return new long[]{size, perpSum};
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
}

