import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.java8spliterator.PersonSpliterator;
import org.java8spliterator.model.Person;

public class Main {

  public static void main(String[] args) {

    System.out.println("Hello world!");
    Path path = Paths.get("files/people.txt");
    try (Stream<String> lines = Files.lines(path)) {

      Spliterator<String> lineSpliterator = lines.spliterator();
      Spliterator<Person> personSpliterator = null;

      // implementation of person spliterator
      personSpliterator = new PersonSpliterator(lineSpliterator);
      Stream<Person> personStream = StreamSupport.stream(personSpliterator, false);
      personStream.forEach(System.out::println);

    } catch (IOException e) {
      System.out.println(e);
    }
  }
}