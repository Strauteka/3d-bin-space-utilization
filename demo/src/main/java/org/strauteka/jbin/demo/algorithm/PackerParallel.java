package org.strauteka.jbin.demo.algorithm;

import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.core.Size;
import org.strauteka.jbin.core.Space;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PackerParallel {

    public static Tuple2<Bin<?>, List<Tuple2<Item, Integer>>> calculate(int iterations, int calcSec, Bin<?> bin,
                                                                        List<Tuple2<Item, Integer>> items) {
        // stop timer
        final long stop = System.currentTimeMillis() + (calcSec * 1000L);

        final List<Tuple2<Bin<?>, List<Tuple2<Item, Integer>>>> result = IntStream.range(0, iterations).parallel()
                .mapToObj(e -> PackerUnit.pack(bin, items, stop, e)).sorted(sortMaxFirstLowestSecond)
                .collect(Collectors.toList());
        // dbg
        System.out.println(
                "Bin calculations: " + result.stream().map(e -> !e._1.cargo().isEmpty()).filter(e -> e).count());

        return result.get(0);
    }

    private final static Comparator<Tuple2<Bin<?>, List<Tuple2<Item, Integer>>>> sortMaxFirstLowestSecond =
            (o1, o2) -> {
                final long max = o2._1.cargo().stream().map(Size::value).reduce(0L, Long::sum)
                        - o1._1.cargo().stream().map(Size::value).reduce(0L, Long::sum);
                if (max != 0)
                    return max > 0 ? 1 : -1;

                final int h = o1._1.cargo().stream().mapToInt(Space::h__).max().orElse(0)
                        - o2._1.cargo().stream().mapToInt(Space::h__).max().orElse(0);
                return Integer.compare(h, 0);
            };
}
