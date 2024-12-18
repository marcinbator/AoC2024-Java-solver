package solutions.day18;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.*;
import java.util.stream.Stream;

public class Day18 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        String line = inputScanner.nextLine();
        int boardSize = line.equals("5,4") ? 7 : 71;
        int bytesLimit = line.equals("5,4") ? 12 : 1024;

        List<List<Character>> board = new ArrayList<>(boardSize);
        for (int i = 0; i < boardSize; i++) {
            var boardLine = new ArrayList<Character>(boardSize);
            for (int j = 0; j < boardSize; j++) {
                boardLine.add('.');
            }
            board.add(boardLine);
        }
        List<Integer[]> cords = new ArrayList<>();
        while (inputScanner.hasNextLine()) {
            var cord = Stream.of(line.split(",")).map(Integer::valueOf).toArray(Integer[]::new);
            line = inputScanner.nextLine();
            cords.add(cord);
        }
        
        int part1=0;
        String part2;

        //1
        fillBoard(bytesLimit, cords, board);
        try {
            part1 = getTotalCost(board);
        } catch (Exception ignored) {}

        //2
        String lastByte = null;
        while (true) {
            bytesLimit++;
            try {
                lastByte = fillBoard(bytesLimit, cords, board);
                getTotalCost(board);
            } catch (Exception e) {
                assert lastByte != null;
                var result = String.join("", lastByte.split(" "));
                part2 = result.substring(1, result.length()-1);
                break;
            }
        }
        System.out.println(part2);

        return new SolutionResponse(part1, 0);
    }

    private static String fillBoard(int bytes, List<Integer[]> cords, List<List<Character>> board) {
        int fallen = 0;
        int index = 0;
        int boardSize = board.size();
        Integer[] cord = null;
        while (fallen < bytes) {
            cord = cords.get(index);
            index++;
            if (cord[0] >= 0 && cord[0] < boardSize && cord[1] >= 0 && cord[1] < boardSize) {
                board.get(cord[1]).set(cord[0], '#');
                fallen++;
            }
        }
        assert cord != null;
        return Arrays.toString(cord);
    }

    private int getTotalCost(List<List<Character>> board) throws Exception {
        int boardSize = board.size();
        var queue = new PriorityQueue<Tile>(Comparator.comparingInt(t -> t.totalCost));
        queue.add(new Tile(0, 0, 0, 0, null));
        var visitedCosts = new HashMap<Tile, Integer>();

        while (!queue.isEmpty()) {
            var tile = queue.poll();
            if (tile.x == boardSize - 1 && tile.y == boardSize - 1) return tile.cost;
            if (visitedCosts.containsKey(tile) && visitedCosts.get(tile) <= tile.cost) continue;

            visitedCosts.put(tile, tile.cost);
            queue.addAll(getNeighbors(tile, board));
        }
        
        throw new Exception("No route");
    }

    record Tile(int x, int y, int cost, int totalCost, Tile parent) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tile tile = (Tile) o;
            return x == tile.x && y == tile.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private List<Tile> getNeighbors(Tile tile, List<List<Character>> board) {
        List<Tile> neighbors = new ArrayList<>();
        var directions = new int[][]{{1, 0}, {0, -1}, {-1, 0}, {0, 1},};
        for (var dir : directions) {
            int nx = tile.x + dir[1];
            int ny = tile.y + dir[0];

            if (ny < 0 || ny >= board.size() || nx < 0 || nx >= board.get(ny).size()) continue;
            var neighbor = board.get(ny).get(nx);
            if (neighbor == '#') continue;

            neighbors.add(
                    new Tile(nx, ny, tile.cost + 1, tile.cost + 1 + calcDistance(nx, ny, board.size() - 1), tile)
            );
        }
        return neighbors;
    }

    private int calcDistance(int x, int y, int size) {
        return Math.abs(x - size) + Math.abs(y - size);
    }
}
