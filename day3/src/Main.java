import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        var dataB = new StringBuilder();
        var scanner = new Scanner(new File("input.txt"));
        while (scanner.hasNextLine()) {
            dataB.append(scanner.nextLine());
        }
        String data = dataB.toString();

        Pattern pattern = Pattern.compile("mul\\([0-9]*,[0-9]*\\)|do\\(\\)|don't\\(\\)");
        var matcher = pattern.matcher(data);
        
        int sum = 0;
        boolean enabled = true;
        
        while(matcher.find()) {
            String found = matcher.group();
            if(found.equals("do()")) enabled = true;
            else if(found.equals("don't()")) enabled = false;
            
            else if(enabled) {
                String[] mul = (matcher.group().substring(4, matcher.group().length() - 1).split(","));
                sum += Integer.parseInt(mul[0]) * Integer.parseInt(mul[1]);
            }
        }
        
        System.out.println(sum);
    }
}