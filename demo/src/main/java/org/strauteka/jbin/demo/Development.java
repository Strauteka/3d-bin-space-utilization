package org.strauteka.jbin.demo;

import java.util.ArrayList;
import java.util.List;

import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.core.Cargo;
import org.strauteka.jbin.core.Size;
import org.strauteka.jbin.core.configuration.StackConfig;
import org.strauteka.jbin.draw3d.Draw3d;

public class Development {

    public static void draw() {
        List<Bin> bins = new ArrayList<>();
        bins.add(test2());
        bins.add(test3());
        Draw3d.draw(bins.toArray(new Bin[0]));
    }

    public static Bin test3() {
        final Bin b = new Bin(new Size(1000, 1000, 1000), new StackConfig(20, 50, 20, 50));
        final Cargo<Size> w = new Cargo<Size>(new Size(900, 200, 900), new Size(50, 0, 50));
        b.add(w);
        // b.add(xx);
        return b;
    }

    public static Bin test2() {
        final Bin b = new Bin(new Size(1000, 1000, 1000), new StackConfig(20, 50, 20, 50));
        final Cargo<Size> s = new Cargo<Size>(new Size(200, 200, 200), new Size(0, 0, 0));
        final Cargo<Size> x = new Cargo<Size>(new Size(300, 200, 500), new Size(0, 0, 200));
        final Cargo<Size> y = new Cargo<Size>(new Size(500, 300, 200), new Size(200, 0, 0));
        final Cargo<Size> z = new Cargo<Size>(new Size(300, 200, 300), new Size(300, 0, 200));
        final Cargo<Size> w = new Cargo<Size>(new Size(600, 200, 300), new Size(200, 0, 700));
        final Cargo<Size> o = new Cargo<Size>(new Size(300, 200, 200), new Size(300, 0, 500));
        // final Cargo<Size> xx = new Cargo<Size>(new Size(200, 200, 200), Rotation.lhw,
        // new Size(100, 200, 300));

        b.add(s);
        b.add(x);
        b.add(y);
        b.add(z);
        b.add(w);
        b.add(o);
        // b.add(xx);
        return b;
    }
}
