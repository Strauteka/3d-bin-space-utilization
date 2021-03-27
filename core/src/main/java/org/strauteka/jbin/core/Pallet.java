package org.strauteka.jbin.core;

public class Pallet extends Bin {
    private Bin palletBin;
    private Size pallet;

    public Pallet(Dimension size, int h) {
        super(size);
        this.palletBin = new Bin(new Size(size.l(), size.h() - h, size.w()));
        this.pallet = new Size(this.l(), h, this.w());
        super.add(new Cargo<Size>(this.pallet, new Size(0, 0, 0)));
        super.add(new Cargo<Bin>(this.palletBin, new Size(0, h, 0)));
    }

    public Bin palletBin() {
        return palletBin;
    }

    public Size pallet() {
        return pallet;
    }

    @Override
    public void add(Cargo<?> cargo) {
        throw new RuntimeException("Use function Pallet.palletBin().add(Cargo<?> cargo) when adding boxes to pallet!");
    }
}
