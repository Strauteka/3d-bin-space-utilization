package org.strauteka.jbin.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.strauteka.jbin.core.Utils.Rotation;

public class Bin extends Size {
    final List<Cargo<?>> cargo = new ArrayList<Cargo<?>>();
    List<Space> space = new ArrayList<Space>();

    public Bin(Dimension size) {
        super(size);
        space.add(new Space(this, new Size(0, 0, 0)));
    }

    public void add(Cargo<?> cargo) {
        add(cargo, false);
    }

    public void add(Cargo<?> cargo, boolean disableTop) {
        this.cargo.add(cargo);
        this.space = mergeAll(dropOverlapSpace(createSpace(space, cargo, disableTop)));
    }

    private List<Space> createSpace(List<Space> space, Cargo<?> cargo, boolean disableTop) {
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

    public List<Space> emptySpace() {
        return Collections.unmodifiableList(space);
    }

    public List<Cargo<?>> cargo() {
        return Collections.unmodifiableList(cargo);
    }

    private List<Space> mergeAll(List<Space> space) {
        final List<Space> mergedItems = merge(space);
        if (!mergedItems.isEmpty()) {
            return mergeAll(
                    dropOverlapSpace(Stream.concat(mergedItems.stream(), space.stream()).collect(Collectors.toList())));
        } else {
            return space;
        }
    }

    private List<Space> merge(List<Space> space) {
        return space.stream().filter(e -> e.h_() > 0)
                .map(e -> space.stream().filter(x -> e.h_() == x.h_() && !e.equals(x))
                        .filter(x -> e.neighborLeftOrFront(x)).map(x -> e.combineSpace(x)).flatMap(x -> x.stream()))
                .flatMap(e -> e)
                .filter(e -> !space.stream().filter(x -> e.equals(x) || x.overlay(e)).findAny().isPresent()).distinct()
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
