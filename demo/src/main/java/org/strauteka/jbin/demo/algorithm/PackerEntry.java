package org.strauteka.jbin.demo.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            System.out.println("Starting rat-race for Bin:" + bin);
            Tuple2<Bin, List<Tuple2<Item, Integer>>> tmp = PackerParallel.calculate(iterations, calcSec, bin,
                    itemsUtil);
            // Todo: deepDive in bin!
            long cargoSpace = tmp._1.cargo().stream().map(x -> x.value()).reduce(0l, Long::sum);

            System.out.println("Calculation:: " + (Double.valueOf(cargoSpace) / Double.valueOf(bin.value()))
                    + "% Items:" + (Double.valueOf(ItemSpace) / Double.valueOf(bin.value())) + "%");
            binCollector.add(tmp._1);
            itemsUtil = tmp._2;
        }
        // todo: overview analytic functions!
        // long ItemSpace = items.stream()
        // .map(e -> Long.valueOf(e.l()) * Long.valueOf(e.h()) * Long.valueOf(e.w()) *
        // e.qty)
        // .reduce(0l, Long::sum);

        // result.get(0)._1.stream().map(e -> e.cargo()).flatMap(e -> e.stream())
        // .collect(Collectors.groupingBy(e -> e.cargo(),
        // Collectors.counting())).entrySet().stream()
        // .forEach(System.out::println);

        // long cargoSpace = result.get(0)._1.stream().map(e -> e.cargo().stream()
        // .map(x -> Long.valueOf(x.l()) * Long.valueOf(x.h()) *
        // Long.valueOf(x.w())).reduce(0l, Long::sum))
        // .reduce(0l, Long::sum);

        // System.out.println("Calculation: " + "x" + " :: " +
        // (Double.valueOf(cargoSpace) / Double.valueOf(fullSpace))
        // + "% Items:" + (Double.valueOf(ItemSpace) / Double.valueOf(fullSpace)) +
        // "%");

        return binCollector;
    }

    private static List<Bin> getBins() {
        List<Bin> collector = new ArrayList<>();
        // collector.add(new Bin(new Size(5900, 2380, 2345)));
        // collector.add(new Bin(new Size(5900, 2380, 2345)));
        collector.add(new Bin(new Size(5900, 2380, 2345), new StackConfig(100, 100, 100, 100)));
        collector.add(new Bin(new Size(5900, 2380, 2345), new StackConfig(100, 100, 100, 100)));
        return collector;
    }

    private static List<Item> getStaticItems() {
        List<Item> collector = new ArrayList<>();
        collector.add(new Item(400, 500, 1000, 10));
        collector.add(new Item(567, 532, 538, 10));
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
}
