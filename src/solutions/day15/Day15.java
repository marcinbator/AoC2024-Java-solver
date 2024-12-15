package solutions.day15;

import solutions.Solution;
import solutions.SolutionResponse;

import java.lang.invoke.WrongMethodTypeException;
import java.security.Guard;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day15 implements Solution {
    public SolutionResponse solve(Scanner inputScanner) {
        List<List<Character>> tiles = new ArrayList<>();
        List<List<Character>> tiles2 = new ArrayList<>(); 
        Tile robot=null;
        Tile robot2=null;
        String moves;

        int sizeY=0;
        while (inputScanner.hasNextLine()) {
            String line = inputScanner.nextLine();
            if(line.isEmpty()) break;
            List<Character> tileLine = new ArrayList<>();
            List<Character> tileLine2 = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                var value = line.charAt(i);
                tileLine.add(value);
                if(value == '@') robot = new Tile(i,sizeY,value);
                //
                if(value == '#' || value == '.') {
                    tileLine2.add(value);
                    tileLine2.add(value);
                } else if(value == 'O') {
                    tileLine2.add('[');
                    tileLine2.add(']');
                }
                else {
                    tileLine2.add(value);
                    tileLine2.add('.');
                    robot2 = new Tile(tileLine2.size()-2, sizeY,value);
                }
            }
            tiles.add(tileLine);
            tiles2.add(tileLine2);
            sizeY++;
        }
        var movesB = new StringBuilder();
        while(inputScanner.hasNextLine()) {
            movesB.append(inputScanner.nextLine());
        }
        moves = movesB.toString();
        
        assert robot != null;
        
        for(int i=0; i<moves.length(); i++) {
            var move = moves.charAt(i);
            var lookingAt = getLookingAt(move, tiles, robot);
            if(lookingAt.value == '#') continue;
            robot = moveRobot(robot, tiles, lookingAt, move);
        }
        
        int sum=0;
        for(int i=0; i<tiles.size(); i++) {
            for(int j=0; j<tiles.getFirst().size(); j++) {
                if(tiles.get(i).get(j) == 'O') {
                    sum+=100*i+j;
                }
            }
        }
        
        //

        List<List<Character>> tilesCopy = List.of();
        for(int i=0; i<moves.length(); i++) {
            var move = moves.charAt(i);
            var lookingAt = getLookingAt(move, tiles2, robot2);
            if(lookingAt.value == '#') continue;
            try{
                robot2 = moveRobot2(robot2, tiles2, lookingAt, move, new ArrayList<>(), false);
                tilesCopy = new ArrayList<>();
                for (List<Character> innerList : tiles2) {
                    tilesCopy.add(new ArrayList<>(innerList));
                }
            } catch(WrongMethodTypeException e) {
                tiles2 = new ArrayList<>();
                for (List<Character> innerList : tilesCopy) {
                    tiles2.add(new ArrayList<>(innerList));
                }
            }
//            print(tiles2);
        }

        int sum2=0;
        for(int i=0; i<tiles2.size(); i++) {
            for(int j=0; j<tiles2.getFirst().size(); j++) {
                if(tiles2.get(i).get(j) == '[') {
                    sum2+=100*i+j;
                }
            }
        }

        System.out.println(sum);
        System.out.println(sum2);
        return new SolutionResponse(0, 0);
    }
    
    private void print(List<List<Character>> tiles){
        for(List<Character> tile : tiles) {
            for(Character character : tile) {
                System.out.print(character);
            }
            System.out.print('\n');
        }
        System.out.println();
    }

    private static Tile getLookingAt(char move, List<List<Character>> tiles, Tile robot) {
        return switch (move) {
            case '^' -> new Tile(robot.x, robot.y-1, tiles.get(robot.y - 1).get(robot.x));
            case '>' -> new Tile(robot.x+1, robot.y, tiles.get(robot.y).get(robot.x + 1));
            case 'v' -> new Tile(robot.x, robot.y+1, tiles.get(robot.y + 1).get(robot.x));
            case '<' -> new Tile(robot.x-1, robot.y, tiles.get(robot.y).get(robot.x - 1));
            default -> throw new IllegalStateException("Unexpected value: " + move);
        };
    }

    record Tile(int x, int y, char value){}
    record TilePair(Tile obj, Tile dot){}
    
    Tile moveRobot(Tile robot, List<List<Character>> tiles, Tile lookingAt, char move) {
        if(lookingAt.value == '#') return robot;
        if(lookingAt.value == '.') {
            tiles.get(robot.y).set(robot.x, '.');
            robot = new Tile(lookingAt.x,lookingAt.y,robot.value);
            tiles.get(lookingAt.y).set(lookingAt.x, robot.value);
            return robot;
        }
        
        var newLookingAt = getLookingAt(move, tiles, lookingAt);
        var test = moveRobot(lookingAt, tiles, newLookingAt, move);
        if(test.equals(lookingAt)) return robot;
        newLookingAt = getLookingAt(move, tiles, robot);
        return moveRobot(robot, tiles, newLookingAt, move);
    }

    Tile moveRobot2(Tile robot, List<List<Character>> tiles, Tile lookingAt, char move, List<TilePair> swapped, boolean stop) throws WrongMethodTypeException {
        if(stop) return robot;
        if((move=='v'||move=='^')&&(robot.value=='['||robot.value==']')) {
            var robot2 = new Tile(
                    robot.value=='['?robot.x+1: robot.x-1,
                    robot.y,
                    robot.value=='['?']':'['
            );
            var lookingAt2 = getLookingAt(move, tiles, robot2);
            
            if(lookingAt.value == '#' || lookingAt2.value=='#'){
                throw new WrongMethodTypeException("e");
            }
            if(lookingAt.value == '.' && lookingAt2.value=='.') {
                Tile dot1 = new Tile(lookingAt.x, lookingAt.y, robot.value);
                Tile dot2 = new Tile(lookingAt2.x, lookingAt2.y, robot2.value);
                
                swapped.add(new TilePair(
                        robot,
                        dot1
                ));
                swapped.add(new TilePair(
                        robot2,
                        dot2
                ));
                
                tiles.get(robot.y).set(robot.x, '.');
                tiles.get(robot2.y).set(robot2.x, '.');
                robot = dot1;
                robot2 = dot2;
                tiles.get(lookingAt.y).set(lookingAt.x, robot.value);
                tiles.get(lookingAt2.y).set(lookingAt2.x, robot2.value);
                
                return robot;
            }

            var newLookingAt = getLookingAt(move, tiles, lookingAt.value!='.'?lookingAt:lookingAt2);
            var test = moveRobot2(lookingAt.value!='.'?lookingAt:lookingAt2, tiles, newLookingAt, move, swapped, stop);
            if(test.equals(lookingAt.value!='.'?lookingAt:lookingAt2)) return robot;
            newLookingAt = getLookingAt(move, tiles, robot);
            return moveRobot2(robot, tiles, newLookingAt, move, swapped, stop);
        }

        if(lookingAt.value == '#') return robot;
        if(lookingAt.value == '.') {
            tiles.get(robot.y).set(robot.x, '.');
            robot = new Tile(lookingAt.x,lookingAt.y,robot.value);
            tiles.get(lookingAt.y).set(lookingAt.x, robot.value);
            return robot;
        }

        var newLookingAt = getLookingAt(move, tiles, lookingAt);
        var test = moveRobot2(lookingAt, tiles, newLookingAt, move, swapped, stop);
        if(test.equals(lookingAt)) return robot;
        newLookingAt = getLookingAt(move, tiles, robot);
        return moveRobot2(robot, tiles, newLookingAt, move, swapped, stop);
    }
}

