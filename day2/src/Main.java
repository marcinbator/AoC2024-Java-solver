import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        List<List<Integer>> data = new ArrayList<>();

        Scanner scanner = new Scanner(new File("input.txt"));
        while (scanner.hasNextLine()) {
            data.add(Arrays.stream(scanner.nextLine().split(" ")).map(Integer::parseInt).toList());
        }

        var safe = data.stream()
                .filter(Main::checkLine)
                .count();

        var safe2 = data.stream()
                .filter(line -> !checkLine(line))
                .filter(line -> IntStream.range(0, line.size())
                        .mapToObj(i -> {
                            var lineCopy = new ArrayList<>(line);
                            lineCopy.remove(i);
                            return lineCopy;
                        })
                        .anyMatch(Main::checkLine))
                .count();

        System.out.println(safe);
        System.out.println(safe + safe2);
    }

    private static boolean checkLine(List<Integer> line) {
        boolean signed = line.get(0) > line.get(1);

        return IntStream.range(0, line.size() - 1)
                .map(i -> line.get(i+1) - line.get(i))
                .allMatch(d ->(signed ? d<0 : d>0) && Math.abs(d) > 0 && Math.abs(d) <= 3);
    }
}