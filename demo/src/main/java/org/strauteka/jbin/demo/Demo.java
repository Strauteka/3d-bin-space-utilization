package org.strauteka.jbin.demo;

import java.util.ArrayList;
import java.util.List;

import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.core.Cargo;
import org.strauteka.jbin.core.Pallet;
import org.strauteka.jbin.core.Size;
import org.strauteka.jbin.core.Utils.Rotation;
import org.strauteka.jbin.core.configuration.StackConfig;
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
        List<Bin> bins = new ArrayList<>();
        Size binSize = new Size(500, 500, 500);
        Size s = new Size(150, 300, 500);
        Size pos = new Size(0, 0, 0);
        final Bin b1 = new Bin(binSize);
        final Bin b2 = new Bin(binSize);
        final Bin b3 = new Bin(binSize);
        final Bin b4 = new Bin(binSize);
        final Bin b5 = new Bin(binSize);
        final Bin b6 = new Bin(binSize);

        b1.add(new Cargo<Size>(s, Rotation.lhw, pos));
        b2.add(new Cargo<Size>(s, Rotation.whl, pos));
        b3.add(new Cargo<Size>(s, Rotation.wlh, pos));
        b4.add(new Cargo<Size>(s, Rotation.hlw, pos));
        b5.add(new Cargo<Size>(s, Rotation.hwl, pos));
        b6.add(new Cargo<Size>(s, Rotation.lwh, pos));
        bins.add(b1);
        bins.add(b2);
        bins.add(b3);
        bins.add(b4);
        bins.add(b5);
        bins.add(b6);
        Draw3d.draw(true, bins.toArray(new Bin[0]));
    }

    public static Bin test3() {
        final Bin b = new Bin(new Size(1000, 1000, 1000));
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

    public static Bin test4() {
        final Bin b = new Bin(new Size(1000, 1000, 1000));
        final Cargo<Size> s = new Cargo<Size>(new Size(700, 200, 200), new Size(0, 0, 0));
        final Cargo<Size> x = new Cargo<Size>(new Size(150, 200, 500), new Size(0, 800, 300));
        final Cargo<Size> y = new Cargo<Size>(new Size(300, 200, 200), new Size(0, 0, 200));
        b.add(s);
        b.add(x);
        b.add(y);

        return b;
    }

    public static Bin test5() {
        final Bin b = new Bin(new Size(1000, 1000, 1000));
        final Cargo<Size> s = new Cargo<Size>(new Size(100, 50, 50), 4, 8, 3, Rotation.whl, new Size(0, 0, 0));
        final Cargo<Size> x = new Cargo<Size>(new Size(100, 50, 50), 5, 5, 3, Rotation.whl, new Size(0, 0, 500));
        b.add(s);
        b.add(x);
        return b;
    }

    public static Bin test6() {
        final Bin b = new Bin(new Size(1000, 1000, 1000));
        final Cargo<Size> s = new Cargo<Size>(new Size(100, 50, 50), 4, 8, 2, new Size(300, 300, 300));
        final Cargo<Size> x = new Cargo<Size>(new Size(100, 50, 50), 4, 8, 4, new Size(300, 300, 400));
        b.add(s);
        b.add(x);
        return b;
    }

    public static Bin test7() {
        final Bin a = new Bin(new Size(1000, 1000, 1000));
        final Pallet b = new Pallet(new Size(600, 500, 300), 50);
        final Cargo<Size> s = new Cargo<Size>(new Size(100, 50, 50), 4, 4, 2, Rotation.wlh, new Size(300, 0, 0));
        b.palletBin().add(s);
        final Cargo<Size> x = new Cargo<Size>(new Size(100, 50, 50), 4, 4, 3, Rotation.wlh, new Size(100, 0, 100));
        b.palletBin().add(x);
        final Cargo<Pallet> c = new Cargo<Pallet>(b, new Size(400, 0, 0));
        a.add(c);
        final Cargo<Pallet> cc = new Cargo<Pallet>(b, Rotation.whl, new Size(0, 0, 0));
        a.add(cc);
        return a;
    }

    public static Bin test8() {
        final Bin b = new Bin(new Size(1000, 1000, 1000), new StackConfig(50, 1000, 50, 1000, 50));
        final Cargo<Size> w = new Cargo<Size>(new Size(300, 200, 400), new Size(150, 0, 150));
        final Cargo<Size> x = new Cargo<Size>(new Size(200, 200, 300), new Size(200, 0, 550));
        final Cargo<Size> y = new Cargo<Size>(new Size(200, 200, 200), new Size(450, 0, 150));

        b.add(x);
        b.add(y);
        b.add(w);
        return b;
    }
}
