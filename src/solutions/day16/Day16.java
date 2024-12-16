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

        var queue = new PriorityQueue<TileQ>(Comparator.comparing(t -> t.totalCost));
        queue.add(new TileQ(start, 0, calcDistance(start, end), Direction.E, null));

        var visitedCosts = new HashMap<Tile, Integer>();
        var visitedDirections = new HashMap<Tile, Direction>();
        int total = 0;

        var visited = new ArrayList<TileQ>();
        var path = new HashSet<TileQ>();

        while (!queue.isEmpty()) {
            var current = queue.poll();
            if (current.tile.equals(end)) {
                total = current.gCost;
                var copy = new PriorityQueue<TileQ>(Comparator.comparing(t -> t.totalCost));
                visited.add(current);
                copy.add(current);
                while (!copy.isEmpty()) {
                    TileQ finalCopyElement = copy.poll();
                    var same = visited.stream().filter(v -> v.tile == finalCopyElement.tile && v.parent != null && v.parent.gCost < finalCopyElement.gCost).map(v -> v.parent).filter(v -> v.tile.value == '.').toList();
                    path.addAll(same);
                    copy.addAll(same);
                }
                break;
            }
            if (visitedCosts.containsKey(current.tile) && visitedDirections.get(current.tile) == current.direction
                    && visitedCosts.get(current.tile) <= current.gCost) {
                continue;
            }

            visitedCosts.put(current.tile, current.gCost);
            visitedDirections.put(current.tile, current.direction);
            visited.add(current);
            var neighbors = getNeighs(current, tiles, end);

            queue.addAll(neighbors);
        }

        System.out.println(total);
        System.out.println(path.stream().map(p -> p.tile).filter(p -> p.value == '.').distinct().toList().size() + 2);

        return new SolutionResponse(total, path.stream().map(p -> p.tile).filter(p -> p.value == '.').distinct().toList().size() + 2);
    }

    enum Direction {E, W, N, S}

    record Tile(int x, int y, char value) {
    }

    record TileQ(Tile tile, int gCost, int totalCost, Direction direction, TileQ parent) {
    }

    record NeighDir(int dx, int dy, Direction direction) {
    }

    int calcDistance(Tile from, Tile to) {
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

            int moveCost = current.gCost + getTurnCost(current.direction, dir.direction);
            int heuristic = calcDistance(neighbor, end);

            neighbors.add(new TileQ(neighbor, moveCost, moveCost + heuristic, dir.direction, current));
        }

        return neighbors;
    }

    int getTurnCost(Direction from, Direction to) {
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
