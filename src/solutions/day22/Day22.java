package solutions.day22;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.*;

public class Day22 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        long sum = 0L;
        List<List<int[]>> prizes = new ArrayList<>();
        while (inputScanner.hasNextLong()) {
            long num = inputScanner.nextLong();
            List<int[]> buyerPrize = new ArrayList<>();

            for (int i = 0; i < 2000; i++) {
                String prevString = Long.toString(num);
                num = getNextNumber(num);
                String numString = Long.toString(num);
                var newVal = numString.charAt(numString.length() - 1) - '0';
                buyerPrize.add(new int[]{newVal, newVal - (prevString.charAt(prevString.length() - 1) - '0')});
            }
            sum += num;
            prizes.add(buyerPrize);
        }

        List<Map<String, Integer>> sequences = new ArrayList<>();
        Set<String> keys = new HashSet<>();
        for (var elem : prizes) {
            var elemSeq = new HashMap<String, Integer>();
            for (int i = 3; i < elem.size(); i++) {
                String val = elem.get(i - 3)[1] + "," + elem.get(i - 2)[1] + "," + elem.get(i - 1)[1] + "," + elem.get(i)[1];
                if (elemSeq.containsKey(val)) continue;
                elemSeq.put(val, elem.get(i)[0]);
            }
            sequences.add(elemSeq);
            keys.addAll(elemSeq.keySet());
        }

        List<Integer> sums = new ArrayList<>();
        for (var key : keys) {
            int subSum = 0;
            for (var seq : sequences) {
                subSum += seq.get(key) != null ? seq.get(key) : 0;
            }
            sums.add(subSum);
        }

        return new SolutionResponse(sum, sums.stream().max(Integer::compareTo).get());
    }

    private long getNextNumber(long number) {
        number = prune(mix(number * 64, number));
        number = prune(mix(number / 32, number));
        number = prune(mix(number * 2048, number));
        return number;
    }

    private long prune(long number) {
        return number % 16777216;
    }

    private long mix(long number, long secret) {
        return number ^ secret;
    }
}

