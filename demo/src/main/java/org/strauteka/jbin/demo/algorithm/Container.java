package org.strauteka.jbin.demo.algorithm;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.core.Cargo;
import org.strauteka.jbin.core.Dimension;
import org.strauteka.jbin.core.Rotation;
import org.strauteka.jbin.core.Size;
import org.strauteka.jbin.core.Space;
import org.strauteka.jbin.core.configuration.StackConfig;

public class Container extends Bin<Container> {
    public Container(Container bin) {
        this(bin, bin.stackConfig());
    }

    public Container(Container bin, StackConfig overStack) {
        super(bin, Optional.ofNullable(overStack).orElseGet(() -> bin.stackConfig()), bin.emptySpace(), bin.cargo());
    }

    public Container(int l, int h, int w) {
        this(new Size(l, h, w));
    }

    public Container(int l, int h, int w, StackConfig overStack) {
        this(new Size(l, h, w), overStack);
    }

    public Container(Dimension size) {
        this(size, new StackConfig(0, 0, 0, 0, 0, false));
    }

    public Container(Dimension size, StackConfig overStack) {
        super(size, overStack, null, null);
    }

    public Container(Dimension size, StackConfig overStack, List<Space> space, List<Cargo<? extends Dimension>> cargo) {
        super(size, overStack, space, cargo);
    }

    @Override
    public Container add(Cargo<? extends Dimension> cargo, boolean disableTop) {
        return new Container(this, super.stackConfig(),
                super.dropUnusableSpace(super.mergeAll(
                        super.dropOverlapSpace(super.createSpace(super.emptySpace(), cargo, disableTop)))),
                Stream.concat(super.cargo().stream(), Stream.of(cargo)).collect(Collectors.toList()));
    }

    @Override
    public Container self() {
        return this;
    }

    @Override
    public Container binRotate(Rotation rotation) {
        return this.cargo().stream().reduce(new Container(rotate(rotation), this.stackConfig()),
                (bin, cargo) -> bin.add(cargo.cargoRotate(bin, rotation)), (bin1, bin2) -> {
                    throw new UnsupportedOperationException();
                });
    }
}
