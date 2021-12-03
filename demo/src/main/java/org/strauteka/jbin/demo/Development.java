package org.strauteka.jbin.demo;

import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.core.Cargo;
import org.strauteka.jbin.core.Rotation;
import org.strauteka.jbin.core.Size;
import org.strauteka.jbin.core.configuration.StackConfig;
import org.strauteka.jbin.demo.algorithm.Container;
import org.strauteka.jbin.demo.algorithm.Pallet;
import org.strauteka.jbin.draw3d.Draw3d;
import java.util.stream.Stream;

public class Development {
    public static void main(String[] args) {
        Draw3d.draw(
                Stream.of(
                        test2(),
                        test3(),
                        test4(),
                        test7()
                ).toArray(Bin[]::new));
    }

    public static Container test3() {
        final StackConfig overStack = new StackConfig(20, 50, 20, 50, 10, 50, false);
        return new Container(1000, 1000, 1000, overStack)
                .add(new Cargo<>(new Size(900, 200, 900), new Size(50, 0, 50)));
    }

    public static Container test2() {
        return new Container(1000, 700, 1000)
                .add(new Cargo<>(new Size(200, 200, 200), new Size(0, 0, 0)))
                .add(new Cargo<>(new Size(300, 200, 500), new Size(0, 0, 200)))
                .add(new Cargo<>(new Size(500, 300, 200), new Size(200, 0, 0)))
                .add(new Cargo<>(new Size(300, 200, 300), new Size(300, 0, 200)))
                .add(new Cargo<>(new Size(600, 200, 300), new Size(200, 0, 700)))
                .add(new Cargo<>(new Size(300, 200, 200), new Size(300, 0, 500)));
    }

    public static Container test4() {
        return new Container(1000, 700, 1000, new StackConfig(20, 1000, 20, 1000, 5, 0, false))
                .add(new Cargo<>(new Size(300, 200, 400), new Size(150, 0, 150)))
                .add(new Cargo<>(new Size(200, 200, 300), new Size(200, 0, 550)))
                .add(new Cargo<>(new Size(200, 200, 200), new Size(480, 0, 150)))
                .add(new Cargo<>(new Size(100, 195, 200), new Size(700, 0, 150)))
                .add(new Cargo<>(new Size(100, 190, 200), new Size(800, 0, 150)));
    }

    public static Container test7() {
        return new Container(3000, 1000, 2000)//
                .add(new Cargo<>(getPallet(), Rotation.lhw, new Size(0, 0, 0)))
                .add(new Cargo<>(getPallet(), Rotation.whl, new Size(0, 0, 1000)))
                .add(new Cargo<>(getPallet(), Rotation.hlw, new Size(1000, 0, 0)))
                .add(new Cargo<>(getPallet(), Rotation.wlh, new Size(1000, 0, 1000)))
                .add(new Cargo<>(getPallet(), Rotation.hwl, new Size(2000, 0, 0)))
                .add(new Cargo<>(getPallet(), Rotation.lwh, new Size(2000, 0, 1000)));
    }

    public static Pallet getPallet() {
        return Pallet.pallet(new Size(600, 500, 300), 50)
                .add(new Cargo<>(new Size(200, 100, 50), 2, 1, 4, Rotation.hlw, new Size(300, 50, 0)))
                .add(new Cargo<>(new Size(200, 100, 50), 1, 1, 1, Rotation.hwl, new Size(0, 50, 100)))
                .add(new Cargo<>(new Size(200, 100, 50), 1, 1, 1, Rotation.lwh, new Size(300, 250, 0)));
    }
}
