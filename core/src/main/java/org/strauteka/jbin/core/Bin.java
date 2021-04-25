package org.strauteka.jbin.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays;

import org.strauteka.jbin.core.configuration.StackConfig;

public abstract class Bin<SELF extends Bin<SELF>> extends Size {

    private final StackConfig stackConfig;
    private final List<Cargo<? extends Dimension>> cargo;
    private final List<Space> space;

    public Bin(Dimension size, StackConfig overStack, List<Space> space, List<Cargo<? extends Dimension>> cargo) {
        super(size);
        this.stackConfig = Optional.ofNullable(overStack).orElseGet(() -> new StackConfig(0, 0, 0, 0, 0, 0, false));
        this.cargo = Collections.unmodifiableList(Optional.ofNullable(cargo)
                .orElseGet(() -> new ArrayList<Cargo<? extends Dimension>>()).stream().collect(Collectors.toList()));
        this.space = Collections.unmodifiableList(Optional.ofNullable(space)
                .orElseGet(() -> new ArrayList<Space>(Arrays.asList(new Space(this, new Size(0, 0, 0))))).stream()
                .collect(Collectors.toList())); // collect toUnmodifiableList error java 1.8 todo: fix?
    }

    public abstract SELF self();

    public SELF add(Cargo<? extends Dimension> cargo) {
        return add(cargo, false, stackConfig);
    }

    public abstract SELF add(Cargo<? extends Dimension> cargo, boolean disableTop, StackConfig stackConfig);

    protected List<Space> createFilterSpace(Cargo<? extends Dimension> cargo, boolean disableTop,
            StackConfig stackConfig) {
        final Space affectedArea = new Space(cargo.l() + (stackConfig.l() * 2), cargo.h() + stackConfig.equalizeH(),
                cargo.w() + (stackConfig.w() * 2), cargo.l_() - stackConfig.l(), cargo.h_(),
                cargo.w_() - stackConfig.w());
        final Map<Boolean, List<Space>> splitAffectedSpace = space.stream()
                .collect(Collectors.groupingBy(e -> affectedArea.overlapIncluded(e), Collectors.toList()));
        return Stream
                .concat(splitAffectedSpace.entrySet().stream().filter(e -> !e.getKey())
                        .flatMap(e -> e.getValue().stream()),
                        dropUnusableSpace(dropOverlapSpace(
                                mergeAll(createSpace(splitAffectedSpace.getOrDefault(true, new ArrayList<>()), cargo,
                                        disableTop), stackConfig)),
                                stackConfig.minimumSpaceSide()).stream())
                .collect(Collectors.toList());
    }

    protected List<Cargo<? extends Dimension>> createCargo(Cargo<? extends Dimension> cargo) {
        return Stream.concat(this.cargo.stream(), Stream.of(cargo)).collect(Collectors.toList());
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

    private List<Space> dropUnusableSpace(final List<Space> space, int minimumSpaceSide) {
        return space.stream().filter(e -> Math.min(Math.min(e.l(), e.h()), e.w()) > minimumSpaceSide)
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

    private List<Space> mergeAll(List<Space> space, StackConfig stackConfig) {
        if (stackConfig.disableMerge())
            return space;
        final List<Space> mergedItems = merge(space, stackConfig);
        if (!mergedItems.isEmpty())
            return mergeAll(Stream
                    .concat(mergedItems.stream(),
                            space.stream()
                                    .filter(e -> !mergedItems.stream().filter(x -> x.overlay(e)).findAny().isPresent()))
                    .collect(Collectors.toList()), stackConfig);

        return space;
    }

    private List<Space> merge(List<Space> space, StackConfig stackConfig) {
        return space.stream().map(e -> space.stream()//
                .filter(x -> !e.equals(x))//
                .filter(x -> e.needToCombineSpace(x, stackConfig))//
                .map(x -> e.combineSpace(x, stackConfig)).filter(Objects::nonNull)).flatMap(e -> e).distinct()
                .filter(e -> !space.stream().filter(x -> x.overlay(e)).findAny().isPresent())
                .collect(Collectors.toList());
    }

    public abstract SELF binRotate(Rotation rotation);
}
