#!/bin/bash

if [ $# -ne 1 ]; then
    echo "Error: no dayNumber given."
    exit 1
fi

dayNumber=$1

if ! [[ "$dayNumber" =~ ^[0-9]+$ ]] || [ "$dayNumber" -lt 1 ] || [ "$dayNumber" -gt 25 ]; then
    echo "Error: give number from 1 to 25."
    exit 1
fi

directory="src/solutions/day${dayNumber}"
if [ -d "$directory" ]; then
    echo "Error: directory $directory already exists."
    exit 1
fi

mkdir "$directory"

file="${directory}/Day${dayNumber}.java"
touch "$file"

sample="${directory}/sample.txt"
touch "$sample"

input="${directory}/input.txt"
touch "$input"

cat <<EOL > "$file"
package solutions.day${dayNumber};

import solutions.Solution;
import solutions.SolutionResponse;

import java.util.Scanner;

public class Day${dayNumber} implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        return new SolutionResponse(0, 0);
    }
}

EOL

git add -A
echo "Created day $dayNumber."
