package org.strauteka.jbin.demo.algorithm;

import java.util.List;

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
        super(bin, overStack, bin.emptySpace(), bin.cargo());
    }

    public Container(int l, int h, int w) {
        this(new Size(l, h, w));
    }

    public Container(int l, int h, int w, StackConfig overStack) {
        this(new Size(l, h, w), overStack);
    }

    public Container(Dimension size) {
        this(size, null);
    }

    public Container(Dimension size, StackConfig overStack) {
        super(size, overStack, null, null);
    }

    public Container(Dimension size, StackConfig overStack, List<Space> space, List<Cargo<? extends Dimension>> cargo) {
        super(size, overStack, space, cargo);
    }

    @Override
    public Container add(Cargo<? extends Dimension> cargo, boolean disableTop, StackConfig stackConfig) {
        return new Container(this, super.stackConfig(), super.createFilterSpace(cargo, disableTop, stackConfig),
                super.createCargo(cargo));
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
