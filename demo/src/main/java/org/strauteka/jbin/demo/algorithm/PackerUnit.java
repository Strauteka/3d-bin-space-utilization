package org.strauteka.jbin.demo.algorithm;

import org.strauteka.jbin.core.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PackerUnit {

    public static Tuple2<Bin<?>, List<Tuple2<Item, Integer>>> pack(Bin<?> bin,
                                                                   List<Tuple2<Item, Integer>> items, long stop, int id) {
        if (System.currentTimeMillis() >= stop)
            return Tuple2.of(bin, items);

        return packRecursive(bin, items,
                items.stream().map(e -> e._1.priority()).distinct().sorted().collect(Collectors.toList()), id);
    }

    private static Tuple2<Bin<?>, List<Tuple2<Item, Integer>>> packRecursive(Bin<?> bin,
                                                                             List<Tuple2<Item, Integer>> itemUtilize,
                                                                             List<Integer> priority,
                                                                             int id) {
        final int prior = priority.get(0);
        final Optional<Cargo<? extends Dimension>> optCargo =
                findCargo(bin.emptySpace(),
                        itemUtilize.stream()
                                .filter(e -> e._1.priority() == prior)
                                .filter(e -> e._1.qty() > e._2)
                                .collect(Collectors.toList()),
                        id);

        if (!optCargo.isPresent()) {
            if (priority.size() > 1) {
                return packRecursive(bin, itemUtilize, priority.stream().skip(1).collect(Collectors.toList()), id);
            }
            return Tuple2.of(bin, itemUtilize);
        }

        final Cargo<? extends Dimension> cargo = optCargo.get();
        final Item cargoItem = (Item) cargo.cargo();
        final Bin<?> nextBin = bin.add(cargo, cargoItem instanceof Pallet, bin.stackConfig());
        final List<Tuple2<Item, Integer>> next = Stream
                .concat(itemUtilize.stream().filter(e -> !e._1.equals(cargoItem)), //
                        itemUtilize.stream().filter(e -> e._1.equals(cargoItem))
                                .map(e -> Tuple2.of(e._1, e._2 + Long.valueOf(cargo.stack().value()).intValue())))
                .collect(Collectors.toList());
        return packRecursive(nextBin, next, priority, id);
    }

    private static Optional<Cargo<? extends Dimension>> findCargo(List<Space> spaces,
                                                                  List<Tuple2<Item, Integer>> itemUtilize, int id) {
        // looping through all spaces is time-consuming...
        return selectSpace(spaces, itemUtilize, id)
                .flatMap(value -> createCargo(value, itemUtilize));

    }

    private static Optional<Space> selectSpace(List<Space> spaces, List<Tuple2<Item, Integer>> itemUtilize, int id) {
        final List<Dimension> uniqueRotatedDimensions = itemUtilize.stream()
                .flatMap(tuple -> Stream.of(tuple._1.rotations())
                        .map(tuple._1::rotate))
                .distinct()
                .collect(Collectors.toList());

        // filter off spaces that can't fit any item
        return spaces.stream()
                        .filter(e -> uniqueRotatedDimensions.stream().anyMatch(e::fit))
                        .min(Comparator
                                .comparingLong(e -> (id % 2 == 0 ? 1L : -1L) * Utils.concat(e.h_(), e.l_(), e.w_())));

    }

    private static Optional<Cargo<? extends Dimension>> createCargo(Space space,
                                                                    List<Tuple2<Item, Integer>> itemUtilize) {
        return itemUtilize.stream()
                .map(e -> createCargo(space, e._1, e._2))
                .flatMap(Collection::stream)
                .min(Comparator.comparingLong(e -> {
                    final Size s = space.subtract(e);
                    return Utils.concat(s.h(), s.w(), s.l(), 50);
                }));
    }

    private static List<Cargo<? extends Dimension>> createCargo(Space space, Item item, Integer qty) {
        return Stream.of(item.rotations()).filter(e -> space.fit(item.rotate(e)))
                .map(e -> buildCargoSwitch(space, item, qty, e)).flatMap(Collection::stream).collect(Collectors.toList());
    }

    private static List<Cargo<? extends Dimension>> buildCargoSwitch(Space space, Item item, Integer qtyUsed,
                                                                     Rotation rotation) {
        return Stream.of(buildCargoA(space, item, (item.qty() - qtyUsed), rotation),
                buildCargoB(space, item, (item.qty() - qtyUsed), rotation),
                buildCargoC(space, item, (item.qty() - qtyUsed), rotation)).collect(Collectors.toList());
    }

    private static Cargo<? extends Dimension> buildCargoC(Space space, Item item, Integer qtyLeft,
                                                          Rotation rotation) {
        final Size maxQtyOnDimension = Utils.maxQtyOnDimension(space, item.rotate(rotation));
        final int l = Utils.randomReduce(Math.min(maxQtyOnDimension.l(), qtyLeft));
        final int h = Utils.randomReduce(Math.min(Utils.grow(qtyLeft, l), maxQtyOnDimension.h()));
        final int w = Math.min(Utils.grow(qtyLeft, l * h), maxQtyOnDimension.w());
        return new Cargo<>(item, l, h, w, rotation, space.position());
    }

    private static Cargo<? extends Dimension> buildCargoB(Space space, Item item, Integer qtyLeft,
                                                          Rotation rotation) {
        final Size maxQtyOnDimension = Utils.maxQtyOnDimension(space, item.rotate(rotation));
        final int w = Utils.randomReduce(Math.min(maxQtyOnDimension.w(), qtyLeft));
        final int l = Utils.randomReduce(Math.min(Utils.grow(qtyLeft, w), maxQtyOnDimension.l()));
        final int h = 1;
        return new Cargo<>(item, l, h, w, rotation, space.position());
    }

    private static Cargo<? extends Dimension> buildCargoA(Space space, Item item, Integer qtyLeft,
                                                          Rotation rotation) {
        final Size maxQtyOnDimension = Utils.maxQtyOnDimension(space, item.rotate(rotation));
        final int l = Utils.randomReduce(Math.min(maxQtyOnDimension.l(), qtyLeft));
        final int w = Utils.randomReduce(Math.min(Utils.grow(qtyLeft, l), maxQtyOnDimension.w()));
        final int h = 1;
        return new Cargo<>(item, l, h, w, rotation, space.position());
    }
}
