package org.strauteka.jbin.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.strauteka.jbin.core.Utils.Rotation;
import org.strauteka.jbin.core.configuration.StackConfig;

public class Bin extends Size {
    private final StackConfig stackConfig;
    private final List<Cargo<? extends Dimension>> cargo;
    private final List<Space> space;

    public Bin(Bin bin) {
        this(bin, bin.stackConfig());
    }

    public Bin(Bin bin, StackConfig overStack) {
        this(bin, Optional.ofNullable(overStack).orElseGet(() -> bin.stackConfig()), bin.emptySpace(), bin.cargo());
    }

    public Bin(int l, int h, int w) {
        this(new Size(l, h, w));
    }

    public Bin(int l, int h, int w, StackConfig overStack) {
        this(new Size(l, h, w), overStack);
    }

    public Bin(Dimension size) {
        this(size, new StackConfig(0, 0, 0, 0, 0, false));
    }

    public Bin(Dimension size, StackConfig overStack) {
        this(size, overStack, null, null);
    }

    public Bin(Dimension size, StackConfig overStack, List<Space> space, List<Cargo<? extends Dimension>> cargo) {
        super(size);
        this.stackConfig = overStack;
        this.cargo = Collections.unmodifiableList(Optional.ofNullable(cargo)
                .orElseGet(() -> new ArrayList<Cargo<? extends Dimension>>()).stream().collect(Collectors.toList()));
        this.space = Collections.unmodifiableList(Optional.ofNullable(space)
                .orElseGet(() -> new ArrayList<Space>(Arrays.asList(new Space(this, new Size(0, 0, 0))))).stream()
                .collect(Collectors.toList())); // collect toUnmodifiableList error java 1.8 todo: fix?
    }

    public Bin add(Cargo<? extends Dimension> cargo) {
        return add(cargo, false);
    }

    public Bin add(Cargo<? extends Dimension> cargo, boolean disableTop) {
        return new Bin(this, this.stackConfig,
                dropUnusableSpace(mergeAll(dropOverlapSpace(createSpace(space, cargo, disableTop)))),
                Stream.concat(this.cargo.stream(), Stream.of(cargo)).collect(Collectors.toList()));
    }

    private List<Space> createSpace(List<Space> space, Cargo<? extends Dimension> cargo, boolean disableTop) {
        return Stream
                .concat(space.stream().filter(e -> e.overlap(cargo)).map(e -> e.createSpace(cargo, disableTop))
                        .flatMap(e -> e.stream()).distinct(), space.stream().filter(e -> !e.overlap(cargo)))
                .collect(Collectors.toList());
    }

    private List<Space> dropOverlapSpace(final List<Space> space) {
        return space.stream()
                .filter(e -> !space.stream().filter(x -> !e.equals(x) && x.overlay(e)).findAny().isPresent())
                .collect(Collectors.toList());
    }

    private List<Space> dropUnusableSpace(final List<Space> space) {
        return space.stream().filter(e -> Math.min(Math.min(e.l(), e.h()), e.w()) > stackConfig.minimumSpaceSide())
                .collect(Collectors.toList());
    }

    public List<Space> emptySpace() {
        return space;
    }

    public List<Cargo<? extends Dimension>> cargo() {
        return cargo;
    }

    public StackConfig stackConfig() {
        return stackConfig;
    }

    private List<Space> mergeAll(List<Space> space) {
        if (stackConfig.disableMerge())
            return space;
        final List<Space> mergedItems = merge(space);
        if (!mergedItems.isEmpty())
            return mergeAll(Stream
                    .concat(mergedItems.stream(),
                            space.stream()
                                    .filter(e -> !mergedItems.stream().filter(x -> x.overlay(e)).findAny().isPresent()))
                    .collect(Collectors.toList()));

        return space;
    }

    private List<Space> merge(List<Space> space) {
        return space.stream().map(e -> space.stream()//
                .filter(x -> !e.equals(x))//
                .filter(x -> e.needToCombineSpace(x, stackConfig))//
                .map(x -> e.combineSpace(x, stackConfig)).filter(Objects::nonNull)).flatMap(e -> e).distinct()
                .filter(e -> !space.stream().filter(x -> x.overlay(e)).findAny().isPresent())
                .collect(Collectors.toList());
    }

    public Bin binRotate(Rotation rotation) {
        return this.cargo().stream().reduce(new Bin(rotate(rotation), this.stackConfig),
                (bin, cargo) -> bin.add(Utils.cargoRotate((Size) bin, rotation, cargo)), (bin1, bin2) -> {
                    throw new UnsupportedOperationException();
                });
    }
}
