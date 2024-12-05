import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;

public class Main {
    record Rule(int left, int right) {
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Rule> rules = new ArrayList<>();
        List<List<Integer>> queues = new ArrayList<>();

        var scanner = new Scanner(new File("input.txt"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) break;
            var split = line.split("\\|");
            rules.add(new Rule(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            queues.add(new ArrayList<>(Arrays.stream(line.split(",")).map(Integer::parseInt).toList()));
        }

        int sum1 = 0;
        int sum2 = 0;
        for (var queue : queues) {
            var filteredRules = rules.stream().filter(r -> new HashSet<>(queue).containsAll(List.of(r.left, r.right))).toList();
            if (isValid(queue, filteredRules)) {
                sum1 += queue.get(queue.size() / 2);
            } else {
                while (!isValid(queue, filteredRules)) {
                    fix(queue, filteredRules);
                }
                sum2 += queue.get(queue.size() / 2);
            }
        }

        System.out.println(sum1);
        System.out.println(sum2);
    }

    private static boolean isValid(List<Integer> queue, List<Rule> rules) {
        for (var rule : rules) {
            var leftIndexes = findIndexes(queue, rule, false);
            var rightIndexes = findIndexes(queue, rule, true);

            for (int i = 0; i < leftIndexes.length; i++) {
                if (leftIndexes[i] > rightIndexes[i]) return false;
            }
        }
        return true;
    }

    private static void fix(List<Integer> queue, List<Rule> rules) {
        rules.forEach(rule -> {
            var leftIndexes = findIndexes(queue, rule, false);
            var rightIndexes = findIndexes(queue, rule, true);
            IntStream.range(0, leftIndexes.length)
                    .filter(i->rightIndexes.length > 0 && leftIndexes[i] > rightIndexes[i])
                    .forEach(i->{
                        Collections.swap(queue, leftIndexes[i], rightIndexes[i]);
                        var temp = leftIndexes[i];
                        leftIndexes[i] = rightIndexes[i];
                        rightIndexes[i] = temp;
                    });
        });
    }
    
    private static int[] findIndexes(List<Integer> queue, Rule rule, boolean right){
        return IntStream.range(0, queue.size())
                .filter(i -> (right ? rule.right : rule.left) == queue.get(i))
                .toArray();
    }
}