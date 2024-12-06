import solutions.Solution;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner consoleScanner = new Scanner(System.in);

        while (true) {
            System.out.println("Give day number (e.g. '1'), 0 to close:");
            int day = consoleScanner.nextInt();
            if (day == 0) break;

            String solverClassName = "Day" + day;
            String solverDirectory = "solutions.day" + day;
            String solverPath = solverDirectory + "." + solverClassName;
            
            try {
                solve(solverPath, consoleScanner, solverDirectory, day);
            } catch (ClassNotFoundException e) {
                System.out.println("Day " + solverClassName + " does not exist.");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input!");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                System.out.println("\n-----------------\n");
            }
        }

        consoleScanner.close();
    }

    private static char getInputType(Scanner scanner) throws IllegalArgumentException {
        System.out.println("Would you like to solve sample (S) or input data? (I):");
        char type = scanner.next().toLowerCase().charAt(0);
        if (type != 's' && type != 'i') {
            throw new IllegalArgumentException();
        }
        return type;
    }

    private static void solve(String solverPath, Scanner consoleScanner, String solverDirectory, int day)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException, FileNotFoundException {
        Class<?> solverClass = Class.forName(solverPath);
        Solution instance = (Solution) solverClass.getDeclaredConstructor().newInstance();

        char type = getInputType(consoleScanner);
        String inputPath = "src/"+solverDirectory.replace('.','/') + "/"+ (type == 's' ? "sample.txt" : "input.txt");
        var solution = instance.solve(new Scanner(new File(inputPath)));

        System.out.println(
                "Day " + day + " " + (type == 's' ? "<sample> " : "<input> ") + "data solution:\npart 1: "
                        + solution.part2() +", part 2: "+solution.part2()
        );
    }
}
