package solutions.day16;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.*;

public class Day16 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        List<List<Tile>> tiles = new ArrayList<>();
        Tile start = null;
        Tile end = null;

        int sizeY = 0;
        while (inputScanner.hasNextLine()) {
            String line = inputScanner.nextLine();
            List<Tile> tileLine = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                var c = line.charAt(i);
                Tile tile = new Tile(i, sizeY, c);
                tileLine.add(tile);
                if (c == 'S') start = tile;
                if (c == 'E') end = tile;
            }
            tiles.add(tileLine);
            sizeY++;
        }
        assert start != null;
        assert end != null;

        var queue = new PriorityQueue<TileQ>(Comparator.comparingDouble(t -> t.totalCost));
        queue.add(new TileQ(start, 0, calcDistance(start, end), Direction.E));

        var visitedCosts = new HashMap<Tile, Double>();
        var visitedDirections = new HashMap<Tile, Direction>();
        double total=0;
        
        while (!queue.isEmpty()) {
            var current = queue.poll();
            if (current.tile.equals(end)) {
                total=current.gCost;
                break;
            }
            if (visitedCosts.containsKey(current.tile) && visitedDirections.get(current.tile) == current.direction
                    && visitedCosts.get(current.tile) <= current.gCost) {
                continue;
            }
            
            visitedCosts.put(current.tile, current.gCost);
            visitedDirections.put(current.tile, current.direction);
            var neighbors = getNeighs(current, tiles, end);
            
            queue.addAll(neighbors);
        }

        System.out.println(total);
       
        return new SolutionResponse(0, 0);
    }

    enum Direction {E, W, N, S}
    record Tile(int x, int y, char value) {
    }
    record TileQ(Tile tile, double gCost, double totalCost, Direction direction) {
    }
    record NeighDir(int dx, int dy, Direction direction) {
    }

    double calcDistance(Tile from, Tile to) {
        return Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
    }

    List<TileQ> getNeighs(TileQ current, List<List<Tile>> tiles, Tile end) {
        var directions = List.of(
                new NeighDir(1, 0, Direction.E),
                new NeighDir(0, -1, Direction.S),
                new NeighDir(-1, 0, Direction.W),
                new NeighDir(0, 1, Direction.N)
        );

        List<TileQ> neighbors = new ArrayList<>();

        for (var dir : directions) {
            int nx = current.tile.x + dir.dx;
            int ny = current.tile.y + dir.dy;

            if (ny < 0 || ny >= tiles.size() || nx < 0 || nx >= tiles.get(ny).size()) continue;

            Tile neighbor = tiles.get(ny).get(nx);
            if (neighbor.value == '#') continue;

            double moveCost = current.gCost + getTurnCost(current.direction, dir.direction);
            double heuristic = calcDistance(neighbor, end);

            neighbors.add(new TileQ(neighbor, moveCost, moveCost + heuristic, dir.direction));
        }

        return neighbors;
    }

    double getTurnCost(Direction from, Direction to) {
        return switch (from) {
            case N -> switch (to) {
                case N -> 1;
                case E, W -> 1001;
                case S -> 2001;
            };
            case E -> switch (to) {
                case E -> 1;
                case S, N -> 1001;
                case W -> 2001;
            };
            case S -> switch (to) {
                case S -> 1;
                case E, W -> 1001;
                case N -> 2001;
            };
            case W -> switch (to) {
                case W -> 1;
                case S, N -> 1001;
                case E -> 2001;
            };
        };
    }
}
