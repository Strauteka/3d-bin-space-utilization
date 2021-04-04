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
        // bins.add(test2());
        // bins.add(test3());
        // bins.add(test4());
        bins.add(test7());
        Draw3d.draw(bins.toArray(new Bin[0]));
    }

    public static Bin test3() {
        final Bin b = new Bin(new Size(1000, 1000, 1000), new StackConfig(20, 50, 20, 50, 50, false));
        final Cargo<Size> w = new Cargo<Size>(new Size(900, 200, 900), new Size(50, 0, 50));
        b.add(w);
        // b.add(xx);
        return b;
    }

    public static Bin test2() {
        final Bin b = new Bin(new Size(1000, 700, 1000));
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
        final Bin b = new Bin(new Size(1000, 700, 1000), new StackConfig(20, 1000, 20, 1000, 0, false));
        final Cargo<Size> w = new Cargo<Size>(new Size(300, 200, 400), new Size(150, 0, 150));
        final Cargo<Size> x = new Cargo<Size>(new Size(200, 200, 300), new Size(200, 0, 550));
        final Cargo<Size> y = new Cargo<Size>(new Size(200, 200, 200), new Size(480, 0, 150));

        b.add(x);
        b.add(y);
        b.add(w);
        return b;
    }

    public static Bin test7() {
        final Bin a = new Bin(new Size(3000, 1000, 2000));

        final Cargo<Pallet> c = new Cargo<Pallet>(getp(), Rotation.lhw, new Size(0, 0, 0));
        a.add(c);
        final Cargo<Pallet> cc = new Cargo<Pallet>(getp(), Rotation.whl, new Size(0, 0, 1000));
        a.add(cc);
        final Cargo<Pallet> ccc = new Cargo<Pallet>(getp(), Rotation.hlw, new Size(1000, 0, 0));
        a.add(ccc);
        final Cargo<Pallet> cccc = new Cargo<Pallet>(getp(), Rotation.wlh, new Size(1000, 0, 1000));
        a.add(cccc);
        final Cargo<Pallet> ccccc = new Cargo<Pallet>(getp(), Rotation.hwl, new Size(2000, 0, 0));
        a.add(ccccc);
        final Cargo<Pallet> cccccc = new Cargo<Pallet>(getp(), Rotation.lwh, new Size(2000, 0, 1000));
        a.add(cccccc);
        return a;
    }

    public static Pallet getp() {
        final Pallet b = new Pallet(new Size(600, 500, 300), 50);
        final Cargo<Size> s = new Cargo<Size>(new Size(200, 100, 50), 2, 1, 4, Rotation.hlw, new Size(300, 0, 0));
        final Cargo<Size> x = new Cargo<Size>(new Size(200, 100, 50), 1, 1, 1, Rotation.hwl, new Size(0, 0, 100));
        final Cargo<Size> c = new Cargo<Size>(new Size(200, 100, 50), 1, 1, 1, Rotation.lwh, new Size(300, 200, 0));
        b.palletBin().add(x);
        b.palletBin().add(s);
        b.palletBin().add(c);
        return b;
    }

}
