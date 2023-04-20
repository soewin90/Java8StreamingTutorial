import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelStreams {

  public static void main(String[] args) {
    System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "2");

    List<String> strings = new ArrayList<>();

//    Stream.iterate("+", s -> s + "+" )
//        .parallel()
//        .limit(6)
//        .peek(s -> System.out.println(s + " processed in the thread " + Thread.currentThread().getName()))
//        .forEach(System.out::println);
    // wrong pattern
    Stream.iterate("+", s -> s + "+" )
        .parallel()
        .limit(1000)
//        .peek(s -> System.out.println(s + " processed in the thread " + Thread.currentThread().getName()))
        .forEach(s -> strings.add(s));
    System.out.println(strings.size()); // 903 not thread-safe

    // wrong pattern
    List<String> concurrentStrings = new CopyOnWriteArrayList<>();
    Stream.iterate("+", s -> s + "+" )
        .parallel()
        .limit(1000)
//        .peek(s -> System.out.println(s + " processed in the thread " + Thread.currentThread().getName()))
        .forEach(s -> concurrentStrings.add(s));
    System.out.println(concurrentStrings.size()); // 1000, but CopyOnWriteArrayList is poor performance

    // right pattern
   List<String> correctList = Stream.iterate("+", s -> s + "+" )
        .parallel()
        .limit(1000)
//        .peek(s -> System.out.println(s + " processed in the thread " + Thread.currentThread().getName()))
        .collect(Collectors.toList());
    System.out.println(correctList.size());

  }
}