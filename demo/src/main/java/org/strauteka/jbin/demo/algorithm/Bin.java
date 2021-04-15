package org.strauteka.jbin.demo.algorithm;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.strauteka.jbin.core.AbstractBin;
import org.strauteka.jbin.core.Cargo;
import org.strauteka.jbin.core.Dimension;
import org.strauteka.jbin.core.Rotation;
import org.strauteka.jbin.core.Size;
import org.strauteka.jbin.core.Space;
import org.strauteka.jbin.core.configuration.StackConfig;

public class Bin extends AbstractBin<Bin> {
    public Bin(Bin bin) {
        this(bin, bin.stackConfig());
    }

    public Bin(Bin bin, StackConfig overStack) {
        super(bin, Optional.ofNullable(overStack).orElseGet(() -> bin.stackConfig()), bin.emptySpace(), bin.cargo());
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
        super(size, overStack, null, null);
    }

    public Bin(Dimension size, StackConfig overStack, List<Space> space, List<Cargo<? extends Dimension>> cargo) {
        super(size, overStack, space, cargo);
    }

    @Override
    public Bin add(Cargo<? extends Dimension> cargo, boolean disableTop) {
        return new Bin(this, super.stackConfig(),
                super.dropUnusableSpace(super.mergeAll(
                        super.dropOverlapSpace(super.createSpace(super.emptySpace(), cargo, disableTop)))),
                Stream.concat(super.cargo().stream(), Stream.of(cargo)).collect(Collectors.toList()));
    }

    @Override
    public Bin self() {
        return this;
    }

    @Override
    public Bin binRotate(Rotation rotation) {
        return this.cargo().stream().reduce(new Bin(rotate(rotation), this.stackConfig()),
                (bin, cargo) -> bin.add(cargo.cargoRotate(bin, rotation)), (bin1, bin2) -> {
                    throw new UnsupportedOperationException();
                });
    }
}
