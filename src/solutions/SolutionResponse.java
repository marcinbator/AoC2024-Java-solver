package solutions;

public record SolutionResponse(String part1, String part2) {
    public SolutionResponse(long part1, long part2) {
        this(String.valueOf(part1), String.valueOf(part2));
    }
}
