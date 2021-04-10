package org.strauteka.jbin.demo.algorithm;

import java.util.List;

import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.core.Cargo;
import org.strauteka.jbin.core.Dimension;
import org.strauteka.jbin.core.Size;
import org.strauteka.jbin.core.Space;
import org.strauteka.jbin.core.configuration.StackConfig;

public class Pallet extends Bin {
    private final Bin bin;

    public Pallet(Bin bin) {
        super(bin);
        this.bin = bin;
    }

    public static Pallet pallet(Dimension size, int h) {
        return new Pallet(new Bin(size).add(new Cargo<Size>(new Size(size.l(), h, size.w()), new Size(0, 0, 0))));
    }

    @Override
    public Pallet add(Cargo<?> cargo) {
        return new Pallet(bin.add(cargo));
    }

    @Override
    public Pallet add(Cargo<? extends Dimension> cargo, boolean disableTop) {
        return new Pallet(bin.add(cargo, disableTop));
    }

    @Override
    public List<Space> emptySpace() {
        return bin.emptySpace();
    }

    @Override
    public List<Cargo<? extends Dimension>> cargo() {
        return bin.cargo();
    }

    @Override
    public StackConfig stackConfig() {
        return bin.stackConfig();
    }
}
