package org.strauteka.jbin.demo;

import java.util.ArrayList;
import java.util.List;

import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.core.Cargo;
import org.strauteka.jbin.core.Size;
import org.strauteka.jbin.core.Utils.Rotation;
import org.strauteka.jbin.core.configuration.StackConfig;
import org.strauteka.jbin.demo.algorithm.Pallet;
import org.strauteka.jbin.draw3d.Draw3d;

public class Development {

    public static void draw() {
        List<Bin> bins = new ArrayList<>();
        bins.add(test2());
        bins.add(test3());
        bins.add(test4());
        bins.add(test7());
        Draw3d.draw(bins.toArray(new Bin[0]));
    }

    public static Bin test3() {
        return new Bin(1000, 1000, 1000, new StackConfig(20, 50, 20, 50, 50, false))
                .add(new Cargo<Size>(new Size(900, 200, 900), new Size(50, 0, 50)));
    }

    public static Bin test2() {
        return new Bin(1000, 700, 1000).add(new Cargo<Size>(new Size(200, 200, 200), new Size(0, 0, 0)))
                .add(new Cargo<Size>(new Size(300, 200, 500), new Size(0, 0, 200)))
                .add(new Cargo<Size>(new Size(500, 300, 200), new Size(200, 0, 0)))
                .add(new Cargo<Size>(new Size(300, 200, 300), new Size(300, 0, 200)))
                .add(new Cargo<Size>(new Size(600, 200, 300), new Size(200, 0, 700)))
                .add(new Cargo<Size>(new Size(300, 200, 200), new Size(300, 0, 500)));
    }

    public static Bin test4() {
        return new Bin(1000, 700, 1000, new StackConfig(20, 1000, 20, 1000, 0, false))
                .add(new Cargo<Size>(new Size(300, 200, 400), new Size(150, 0, 150)))
                .add(new Cargo<Size>(new Size(200, 200, 300), new Size(200, 0, 550)))
                .add(new Cargo<Size>(new Size(200, 200, 200), new Size(480, 0, 150)));
    }

    public static Bin test7() {
        return new Bin(3000, 1000, 2000)//
                .add(new Cargo<Pallet>(getPallet(), Rotation.lhw, new Size(0, 0, 0)))
                .add(new Cargo<Pallet>(getPallet(), Rotation.whl, new Size(0, 0, 1000)))
                .add(new Cargo<Pallet>(getPallet(), Rotation.hlw, new Size(1000, 0, 0)))
                .add(new Cargo<Pallet>(getPallet(), Rotation.wlh, new Size(1000, 0, 1000)))
                .add(new Cargo<Pallet>(getPallet(), Rotation.hwl, new Size(2000, 0, 0)))
                .add(new Cargo<Pallet>(getPallet(), Rotation.lwh, new Size(2000, 0, 1000)));
    }

    public static Pallet getPallet() {
        return Pallet.pallet(new Size(600, 500, 300), 50)
                .add(new Cargo<Size>(new Size(200, 100, 50), 2, 1, 4, Rotation.hlw, new Size(300, 50, 0)))
                .add(new Cargo<Size>(new Size(200, 100, 50), 1, 1, 1, Rotation.hwl, new Size(0, 50, 100)))
                .add(new Cargo<Size>(new Size(200, 100, 50), 1, 1, 1, Rotation.lwh, new Size(300, 250, 0)));
    }
}
