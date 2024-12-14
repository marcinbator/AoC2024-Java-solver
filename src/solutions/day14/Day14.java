package solutions.day14;

import solutions.Solution;
import solutions.SolutionResponse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Day14 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        List<Guard> guards = new ArrayList<>();
        while (inputScanner.hasNextLine()) {
            var line = inputScanner.nextLine().split(" ");
            var pos = line[0].split("=")[1].split(",");
            var spd = line[1].split("=")[1].split(",");
            guards.add(new Guard(
                    Integer.parseInt(pos[0]), Integer.parseInt(pos[1]),
                    Integer.parseInt(spd[0]), Integer.parseInt(spd[1])
            ));
        }
        int sizeX = guards.size() == 12 ? 11 : 101;
        int sizeY = guards.size() == 12 ? 7 : 103;

        var endGuards = calc1(new ArrayList<>(guards), sizeX, sizeY);
        var end1 = endGuards.stream().filter(g -> g.x < sizeX / 2 && g.y < sizeY / 2).toList();
        var end2 = endGuards.stream().filter(g -> g.x > sizeX / 2 && g.y < sizeY / 2).toList();
        var end3 = endGuards.stream().filter(g -> g.x < sizeX / 2 && g.y > sizeY / 2).toList();
        var end4 = endGuards.stream().filter(g -> g.x > sizeX / 2 && g.y > sizeY / 2).toList();

        return new SolutionResponse(
                (long) end1.size() * end2.size() * end3.size() * end4.size(),
                calc2(new ArrayList<>(guards), sizeX, sizeY)
        );
    }

    record Guard(int x, int y, int speedX, int speedY) {
    }

    private List<Guard> calc1(List<Guard> guards, int sizeX, int sizeY) {
        for (int i = 0; i < 100; i++) {
            guards = getNewGuards(guards, sizeX, sizeY);
        }
        return guards;
    }

    private int calc2(List<Guard> guards, int sizeX, int sizeY) {
        String fileName = "src/solutions/day14/output.txt";
        File file = new File(fileName);
        file.delete();

        for (int i = 0; i < 10000; i++) {
            guards = getNewGuards(guards, sizeX, sizeY);

            int part2 = checkGuards(new ArrayList<>(guards), sizeX, sizeY, i, fileName);
            if (part2 > 0) return part2;
        }
        return 0;
    }

    private static List<Guard> getNewGuards(List<Guard> guards, int sizeX, int sizeY) {
        var newGuards = new ArrayList<Guard>();
        for (var guard : guards) {
            int newX = guard.x + guard.speedX;
            int newY = guard.y + guard.speedY;
            if (newX < 0) newX = sizeX + newX;
            if (newY < 0) newY = sizeY + newY;
            if (newX >= sizeX) newX = newX - sizeX;
            if (newY >= sizeY) newY = newY - sizeY;
            newGuards.add(new Guard(newX, newY, guard.speedX, guard.speedY));
        }
        guards = newGuards;
        return guards;
    }

    private int checkGuards(List<Guard> guards, int sizeX, int sizeY, int second, String fileName) {
        var cords = guards.stream().map(g -> new int[]{g.x, g.y}).toList();

        for (int i = 0; i < sizeY; i++) {
            int finalI = i;
            var line = cords.stream().filter(c -> c[1] == finalI).sorted(Comparator.comparing(c -> c[0])).toList();
            var b = new StringBuilder();
            for (int j = 0; j < sizeX; j++) {
                int finalJ = j;
                if (!line.stream().filter(l -> l[0] == finalJ).toList().isEmpty()) {
                    b.append("#");
                } else b.append(".");
            }
            if (b.toString().contains("###############################")) {
                printGuardsToFile(guards, sizeX, sizeY, second + 1, fileName);
                return second + 1;
            }
        }
        return 0;
    }

    private void printGuardsToFile(List<Guard> guards, int sizeX, int sizeY, int second, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            for (int i = 0; i < sizeX; i++) {
                for (int j = 0; j < sizeY; j++) {
                    int finalI = i;
                    int finalJ = j;
                    if (!guards.stream().filter(g -> g.y == finalI && g.x == finalJ).toList().isEmpty()) {
                        writer.write("#");
                    } else {
                        writer.write(".");
                    }
                }
                writer.newLine();
            }
            writer.write(second + "\n");
        } catch (IOException ignored) {
        }
    }
}
