package org.strauteka.jbin.demo.algorithm;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.core.Cargo;
import org.strauteka.jbin.core.Size;
import org.strauteka.jbin.core.Space;
import org.strauteka.jbin.core.Utils.Rotation;

public class PackerUnit {

    public static Tuple2<Bin, List<Tuple2<Item, Integer>>> pack(Bin bin, List<Tuple2<Item, Integer>> items,
            AtomicBoolean atomicStop, int id) {
        // System.out.println(
        // "Current Thread Name- " + Thread.currentThread().getName() + " :: " +
        // Thread.currentThread().getId());
        if (atomicStop.get()) {
            return new Tuple2<Bin, List<Tuple2<Item, Integer>>>(bin, items);
        } else {
            return packRecursive(new Bin(bin), items);
        }
    }

    private static Tuple2<Bin, List<Tuple2<Item, Integer>>> packRecursive(Bin bin,
            List<Tuple2<Item, Integer>> itemUtilize) {
        final Optional<Cargo<? extends Size>> optCargo = findCargo(bin.emptySpace(),
                itemUtilize.stream().filter(e -> e._1.qty() > e._2).collect(Collectors.toList()));
        if (optCargo.isPresent()) {
            final Cargo<? extends Size> cargo = optCargo.get();
            bin.add(cargo);
            final Item cargoItem = (Item) cargo.cargo();
            final List<Tuple2<Item, Integer>> next = Stream
                    .concat(itemUtilize.stream().filter(e -> !e._1.equals(cargoItem)), //
                            itemUtilize.stream().filter(e -> e._1.equals(cargoItem))
                                    .map(e -> new Tuple2<>(e._1, e._2 + (int) cargo.stack().value())))
                    .collect(Collectors.toList());
            return packRecursive(bin, next);
        } else {
            return new Tuple2<Bin, List<Tuple2<Item, Integer>>>(bin, itemUtilize);
        }
    }

    private static Optional<Cargo<? extends Size>> findCargo(List<Space> spaces,
            List<Tuple2<Item, Integer>> itemUtilize) {
        // looping through all spaces is time consuming...
        final Optional<Space> space = selectSpace(spaces, itemUtilize);
        if (space.isPresent()) {
            return createCargo(space.get(), itemUtilize);
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Space> selectSpace(List<Space> spaces, List<Tuple2<Item, Integer>> itemUtilize) {
        // filter off spaces that can't fit any item
        return selectSpace(
                spaces.stream().filter(e -> itemUtilize.stream().filter(x -> e.fitAny(x._1)).findAny().isPresent())
                        .collect(Collectors.toList()));
    }

    private static Optional<Space> selectSpace(List<Space> validSpaces) {
        if (validSpaces.size() > 0) {
            if (random(0, 2) == 0) {
                return Optional.of(validSpaces.get(random(0, validSpaces.size() - 1)));
            } else {
                return validSpaces.stream().sorted(Comparator.comparingInt(e -> e.h_())).findFirst();
            }
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Cargo<? extends Size>> createCargo(Space space, List<Tuple2<Item, Integer>> itemUtilize) {
        final List<Cargo<? extends Size>> cargo = itemUtilize.stream().map(e -> createCargo(space, e._1, e._2))
                .flatMap(e -> e.stream()).collect(Collectors.toList());
        if (random(0, 2) == 0) {
            return Optional.of(cargo.get(random(0, cargo.size() - 1)));
        } else {
            return cargo.stream().sorted(Comparator.comparingInt(e -> (space.l() * space.w()) - (e.l() * e.w())))
                    .findFirst();
        }
    }

    private static List<Cargo<? extends Size>> createCargo(Space space, Item item, Integer qty) {
        return Stream.of(Rotation.values()).filter(e -> space.fit(item.rotate(e)))
                .map(e -> buildCargoSwitch(space, item, qty, e)).collect(Collectors.toList());
    }

    private static Cargo<? extends Size> buildCargoSwitch(Space space, Item item, Integer qtyUsed, Rotation rotation) {
        int rndSwitch = random(0, 4);
        if (rndSwitch == 0) {
            return buildCargoA(space, item, (item.qty() - qtyUsed), rotation);
        } else if (rndSwitch == 1) {
            return buildCargoB(space, item, (item.qty() - qtyUsed), rotation);
        } else {
            return buildCargoC(space, item, (item.qty() - qtyUsed), rotation);
        }
    }

    private static Cargo<? extends Size> buildCargoC(Space space, Item item, Integer qtyLeft, Rotation rotation) {
        final Size maxQtyOnDimension = maxQtyOnDimension(space, item.rotate(rotation));
        final int l = randomReduce(Math.min(maxQtyOnDimension.l(), qtyLeft));
        final int h = randomReduce(Math.min(grow(qtyLeft, l), maxQtyOnDimension.h()));
        final int w = Math.min(grow(qtyLeft, l * h), maxQtyOnDimension.w());
        return new Cargo<Size>(item, l, h, w, rotation, space.position());
    }

    private static Cargo<? extends Size> buildCargoB(Space space, Item item, Integer qtyLeft, Rotation rotation) {
        final Size maxQtyOnDimension = maxQtyOnDimension(space, item.rotate(rotation));
        final int w = randomReduce(Math.min(maxQtyOnDimension.w(), qtyLeft));
        final int l = randomReduce(Math.min(grow(qtyLeft, w), maxQtyOnDimension.l()));
        final int h = 1;
        return new Cargo<Size>(item, l, h, w, rotation, space.position());
    }

    private static Cargo<? extends Size> buildCargoA(Space space, Item item, Integer qtyLeft, Rotation rotation) {
        final Size maxQtyOnDimension = maxQtyOnDimension(space, item.rotate(rotation));
        final int l = randomReduce(Math.min(maxQtyOnDimension.l(), qtyLeft));
        final int w = randomReduce(Math.min(grow(qtyLeft, l), maxQtyOnDimension.w()));
        final int h = 1;
        return new Cargo<Size>(item, l, h, w, rotation, space.position());
    }

    private static Size maxQtyOnDimension(Space space, Size size) {
        return new Size(space.l() / size.l(), space.h() / size.h(), space.w() / size.w());
    }

    private static int grow(int max, int step) {
        return max / step;
    }

    private static int random(int origin, int boundInclusive) {
        return ThreadLocalRandom.current().nextInt(origin, (boundInclusive + 1));
    }

    private static int randomReduce(int input) {
        if (input > 1 && random(0, 2) == 0) {
            return (input - 1);
        } else {
            return input;
        }
    }
}
