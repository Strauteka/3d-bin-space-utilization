package org.strauteka.jbin.core;

import org.strauteka.jbin.core.configuration.StackConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public abstract class Bin<SELF extends Bin<SELF>> extends Size {

    private final StackConfig stackConfig;
    private final List<Cargo<? extends Dimension>> cargo;
    private final List<Space> space;

    public Bin(Dimension size, StackConfig overStack, List<Space> space, List<Cargo<? extends Dimension>> cargo) {
        super(size);
        this.stackConfig = ofNullable(overStack)
                .orElse(new StackConfig(0, 0, 0, 0, 0, 0, false));
        this.cargo = unmodifiableList(new ArrayList<>(ofNullable(cargo)
                .orElseGet(ArrayList::new)));
        // collect toUnmodifiableList error java 1.8 todo: fix?
        this.space = unmodifiableList(new ArrayList<>(ofNullable(space)
                .orElse(new ArrayList<>(singletonList(new Space(this, new Size(0, 0, 0)))))));
    }

    public abstract SELF self();

    public SELF add(Cargo<? extends Dimension> cargo) {
        return add(cargo, false, stackConfig);
    }

    public abstract SELF add(Cargo<? extends Dimension> cargo, boolean disableTop, StackConfig stackConfig);

    protected List<Space> createFilterSpace(Cargo<? extends Dimension> cargo, boolean disableTop,
                                            StackConfig stackConfig) {
        final Space affectedArea = new Space(
                cargo.l() + (stackConfig.l() * 2),
                cargo.h() + stackConfig.equalizeH(),
                cargo.w() + (stackConfig.w() * 2),
                cargo.l_() - stackConfig.l(),
                cargo.h_(),
                cargo.w_() - stackConfig.w());

        final Map<Boolean, List<Space>> splitAffectedSpace = space.stream()
                .collect(groupingBy(affectedArea::overlapIncluded, toList()));

        return Stream
                .concat(splitAffectedSpace
                                .getOrDefault(false, new ArrayList<>()).stream(),
                        calculateAffectedArea(splitAffectedSpace
                                        .getOrDefault(true, new ArrayList<>()),
                                cargo,
                                disableTop,
                                stackConfig
                        )
                )
                .collect(toList());
    }

    private Stream<Space> calculateAffectedArea(List<Space> affectedSpace,
                                                Cargo<? extends Dimension> cargo,
                                                Boolean disableTop,
                                                StackConfig stackConfig) {
        return dropUnusableSpace(
                dropOverlapSpace(
                        mergeAll(
                                createSpace(affectedSpace, cargo, disableTop),
                                stackConfig
                        )
                ),
                stackConfig.minimumSpaceSide()
        );
    }

    protected List<Cargo<? extends Dimension>> createCargo(Cargo<? extends Dimension> cargo) {
        return Stream.concat(this.cargo.stream(), Stream.of(cargo)).collect(toList());
    }

    private List<Space> createSpace(List<Space> space, Cargo<? extends Dimension> cargo, boolean disableTop) {
        return Stream.concat(
                        space.stream()
                                .filter(e -> e.overlap(cargo))
                                .map(e -> e.createSpace(cargo, disableTop))
                                .flatMap(Collection::stream)
                                .distinct(),
                        space.stream()
                                .filter(e -> !e.overlap(cargo)))
                .collect(toList());
    }

    private Stream<Space> dropOverlapSpace(final List<Space> space) {
        return space.stream()
                .filter(e -> space.stream().noneMatch(x -> !e.equals(x) && x.overlay(e)));
    }

    private Stream<Space> dropUnusableSpace(Stream<Space> space, int minimumSpaceSide) {
        return space.filter(e -> Math.min(Math.min(e.l(), e.h()), e.w()) > minimumSpaceSide);
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
                                    .filter(e -> mergedItems.stream()
                                            .noneMatch(x -> x.overlay(e))))
                    .collect(toList()), stackConfig);

        return space;
    }

    private List<Space> merge(List<Space> space, StackConfig stackConfig) {
        return space.stream()
                .flatMap(outer ->
                        space.stream()
                                .filter(inner -> !outer.equals(inner))
                                .filter(inner -> outer.needToCombineSpace(inner, stackConfig))
                                .map(x -> outer.combineSpace(x, stackConfig)))
                .distinct()
                .filter(outer -> space.stream().noneMatch(x -> x.overlay(outer)))
                .collect(toList());
    }

    public abstract SELF binRotate(Rotation rotation);
}
