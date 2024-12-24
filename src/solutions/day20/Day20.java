package solutions.day20;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.*;

public class Day20 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        List<List<Character>> board = new ArrayList<>();
        Tile start = null;
        Tile end = null;
        int sizeY = 0;
        while (inputScanner.hasNextLine()) {
            String line = inputScanner.nextLine();
            List<Character> row = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                char element = line.charAt(i);
                row.add(element);
                if (element == 'S') start = new Tile(i, sizeY, 0, 0, null, CheatState.NO);
                if (element == 'E') end = new Tile(i, sizeY, 0, 0, null, CheatState.NO);
            }
            board.add(row);
            sizeY++;
        }
        assert start != null;
        assert end != null;

        //

        int normalCost = 0;
        var normal = new PriorityQueue<Tile>(Comparator.comparingInt(t -> t.totalCost));
        normal.add(start);
        var visitedCosts = new HashMap<Tile, Integer>();
        while (!normal.isEmpty()) {
            var tile = normal.poll();
            if (tile.equals(end)) normalCost = tile.cost;
            if (visitedCosts.containsKey(tile) && visitedCosts.get(tile) <= tile.cost) continue;

            visitedCosts.put(tile, tile.cost);
            normal.addAll(getNeighbors(tile, board));
        }

        int cheatedCost = 0;
        var cheated = new PriorityQueue<Tile>(Comparator.comparingInt(t -> t.totalCost));
        cheated.add(start);
        var cheatedVisitedCost = new HashMap<Tile, Integer>();
        while (!cheated.isEmpty()) {
            var tile = cheated.poll();
            if (tile.equals(end)) cheatedCost = tile.cost;
            if (cheatedVisitedCost.containsKey(tile) && cheatedVisitedCost.get(tile) <= tile.cost) continue;

            cheatedVisitedCost.put(tile, tile.cost);
            cheated.addAll(getCheatedNeighbors(tile, board));
        }

        System.out.println(normalCost);
        System.out.println(cheatedCost);
        System.out.println(normalCost - cheatedCost);

        return new SolutionResponse(0, 0);
    }

    record Tile(int x, int y, int cost, int totalCost, Tile parent, CheatState state) {
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

    enum CheatState {
        NO, STARTED, ENDED
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
                    new Tile(nx, ny, tile.cost + 1, tile.cost + 1 + calcDistance(nx, ny, board.size() - 1), tile, CheatState.NO)
            );
        }
        return neighbors;
    }

    private List<Tile> getCheatedNeighbors(Tile tile, List<List<Character>> board) {
        List<Tile> neighbors = new ArrayList<>();
        var directions = new int[][]{{1, 0}, {0, -1}, {-1, 0}, {0, 1},};
        for (var dir : directions) {
            int nx = tile.x + dir[1];
            int ny = tile.y + dir[0];

            if (ny < 0 || ny >= board.size() || nx < 0 || nx >= board.get(ny).size()) continue;
            var neighbor = board.get(ny).get(nx);

            if (neighbor == '#') {
                if (tile.state.equals(CheatState.NO))
                    tile = new Tile(tile.x, tile.y, tile.cost, tile.totalCost, tile.parent, CheatState.STARTED);
                else continue;
            } else if (tile.state.equals(CheatState.STARTED)) {
                tile = new Tile(tile.x, tile.y, tile.cost, tile.totalCost, tile.parent, CheatState.ENDED);
            }

            neighbors.add(
                    new Tile(nx, ny, tile.cost + 1, tile.cost + 1 + calcDistance(nx, ny, board.size() - 1), tile, CheatState.NO)
            );
        }
        return neighbors;
    }

    private int calcDistance(int x, int y, int size) {
        return Math.abs(x - size) + Math.abs(y - size);
    }
}

