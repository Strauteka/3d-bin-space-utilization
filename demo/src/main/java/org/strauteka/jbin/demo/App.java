package org.strauteka.jbin.demo;

import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.core.Dimension;
import org.strauteka.jbin.core.Size;
import org.strauteka.jbin.core.configuration.StackConfig;
import org.strauteka.jbin.demo.algorithm.Container;
import org.strauteka.jbin.demo.algorithm.*;
import org.strauteka.jbin.draw3d.Draw3d;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) {
        final long l = System.currentTimeMillis();
        final Bin<?>[] result = PackerEntry.calculate(10000, 3,
                getBins(),
                getPallets(),
                getItems()).toArray(new Bin<?>[0]);

        System.out.println("Calc time: " + (System.currentTimeMillis() - l));

        final Map<Dimension, Color> palletColor = Stream
                .of(Tuple2.of(new Size(1200, 150, 800), new Color(140, 70, 20)))
                .collect(Collectors.toMap(tuple -> tuple._1, tuple -> tuple._2));

        Draw3d.draw(false, false, 1200, 800, palletColor, result);
    }

    private static List<Item> getRandomItems(int itemQty, int f, int t, long maxSpace) {
        final Random rnd = new Random();
        return IntStream.range(0, itemQty)
                .mapToObj(e -> new Size(itemSize(f, t, rnd), itemSize(f, t, rnd), itemSize(f, t, rnd)))
                .map(e -> itemSize(itemQty, e, maxSpace)).collect(Collectors.toList());
    }

    private static Item itemSize(int itemQty, Size size, long maxSpace) {
        int qty = Math.round((maxSpace * 1.0f / itemQty) / (size.value()));
        return new ItemImpl(size, (Math.max(qty, 1)));
    }

    private static int itemSize(int boundMin, int boundMax, Random rnd) {
        return boundMin + rnd.nextInt(boundMax - boundMin);
    }

    private static List<Item> getItems() {
        boolean isRndItems = true;
        return isRndItems ? getRandomItems(20, 200, 700,
                getBins().stream()
                        .reduce(0L, (base, v) -> base + v.value(), Long::sum))
                : getStaticItems();
    }

    private static List<Item> getStaticItems() {
        return Stream.of(
                new ItemImpl(400, 500, 1000, 1, true, true, true, 3),
                new ItemImpl(567, 532, 538, 1, true, true, true, 2),
                new ItemImpl(359, 265, 129, 1, true, false, false, 1),
                new ItemImpl(760, 300, 450, 50),
                new ItemImpl(300, 300, 300, 50),
                new ItemImpl(400, 500, 1600, 10),
                new ItemImpl(567, 532, 538, 10),
                new ItemImpl(339, 264, 175, 50),
                new ItemImpl(760, 300, 450, 50),
                new ItemImpl(360, 380, 350, 50),
                new ItemImpl(400, 503, 1020, 10),
                new ItemImpl(567, 532, 538, 10),
                new ItemImpl(359, 265, 15, 50),
                new ItemImpl(760, 304, 430, 50),
                new ItemImpl(300, 650, 345, 50)
        ).collect(Collectors.toList());
    }

    private static List<Bin<Container>> getBins() {
        final Container container = new Container(5900,
                2380,
                2345,
                new StackConfig(100, 100, 100, 100, 5, 100, false));
        return IntStream
                .range(0, 2)
                .boxed()
                .map(notUsed -> container)
                .collect(Collectors.toList());
    }

    private static List<Bin<Pallet>> getPallets() {
        final StackConfig stackConfig = new StackConfig(100, 100, 100, 100, 5, 100, false);
        final Size size = new Size(1200, 2200, 800);
        return IntStream
                .range(0, 11)
                .boxed()
                .map(notUsed -> Pallet.pallet(size, 150, stackConfig))
                .collect(Collectors.toList());
    }
}
