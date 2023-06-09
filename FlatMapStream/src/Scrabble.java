import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
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

    System.out.println("# Words of ShakeSpear " + shakeSpeareWords.size());
    System.out.println("# Words of Scrabble " + scrabbleWords.size());

    final int[] scrabbleENScore = {
        // a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p,  q, r, s, t, u, v, w, x, y, z
        1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10
    };

    Function<String, Integer> score = word -> word.chars()
        .map(letter -> scrabbleENScore[letter - 'a']).sum();
    ToIntFunction<String> intScore = word -> word.chars()
        .map(letter -> scrabbleENScore[letter - 'a']).sum();
    System.out.println("Score of hello: " + intScore.applyAsInt("hello"));

    String bestWord = shakeSpeareWords.stream()
        .filter(word -> scrabbleWords.contains(word))
        .max(Comparator.comparing(score))
        .get();
    System.out.println("Best word: " + bestWord);

    IntSummaryStatistics summaryStatistics = shakeSpeareWords.stream()
        .filter(scrabbleWords::contains)
        .parallel()
        .mapToInt(intScore)
        .summaryStatistics();
    System.out.println(summaryStatistics);

  }

}
