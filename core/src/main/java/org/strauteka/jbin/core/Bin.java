package org.strauteka.jbin.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.strauteka.jbin.core.Utils.Rotation;
import org.strauteka.jbin.core.configuration.StackConfig;

public class Bin extends Size {
    private final StackConfig stackConfig;
    private final List<Cargo<? extends Size>> cargo = new ArrayList<Cargo<?>>();
    List<Space> space = new ArrayList<Space>();

    public Bin(Bin bin) {
        this(bin, bin.stackConfig());
        bin.cargo.stream().sequential().forEach(e -> this.add(e));
    }

    public Bin(Dimension size) {
        this(size, new StackConfig(0, 0, 0, 0, 0));
    }

    public Bin(Dimension size, StackConfig overStack) {
        super(size);
        this.stackConfig = overStack;
        space.add(new Space(this, new Size(0, 0, 0)));
    }

    public void add(Cargo<? extends Size> cargo) {
        add(cargo, false);
    }

    public void add(Cargo<? extends Size> cargo, boolean disableTop) {
        this.cargo.add(cargo);
        this.space = dropUnusableSpace(mergeAll(dropOverlapSpace(createSpace(space, cargo, disableTop))));
    }

    private List<Space> createSpace(List<Space> space, Cargo<? extends Size> cargo, boolean disableTop) {
        return Stream
                .concat(space.stream().filter(e -> e.overlap(cargo)).map(e -> e.createSpace(cargo, disableTop))
                        .flatMap(e -> e.stream()), space.stream().filter(e -> !e.overlap(cargo)))
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
        return Collections.unmodifiableList(space);
    }

    public List<Cargo<? extends Size>> cargo() {
        return Collections.unmodifiableList(cargo);
    }

    public StackConfig stackConfig() {
        return stackConfig;
    }

    private List<Space> mergeAll(List<Space> space) {
        final List<Space> mergedItems = merge(space);
        if (!mergedItems.isEmpty()) {
            return mergeAll(Stream
                    .concat(mergedItems.stream(), space.stream().filter(
                            e -> !mergedItems.stream().filter(x -> !e.equals(x) && x.overlay(e)).findAny().isPresent()))
                    .collect(Collectors.toList()));
        } else {
            return space;
        }
    }

    private List<Space> merge(List<Space> space) {
        return space.stream().map(e -> space.stream()//
                .filter(x -> !e.equals(x))//
                .filter(x -> e.needToCombineSpace(x, stackConfig))//
                .map(x -> e.combineSpace(x, stackConfig)).filter(Objects::nonNull)).flatMap(e -> e).distinct()
                .filter(e -> !space.stream().filter(x -> e.equals(x) || x.overlay(e)).findAny().isPresent())
                .collect(Collectors.toList());
    }

    public Bin binTurn(Rotation rotation) {
        if (rotation.equals(Rotation.whl)) {
            final Bin rotatedBin = new Bin(super.rotate(rotation));
            this.cargo().stream().forEach(e -> rotatedBin.add(e.cargoTurn(rotation)));
            return rotatedBin;
        } else if (rotation.equals(Rotation.lhw))
            return this;
        else {
            throw new RuntimeException(
                    "Bin can turn only side-way using Rotation.whl. Current rotation: " + rotation.name());
        }
    }
}
