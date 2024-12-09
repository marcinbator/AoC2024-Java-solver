package solutions.day9;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.*;

public class Day9 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        StringBuilder memory = new StringBuilder();
        long index = 0;

        while (inputScanner.hasNext()) {
            char[] line = inputScanner.next().toCharArray();
            for (int i = 0; i < line.length; i++) {
                long digit = Character.getNumericValue(line[i]);
                if (i % 2 == 0) {
                    for (long j = 0; j < digit; j++) {
                        memory.append(index).append(" ");
                    }
                    index++;
                } else {
                    for (long j = 0; j < digit; j++) {
                        memory.append(".").append(" ");
                    }
                }
                memory.append(" ");
            }
        }

        String memoryString = memory.toString().trim();
        var memoryList = new ArrayList<>(Arrays.stream(memoryString.split(" ")).filter(m -> !m.isEmpty()).toList());
        var memoryListCopy = new ArrayList<>(memoryList);
        int replaceIndex = memoryListCopy.size() - 1;
        for (int i = 0; i < memoryListCopy.size(); i++) {
            if (memoryListCopy.get(i).equals(".")) {
                while (memoryListCopy.get(replaceIndex).equals(".") && replaceIndex > i) {
                    replaceIndex--;
                }
                memoryListCopy.set(i, memoryListCopy.get(replaceIndex));
                memoryListCopy.set(replaceIndex, ".");
            }
        }

        long checkSum = 0;
        for (int i = 0; i < memoryListCopy.size(); i++) {
            if (!memoryListCopy.get(i).equals(".")) {
                long digit = Long.parseLong(memoryListCopy.get(i));
                checkSum += digit * i;
            }
        }

        var memoryBlockList = new ArrayList<MemBlock>();
        int size = 0;
        int startPos = 0;
        String value = memoryList.getFirst();
        for (int i = 0; i < memoryList.size(); i++) {
            if (memoryList.get(i).equals(value)) {
                size++;
            } else {
                memoryBlockList.add(new MemBlock(startPos, size, value));
                startPos = i;
                size = 1;
                value = memoryList.get(i);
            }
        }
        memoryBlockList.add(new MemBlock(startPos, size, value));

        for (int i = memoryBlockList.size() - 1; i >= 0; i--) {
            var block = memoryBlockList.get(i);
            if (block.value.equals(".")) continue;
            for (int j = 0; j < i; j++) {
                if (memoryBlockList.get(j).size >= block.size && memoryBlockList.get(j).value.equals(".")) {
                    var freeBlock = memoryBlockList.get(j);
                    var b1 = new MemBlock(freeBlock.startPos, block.size, block.value);
                    var b2 = new MemBlock(freeBlock.startPos + block.size, freeBlock.size - block.size, ".");
                    memoryBlockList.set(i, new MemBlock(block.startPos, block.size, "."));
                    memoryBlockList.set(j, b1);
//                    if(b2.size > 0){
                    memoryBlockList.add(b2);
                    memoryBlockList.sort(Comparator.comparingInt(bl -> bl.startPos));
                    i++;
//                    }
                    i -= mergeGaps(memoryBlockList);
                    break;
                }
            }
        }

        long checksum2 = 0;
        int index2 = 0;
        for (int i = 0; i < memoryBlockList.size(); i++) {
            var block = memoryBlockList.get(i);
            if (!block.value.equals(".")) {
                for (int j = 0; j < block.size; j++) {
                    checksum2 += Long.parseLong(block.value) * index2;
                    index2++;
                }
            } else {
                for (int j = 0; j < block.size; j++) {
                    index2++;
                }
            }
        }

        return new SolutionResponse(checkSum, checksum2);
    }

    record MemBlock(int startPos, int size, String value) {
    }

    int mergeGaps(List<MemBlock> list) {
        int count = 0;
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).value.equals(".") && list.get(i + 1).value.equals(".")) {
                list.set(i, new MemBlock(list.get(i).startPos, list.get(i).size + list.get(i + 1).size, "."));
                list.remove(i + 1);
                i--;
                count++;
            }
        }
        return count - 1;
    }
}

