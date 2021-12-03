package org.strauteka.jbin.demo.algorithm;

import java.util.List;
import java.util.stream.Stream;

import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.core.Cargo;
import org.strauteka.jbin.core.Dimension;
import org.strauteka.jbin.core.Size;
import org.strauteka.jbin.core.Space;
import org.strauteka.jbin.core.Rotation;
import org.strauteka.jbin.core.configuration.StackConfig;

public class Pallet extends Bin<Pallet> implements Item {
    public final static Rotation[] rotations;
    static {
        rotations = Stream.of(Rotation.lhw, Rotation.whl).toArray(Rotation[]::new);
    }

    private Pallet(Dimension size) {
        this(size, null);
    }

    private Pallet(Dimension size, StackConfig overStack) {
        this(size, overStack, null, null);
    }

    private Pallet(Dimension size, StackConfig overStack, List<Space> space, List<Cargo<? extends Dimension>> cargo) {
        super(size, overStack, space, cargo);
    }

    public static Pallet pallet(Dimension size, int h) {
        return new Pallet(size).add(new Cargo<>(new Size(size.l(), h, size.w()), new Size(0, 0, 0)));
    }

    public static Pallet pallet(Dimension size, int h, StackConfig overStack) {
        return new Pallet(size, overStack).add(new Cargo<>(new Size(size.l(), h, size.w()), new Size(0, 0, 0)));
    }

    @Override
    public Pallet add(Cargo<? extends Dimension> cargo, boolean disableTop, StackConfig stackConfig) {
        return new Pallet(this, super.stackConfig(), super.createFilterSpace(cargo, disableTop, stackConfig),
                super.createCargo(cargo));
    }

    @Override
    public Pallet self() {
        return this;
    }

    @Override
    public int qty() {
        return 1;
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public Rotation[] rotations() {
        return rotations;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return o == this;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public Pallet binRotate(Rotation rotation) {
        return this.cargo().stream().reduce(new Pallet(rotate(rotation), this.stackConfig()),
                (bin, cargo) -> bin.add(cargo.cargoRotate(bin, rotation)), (bin1, bin2) -> {
                    throw new UnsupportedOperationException();
                });
    }
}
