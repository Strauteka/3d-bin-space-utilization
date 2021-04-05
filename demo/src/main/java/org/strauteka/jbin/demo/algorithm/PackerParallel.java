package org.strauteka.jbin.demo.algorithm;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.strauteka.jbin.core.Bin;

public class PackerParallel {

    public static Tuple2<Bin, List<Tuple2<Item, Integer>>> calculate(int iterations, int calcSec, Bin bin,
            List<Tuple2<Item, Integer>> items) {
        // scheduled stop
        final AtomicBoolean atomicStop = new AtomicBoolean(Boolean.FALSE);
        final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("Stopping rat-race!");
                atomicStop.set(Boolean.TRUE);
            }
        };

        final ScheduledFuture<?> scheduledFuture = scheduledExecutorService.schedule(r, calcSec, TimeUnit.SECONDS);
        final List<Tuple2<Bin, List<Tuple2<Item, Integer>>>> result = IntStream.range(0, iterations).parallel()
                .mapToObj(e -> PackerUnit.pack(bin, items, atomicStop, e)).sorted(sortMaxFirstLowestSecond)
                .collect(Collectors.toList());
        // dbg
        System.out.println(
                "Bin calculations: " + result.stream().map(e -> !e._1.cargo().isEmpty()).filter(e -> e).count());
        scheduledFuture.cancel(true);
        return result.get(0);
    }

    private final static Comparator<Tuple2<Bin, List<Tuple2<Item, Integer>>>> sortMaxFirstLowestSecond = new Comparator<Tuple2<Bin, List<Tuple2<Item, Integer>>>>() {
        @Override
        public int compare(Tuple2<Bin, List<Tuple2<Item, Integer>>> o1, Tuple2<Bin, List<Tuple2<Item, Integer>>> o2) {
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
