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
    public static List<Bin<?>> calculate(int iterations, int calcSec) {
        final boolean isRndItems = true;
        final List<ItemImpl> items = isRndItems ? getRandomItems(20, 200, 700, new Size(5900, 2380, 2345).value())
                : getStaticItems();
        return calculate(iterations, calcSec, getBins().stream().map(e -> (Bin<?>) e).collect(Collectors.toList()),
                getPallets().stream().map(e -> (Bin<?>) e).collect(Collectors.toList()),
                items.stream().map(e -> Tuple2.of(e, 0)).collect(Collectors.toList()));
    }

    public static List<Bin<?>> calculate(int iterations, int calcSec, List<Bin<?>> bins, List<Bin<?>> pallets,
            List<Tuple2<ItemImpl, Integer>> items) {

        if (!pallets.isEmpty()) {
            Tuple2<List<Bin<?>>, List<Tuple2<ItemImpl, Integer>>> calcPallets = calc(calcSec, iterations, pallets,
                    items);
            items = Stream
                    .concat(calcPallets._1.stream().filter(
                            e -> e.cargo().stream().filter(x -> (x.cargo() instanceof ItemImpl)).findAny().isPresent())
                            .map(e -> Tuple2.<ItemImpl, Integer>of((ItemImpl) e, 0)), calcPallets._2.stream())
                    .collect(Collectors.toList());
        }

        Tuple2<List<Bin<?>>, List<Tuple2<ItemImpl, Integer>>> binCollector = calc(calcSec, iterations, bins, items);

        for (Bin<?> bin : binCollector._1) {
            // Todo: deepDive in bin! get value!
            long cargoSpace = bin.cargo().stream().map(x -> x.value()).reduce(0l, Long::sum);
            System.out.println("Items added to bins: " + bin + " || "
                    + (Double.valueOf(cargoSpace) / Double.valueOf(bin.value())) + "%");
            bin.cargo().stream().filter(e -> e.cargo() instanceof Item)
                    .map(e -> Tuple2.of((Item) e.cargo(), e.stack().value()))
                    .collect(Collectors.groupingBy(e -> e._1, collector))//
                    .entrySet().stream().map(e -> Tuple2.of(e.getKey(), e.getValue().get(e.getKey())))
                    .forEach(e -> System.out
                            .println(e._1 + " -- " + e._1.qty() + " || " + e._2 + " :: " + (e._1.qty() - e._2)));
        }

        System.out.println("Items Left:");
        binCollector._2.stream().filter(e -> e._1.qty() > e._2).forEach(
                e -> System.out.println(e._1 + " -- " + e._1.qty() + " || " + e._2 + " :: " + (e._1.qty() - e._2)));
        return binCollector._1;
    }

    public static Tuple2<List<Bin<?>>, List<Tuple2<ItemImpl, Integer>>> calc(int calcSec, int iterations,
            List<Bin<?>> bins, List<Tuple2<ItemImpl, Integer>> items) {
        List<Bin<?>> binCollector = new ArrayList<>();
        List<Tuple2<ItemImpl, Integer>> itemsNext = items;
        for (Bin<?> bin : bins) {
            Tuple2<Bin<?>, List<Tuple2<ItemImpl, Integer>>> tmp = PackerParallel.calculate(iterations, calcSec, bin,
                    itemsNext);
            binCollector = Stream.concat(binCollector.stream(), Stream.of(tmp._1)).collect(Collectors.toList());
            itemsNext = tmp._2;
        }
        return Tuple2.of(binCollector, itemsNext);
    }

    private static List<Bin<Container>> getBins() {
        List<Bin<Container>> collector = new ArrayList<>();
        collector.add(new Container(5900, 2380, 2345, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        collector.add(new Container(5900, 2380, 2345, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        return collector;
    }

    private static List<Bin<Pallet>> getPallets() {
        List<Bin<Pallet>> c = new ArrayList<>();
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        c.add(Pallet.pallet(new Size(1200, 2200, 800), 150, new StackConfig(100, 100, 100, 100, 5, 100, false)));
        return c;
    }

    private static List<ItemImpl> getStaticItems() {
        List<ItemImpl> collector = new ArrayList<>();
        collector.add(new Item(400, 500, 1000, 1, true, true, true, 3));
        collector.add(new Item(567, 532, 538, 1, true, true, true, 2));
        collector.add(new Item(359, 265, 129, 1, true, false, false, 1));
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

    private static List<ItemImpl> getRandomItems(int itemQty, int f, int t, long maxSpace) {
        final Random rnd = new Random();
        return IntStream.range(0, itemQty)
                .mapToObj(e -> new Size(itemSize(f, t, rnd), itemSize(f, t, rnd), itemSize(f, t, rnd)))
                .map(e -> itemSize(itemQty, e, maxSpace, rnd)).collect(Collectors.toList());
    }

    private static ItemImpl itemSize(int itemQty, Size size, long maxSpace, Random rnd) {
        int qty = Math.round((maxSpace * 1.0f / itemQty) / (size.value()));
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
