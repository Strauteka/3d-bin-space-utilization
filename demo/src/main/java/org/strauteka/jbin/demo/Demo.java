package org.strauteka.jbin.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.core.Cargo;
import org.strauteka.jbin.core.Dimension;
import org.strauteka.jbin.core.Size;
import org.strauteka.jbin.core.Utils.Rotation;
import org.strauteka.jbin.demo.algorithm.Pallet;
import org.strauteka.jbin.draw3d.Draw3d;

public class Demo {

    public static void draw() {
        List<Bin> bins = new ArrayList<>();
        bins.add(test3());
        bins.add(test4());
        bins.add(test5());
        bins.add(test6());
        bins.add(test7());
        bins.add(test8());
        Draw3d.draw(bins.toArray(new Bin[0]));
    }

    public static void drawRotations() {
        final List<Bin> bins = new ArrayList<>();
        final Bin bin = new Bin(500, 500, 500);
        final Size s = new Size(150, 300, 500);
        final Size pos = new Size(0, 0, 0);

        bins.add(bin.add(new Cargo<Size>(s, Rotation.lhw, pos)));
        bins.add(bin.add(new Cargo<Size>(s, Rotation.whl, pos)));
        bins.add(bin.add(new Cargo<Size>(s, Rotation.wlh, pos)));
        bins.add(bin.add(new Cargo<Size>(s, Rotation.hlw, pos)));
        bins.add(bin.add(new Cargo<Size>(s, Rotation.hwl, pos)));
        bins.add(bin.add(new Cargo<Size>(s, Rotation.lwh, pos)));
        Map<Dimension, Color> colors = new HashMap<>();
        colors.put(s, new Color(100, 100, 100));
        Draw3d.draw(true, false, 1200, 800, colors, bins.toArray(new Bin[0]));
    }

    public static Bin test3() {
        return new Bin(1000, 1000, 1000)//
                .add(new Cargo<Size>(new Size(200, 200, 200), new Size(0, 0, 0)))
                .add(new Cargo<Size>(new Size(300, 200, 500), new Size(0, 0, 200)))
                .add(new Cargo<Size>(new Size(500, 300, 200), new Size(200, 0, 0)))
                .add(new Cargo<Size>(new Size(300, 200, 300), new Size(300, 0, 200)))
                .add(new Cargo<Size>(new Size(600, 200, 300), new Size(200, 0, 700)))
                .add(new Cargo<Size>(new Size(300, 200, 200), new Size(300, 0, 500)));
    }

    public static Bin test4() {
        return new Bin(1000, 1000, 1000).add(new Cargo<Size>(new Size(700, 200, 200), new Size(0, 0, 0)))
                .add(new Cargo<Size>(new Size(150, 200, 500), new Size(0, 800, 300)))
                .add(new Cargo<Size>(new Size(300, 200, 200), new Size(0, 0, 200)));
    }

    public static Bin test5() {
        return new Bin(1000, 1000, 1000)
                .add(new Cargo<Size>(new Size(100, 55, 50), 4, 8, 3, Rotation.whl, new Size(0, 0, 0)))
                .add(new Cargo<Size>(new Size(100, 45, 50), 5, 5, 3, Rotation.whl, new Size(0, 0, 500)));
    }

    public static Bin test6() {
        return new Bin(1000, 1000, 1000).add(new Cargo<Size>(new Size(100, 50, 50), 4, 8, 2, new Size(300, 300, 300)))
                .add(new Cargo<Size>(new Size(100, 50, 50), 4, 8, 4, new Size(300, 300, 400)));
    }

    public static Bin test7() {
        final Pallet pallet = Pallet.pallet(new Size(600, 500, 300), 50)
                .add(new Cargo<Size>(new Size(100, 50, 50), 4, 4, 2, Rotation.wlh, new Size(300, 50, 0)))
                .add(new Cargo<Size>(new Size(100, 50, 50), 4, 4, 3, Rotation.wlh, new Size(100, 50, 100)));
        return new Bin(new Size(1000, 1000, 1000)).add(new Cargo<Pallet>(pallet, new Size(400, 0, 0)))
                .add(new Cargo<Pallet>(pallet, Rotation.whl, new Size(0, 0, 0)));
    }

    public static Bin test8() {
        return new Bin(1000, 1000, 1000).add(new Cargo<Size>(new Size(300, 200, 400), new Size(150, 0, 150)))
                .add(new Cargo<Size>(new Size(200, 200, 300), new Size(200, 0, 550)))
                .add(new Cargo<Size>(new Size(200, 200, 200), new Size(450, 0, 150)));
    }
}
