package org.strauteka.jbin.demo.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.core.Size;
import org.strauteka.jbin.core.configuration.StackConfig;

public class PackerEntry {
    public static List<Bin> calculate(int iterations, int calcSec) {
        final List<Bin> bins = getBins();
        final boolean isFullSpace = false;
        final boolean isRndItems = true;
        final long customSize = (long) ((isFullSpace ? bins.stream().map(e -> e.value()).reduce(0l, Long::sum)
                : bins.get(0).value()) * 1.1);
        final List<Item> items = isRndItems ? getRandomItems(20, 200, 700, customSize) : getStaticItems();
        // final List<Item> items = getStaticItems();
        List<Tuple2<Item, Integer>> itemsUtil = items.stream().map(e -> new Tuple2<Item, Integer>(e, 0))
                .collect(Collectors.toList());
        List<Bin> binCollector = new ArrayList<>();
        for (Bin bin : bins) {
            final long ItemSpace = itemsUtil.stream().map(e -> e._1.value() * (e._1.qty() - e._2)).reduce(0l,
                    Long::sum);
            System.out.println("Starting rat-race for Bin:" + bin + "\nCalculation Items:"
                    + (Double.valueOf(ItemSpace) / Double.valueOf(bin.value())) + "%");
            Tuple2<Bin, List<Tuple2<Item, Integer>>> tmp = PackerParallel.calculate(iterations, calcSec, bin,
                    itemsUtil);
            binCollector.add(tmp._1);
            itemsUtil = tmp._2;
        }

        for (Bin bin : binCollector) {
            // Todo: deepDive in bin!
            long cargoSpace = bin.cargo().stream().map(x -> x.value()).reduce(0l, Long::sum);
            System.out.println("Items added to bins: " + bin + " || "
                    + (Double.valueOf(cargoSpace) / Double.valueOf(bin.value())) + "%");
            bin.cargo().stream().map(e -> new Tuple2<>((Item) e.cargo(), e.stack().value()))
                    .collect(Collectors.groupingBy(e -> e._1, collector))//
                    .entrySet().stream().map(e -> new Tuple2<Item, Long>(e.getKey(), e.getValue().get(e.getKey())))
                    .forEach(e -> System.out
                            .println(e._1 + " -- " + e._1.qty() + " || " + e._2 + " :: " + (e._1.qty() - e._2)));
        }

        System.out.println("Items Left:");
        itemsUtil.stream().filter(e -> e._1.qty() > e._2).forEach(
                e -> System.out.println(e._1 + " -- " + e._1.qty() + " || " + e._2 + " :: " + (e._1.qty() - e._2)));
        return binCollector;
    }

    private static List<Bin> getBins() {
        List<Bin> collector = new ArrayList<>();
        // collector.add(new Bin(new Size(5900, 2380, 2345)));
        // collector.add(new Bin(new Size(5900, 2380, 2345)));
        collector.add(new Bin(new Size(5900, 2380, 2345), new StackConfig(100, 100, 100, 100, 100, false)));
        collector.add(new Bin(new Size(5900, 2380, 2345), new StackConfig(100, 100, 100, 100, 100, false)));
        return collector;
    }

    private static List<Item> getStaticItems() {
        List<Item> collector = new ArrayList<>();
        collector.add(new Item(400, 500, 1000, 10));
        collector.add(new Item(567, 532, 538, 10));
        collector.add(new Item(359, 265, 129, 50));
        // collector.add(new Item(760, 300, 450, 50));
        // collector.add(new Item(300, 300, 300, 50));
        // collector.add(new Item(400, 500, 1600, 10));
        // collector.add(new Item(567, 532, 538, 10));
        // collector.add(new Item(339, 264, 175, 50));
        // collector.add(new Item(760, 300, 450, 50));
        // collector.add(new Item(360, 380, 350, 50));
        // collector.add(new Item(400, 503, 1020, 10));
        // collector.add(new Item(567, 532, 538, 10));
        // collector.add(new Item(359, 265, 15, 50));
        // collector.add(new Item(760, 304, 430, 50));
        // collector.add(new Item(300, 650, 345, 50));
        return collector;
    }

    private static List<Item> getRandomItems(int itemQty, int f, int t, long maxSpace) {
        final Random rnd = new Random();
        return IntStream.range(0, itemQty)
                .mapToObj(e -> new Size(itemSize(f, t, rnd), itemSize(f, t, rnd), itemSize(f, t, rnd)))
                .map(e -> itemSize(itemQty, e, maxSpace, rnd)).collect(Collectors.toList());
    }

    private static Item itemSize(int itemQty, Size size, long maxSpace, Random rnd) {
        int qty = (int) ((maxSpace / itemQty) / (size.value()));
        return new Item(size, (qty < 1 ? 1 : qty));
    }

    private static int itemSize(int boundMin, int boundMax, Random rnd) {
        return boundMin + rnd.nextInt(boundMax - boundMin);
    }

    // some epic code, need to learn analytic functions :/
    private static final Collector<Tuple2<Item, Long>, Map<Item, Long>, Map<Item, Long>> collector = new Collector<Tuple2<Item, Long>, Map<Item, Long>, Map<Item, Long>>() {
        @Override
        public Supplier<Map<Item, Long>> supplier() {
            return HashMap::new;
        }

        @Override
        public BiConsumer<Map<Item, Long>, Tuple2<Item, Long>> accumulator() {
            return (Map<Item, Long> t, Tuple2<Item, Long> u) -> t.merge(u._1, u._2, (o1, o2) -> o1 + o2);
        }

        @Override
        public BinaryOperator<Map<Item, Long>> combiner() {
            return (Map<Item, Long> t, Map<Item, Long> u) -> Stream.concat(t.entrySet().stream(), u.entrySet().stream())
                    .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (Long o1, Long o2) -> o1 + o2));
        }

        @Override
        public Set<Characteristics> characteristics() {
            return new HashSet<>(Arrays.asList(Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH));
        }

        @Override
        public Function<Map<Item, Long>, Map<Item, Long>> finisher() {
            return Function.identity();
        }
    };
}
