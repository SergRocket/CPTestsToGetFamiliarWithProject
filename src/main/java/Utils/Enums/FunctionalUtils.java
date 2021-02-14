package Utils.Enums;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FunctionalUtils {
    public static <T, K> Map<K, List<T>> group(Collection<T> list, Function<? super T, ? extends K> group) {
        return list.stream().collect(Collectors.groupingBy(group, Collectors.toList()));
    }

    public static <T, K> Map<K, Set<T>> group(Set<T> set, Function<? super T, ? extends K> group) {
        return set.stream().collect(Collectors.groupingBy(group, Collectors.toSet()));
    }

    public static <T> Optional<T> filterOneOptional(Stream<T> stream, Predicate<? super T> predicate) {
        return stream.filter(predicate).findFirst();
    }

    public static <T> Optional<T> filterOneOptional(Collection<T> stream, Predicate<? super T> predicate) {
        return filterOneOptional(stream.stream(), predicate);
    }

    public static <T> T filterOne(Stream<T> stream, Predicate<? super T> predicate) {
        return filterOneOptional(stream, predicate).orElse(null);
    }

    /**
     * @return null if not found
     */
    public static <T> T filterOne(Collection<T> collection, Predicate<? super T> predicate) {
        return filterOne(collection.stream(), predicate);
    }

    public static <T> T filterOneRequired(Stream<T> stream, Predicate<? super T> predicate, String message,
                                          Object... args) {
        return UtilsTest.require(filterOne(stream, predicate), Objects::nonNull, UtilsTest.formatMessage(message, args));
    }

    public static <T> T filterOneRequired(Collection<T> collection, Predicate<? super T> predicate, String message,
                                          Object... args) {
        return filterOneRequired(collection.stream(), predicate, message, args);
    }
}
