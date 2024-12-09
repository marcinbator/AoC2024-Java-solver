package solutions.day9;

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.*;

public class Day9 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        var memoryList = readMemory(inputScanner);
        long checksum1 = countPart1(new ArrayList<>(memoryList));

        var memoryBlockList = getMemoryBlocks(memoryList);
        for (int i = memoryBlockList.size() - 1; i >= 0; i--) {
            var block = memoryBlockList.get(i);
            if (!block.value.equals(".")) {
                i = swapMemory(i, memoryBlockList, block);
            }
        }

        long checksum2 = 0;
        int index2 = 0;
        for (MemBlock block : memoryBlockList) {
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

        if (checksum1 != 6446899523367L || checksum2 != 6478232739671L) System.out.println("wrong");
        return new SolutionResponse(checksum1, checksum2);
    }

    record MemBlock(int startPos, int size, String value) {
    }

    private List<String> readMemory(Scanner inputScanner) {
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
            }
        }

        return new ArrayList<>(Arrays.asList(memory.toString().trim().split(" ")));
    }

    private long countPart1(ArrayList<String> memoryList) {
        int replaceIndex = memoryList.size() - 1;
        for (int i = 0; i < memoryList.size(); i++) {
            if (memoryList.get(i).equals(".")) {
                while (memoryList.get(replaceIndex).equals(".") && replaceIndex > i) {
                    replaceIndex--;
                }
                memoryList.set(i, memoryList.get(replaceIndex));
                memoryList.set(replaceIndex, ".");
            }
        }

        long checkSum = 0;
        for (int i = 0; i < memoryList.size(); i++) {
            if (!memoryList.get(i).equals(".")) {
                long digit = Long.parseLong(memoryList.get(i));
                checkSum += digit * i;
            }
        }
        return checkSum;
    }

    private List<MemBlock> getMemoryBlocks(List<String> memoryList) {
        var memoryBlockList = new ArrayList<MemBlock>();
        int size = 1;
        int startPos = 0;
        String value = memoryList.getFirst();
        for (int i = 1; i < memoryList.size(); i++) {
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

        return memoryBlockList;
    }

    private int swapMemory(int i, List<MemBlock> memoryBlockList, MemBlock block) {
        for (int j = 0; j < i; j++) {
            if (memoryBlockList.get(j).size < block.size || !memoryBlockList.get(j).value.equals(".")) continue;

            var freeBlock = memoryBlockList.get(j);
            var b1 = new MemBlock(freeBlock.startPos, block.size, block.value);
            var b2 = new MemBlock(freeBlock.startPos + block.size, freeBlock.size - block.size, ".");
            memoryBlockList.set(i, new MemBlock(block.startPos, block.size, "."));
            memoryBlockList.set(j, b1);
            memoryBlockList.add(b2);
            i++;

            memoryBlockList.sort(Comparator.comparingInt(bl -> bl.startPos));
            i -= mergeFreeSpaces(memoryBlockList);

            break;
        }
        return i;
    }

    private int mergeFreeSpaces(List<MemBlock> list) {
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