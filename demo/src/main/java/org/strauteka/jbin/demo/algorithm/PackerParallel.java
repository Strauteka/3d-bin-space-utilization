package org.strauteka.jbin.demo.algorithm;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.strauteka.jbin.core.Bin;

public class PackerParallel {

    public static Tuple2<Bin<?>, List<Tuple2<ItemImpl, Integer>>> calculate(int iterations, int calcSec, Bin<?> bin,
            List<Tuple2<ItemImpl, Integer>> items) {
        // stop timer
        final long stop = System.currentTimeMillis() + (calcSec * 1000);

        final List<Tuple2<Bin<?>, List<Tuple2<ItemImpl, Integer>>>> result = IntStream.range(0, iterations).parallel()
                .mapToObj(e -> PackerUnit.pack(bin, items, stop, e)).sorted(sortMaxFirstLowestSecond)
                .collect(Collectors.toList());
        // dbg
        System.out.println(
                "Bin calculations: " + result.stream().map(e -> !e._1.cargo().isEmpty()).filter(e -> e).count());

        return result.get(0);
    }

    private final static Comparator<Tuple2<Bin<?>, List<Tuple2<ItemImpl, Integer>>>> sortMaxFirstLowestSecond = new Comparator<Tuple2<Bin<?>, List<Tuple2<ItemImpl, Integer>>>>() {
        @Override
        public int compare(Tuple2<Bin<?>, List<Tuple2<ItemImpl, Integer>>> o1,
                Tuple2<Bin<?>, List<Tuple2<ItemImpl, Integer>>> o2) {
            final long max = o2._1.cargo().stream().map(x -> x.value()).reduce(0l, Long::sum)
                    - o1._1.cargo().stream().map(x -> x.value()).reduce(0l, Long::sum);
            if (max != 0)
                return max > 0 ? 1 : -1;

            final int h = o1._1.cargo().stream().mapToInt(x -> x.h__()).max().orElse(0)
                    - o2._1.cargo().stream().mapToInt(x -> x.h__()).max().orElse(0);
            return (int) h > 0 ? 1 : (h < 0 ? -1 : 0);
        }
    };
}
