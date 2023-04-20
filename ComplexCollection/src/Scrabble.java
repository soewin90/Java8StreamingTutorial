import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class Scrabble {

  public static void main(String args[]) throws IOException {
    Set<String> shakeSpeareWords = Files.lines(Paths.get("file/words.shakespeare.txt"))
        .map(word -> word.toLowerCase())
        .collect(Collectors.toSet());

    Set<String> scrabbleWords = Files.lines(Paths.get("file/ospd.txt"))
        .map(word -> word.toLowerCase())
        .collect(Collectors.toSet());

    System.out.println("# Words of Scrabble " + scrabbleWords.size());
    System.out.println("# Words of ShakeSpear " + shakeSpeareWords.size());

    final int[] scrabbleENScore = {
        // a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p,  q, r, s, t, u, v, w, x, y, z
        1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10
    };

    Function<String, Integer> score = word -> word.chars()
        .map(letter -> scrabbleENScore[letter - 'a']).sum();

    Map<Integer, List<String>> histoWordsByScore = shakeSpeareWords.stream()
        .filter(scrabbleWords::contains)
        .collect(Collectors.groupingBy(score));
    System.out.println("# histoWordsByScore = " + histoWordsByScore.size());

    histoWordsByScore.entrySet() // Set<Map.Entry<Integer, List<String>>>
        .stream()
        .sorted(Comparator.comparing(entry -> -entry.getKey())
        )
        .limit(3)
        .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));

    Function<String, Map<Integer, Long>> histoWork = word -> word.chars().boxed()
        .collect(Collectors.groupingBy(letter -> letter, Collectors.counting()));

    // repeated char count as 1 char eg: whizzing-> whizng score only unique char

    int[] scrableENDistribution = {
        // a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p,  q, r, s, t, u, v, w, x, y, z
        9, 2, 2, 1, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1
    };

    Function<String, Map<Integer, Long>> histoWrod = word ->
        word.chars().boxed()
            .collect(Collectors.groupingBy(letter -> letter, Collectors.counting()));

    Function<String, Long> nBlank = word ->
        histoWrod.apply(word) // Map<Integer, Long> eg: Map<letter, # of letters>
            .entrySet()
            .stream()// Map.Entry<Integer, Long>
            .mapToLong(
                entry ->
                    Long.max(
                        entry.getValue() - (long) scrableENDistribution[entry.getKey() - 'a'],
                        0L
                    )
            ).sum();

    System.out.println("# of blanks for whizzing : " + nBlank.apply("whizzing"));

    Function<String, Integer> score2 =
        word -> histoWrod.apply(word)
            .entrySet()
            .stream() // Map.Entry<Integer, Long>
            .mapToInt(
                entry -> scrabbleENScore[entry.getKey() - 'a'] * Integer.min(
                    entry.getValue().intValue(), scrableENDistribution[entry.getKey() - 'a'])
            ).sum();

    System.out.println("# of score for whizzing : " + score2.apply("whizzing"));
    System.out.println("# of score2 for whizzing : " + score.apply("whizzing"));

    shakeSpeareWords.stream()
        .filter(scrabbleWords::contains)
        .filter(word -> nBlank.apply(word) <= 2)
        .collect(Collectors.groupingBy(score2))
        .entrySet()
        .stream()
        .sorted(Comparator.comparing(entry -> -entry.getKey()))
        .limit(3)
        .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));

  }

}
