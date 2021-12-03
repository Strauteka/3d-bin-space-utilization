package org.strauteka.jbin.demo.algorithm;

import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.core.Size;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.out;

public class PackerEntry {

    public static List<Bin<?>> calculate(int iterationsOnOneBin,
                                         int calcSecOnOneBin,
                                         List<Bin<Container>> bins,
                                         List<Bin<Pallet>> pallets,
                                         List<Item> items) {
        return localCalculate(iterationsOnOneBin, calcSecOnOneBin,
                new ArrayList<>(bins),
                new ArrayList<>(pallets),
                items.stream().map(e -> Tuple2.of(e, 0)).collect(Collectors.toList()));
    }

    private static List<Bin<?>> localCalculate(int iterationsOnOneBin,
                                               int calcSecOnOneBin,
                                               List<Bin<?>> bins,
                                               List<Bin<?>> pallets,
                                               List<Tuple2<Item, Integer>> items) {

        if (!pallets.isEmpty()) {
            Tuple2<List<Bin<?>>, List<Tuple2<Item, Integer>>> calcPallets = calc(calcSecOnOneBin,
                    iterationsOnOneBin,
                    pallets,
                    items);
            items = Stream
                    .concat(calcPallets._1.stream()
                                    .filter(e -> e.cargo().stream().anyMatch(x -> (x.cargo() instanceof Item)))
                                    .map(e -> Tuple2.of((Item) e, 0)),
                            calcPallets._2.stream())
                    .collect(Collectors.toList());
        }

        Tuple2<List<Bin<?>>, List<Tuple2<Item, Integer>>> binCollector = calc(calcSecOnOneBin,
                iterationsOnOneBin,
                bins,
                items);

        for (Bin<?> bin : binCollector._1) {
            // Todo: deepDive in bin! get value!
            long cargoSpace = bin.cargo().stream().map(Size::value).reduce(0L, Long::sum);
            out.println("Items added to bins: " + bin + " || "
                    + ((double) cargoSpace / (double) bin.value()) + "%");
            bin.cargo().stream().filter(e -> e.cargo() instanceof ItemImpl)
                    .map(e -> Tuple2.of((ItemImpl) e.cargo(), e.stack().value()))
                    .collect(Collectors.groupingBy(e -> e._1, collector))//
                    .entrySet().stream()
                    .map(e -> Tuple2.of(e.getKey(), e.getValue().get(e.getKey())))
                    .forEach(e -> out
                            .println(e._1 + " -- " + e._1.qty() + " || " + e._2 + " :: " + (e._1.qty() - e._2)));
        }

        out.println("Items Left:");
        binCollector._2.stream()
                .filter(e -> e._1.qty() > e._2)
                .forEach(
                        e -> out.println(e._1 + " -- " + e._1.qty() + " || " + e._2 + " :: " + (e._1.qty() - e._2)));
        return binCollector._1;
    }

    public static Tuple2<List<Bin<?>>, List<Tuple2<Item, Integer>>> calc(int calcSec, int iterations,
                                                                         List<Bin<?>> bins, List<Tuple2<Item, Integer>> items) {
        List<Bin<?>> binCollector = new ArrayList<>();
        List<Tuple2<Item, Integer>> itemsNext = items;
        for (Bin<?> bin : bins) {
            Tuple2<Bin<?>, List<Tuple2<Item, Integer>>> tmp = PackerParallel.calculate(iterations, calcSec, bin,
                    itemsNext);
            binCollector = Stream.concat(binCollector.stream(), Stream.of(tmp._1)).collect(Collectors.toList());
            itemsNext = tmp._2;
        }
        return Tuple2.of(binCollector, itemsNext);
    }

    // some epic code, need to learn analytic functions :/
    private static final Collector<Tuple2<ItemImpl, Long>, Map<ItemImpl, Long>, Map<ItemImpl, Long>> collector
            = new Collector<Tuple2<ItemImpl, Long>, Map<ItemImpl, Long>, Map<ItemImpl, Long>>() {
        @Override
        public Supplier<Map<ItemImpl, Long>> supplier() {
            return HashMap::new;
        }

        @Override
        public BiConsumer<Map<ItemImpl, Long>, Tuple2<ItemImpl, Long>> accumulator() {
            return (Map<ItemImpl, Long> t, Tuple2<ItemImpl, Long> u) -> t.merge(u._1, u._2, Long::sum);
        }

        @Override
        public BinaryOperator<Map<ItemImpl, Long>> combiner() {
            return (Map<ItemImpl, Long> t, Map<ItemImpl, Long> u) -> Stream.concat(t.entrySet().stream(),
                            u.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
        }

        @Override
        public Set<Characteristics> characteristics() {
            return new HashSet<>(Arrays.asList(Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH));
        }

        @Override
        public Function<Map<ItemImpl, Long>, Map<ItemImpl, Long>> finisher() {
            return Function.identity();
        }
    };
}
