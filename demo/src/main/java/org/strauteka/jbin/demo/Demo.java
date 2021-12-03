package org.strauteka.jbin.demo;

import org.strauteka.jbin.core.*;
import org.strauteka.jbin.demo.algorithm.Container;
import org.strauteka.jbin.demo.algorithm.Pallet;
import org.strauteka.jbin.draw3d.Draw3d;
import java.util.stream.Stream;

public class Demo {

    public static void main(String[] args) {
        Draw3d.draw(
                Stream.of(
                        test3(),
                        test4(),
                        test5(),
                        test6(),
                        test7(),
                        test8()
                ).toArray(Bin[]::new)
        );
    }


    public static Container test3() {
        return new Container(1000, 1000, 1000)//
                .add(new Cargo<>(new Size(200, 200, 200), new Size(0, 0, 0)))
                .add(new Cargo<>(new Size(300, 200, 500), new Size(0, 0, 200)))
                .add(new Cargo<>(new Size(500, 300, 200), new Size(200, 0, 0)))
                .add(new Cargo<>(new Size(300, 200, 300), new Size(300, 0, 200)))
                .add(new Cargo<>(new Size(600, 200, 300), new Size(200, 0, 700)))
                .add(new Cargo<>(new Size(300, 200, 200), new Size(300, 0, 500)));
    }

    public static Container test4() {
        return new Container(1000, 1000, 1000)
                .add(new Cargo<>(new Size(700, 200, 200), new Size(0, 0, 0)))
                .add(new Cargo<>(new Size(150, 200, 500), new Size(0, 800, 300)))
                .add(new Cargo<>(new Size(300, 200, 200), new Size(0, 0, 200)));
    }

    public static Container test5() {
        return new Container(1000, 1000, 1000)
                .add(new Cargo<>(new Size(100, 55, 50), 4, 8, 3, Rotation.whl, new Size(0, 0, 0)))
                .add(new Cargo<>(new Size(100, 45, 50), 5, 5, 3, Rotation.whl, new Size(0, 0, 500)));
    }

    public static Container test6() {
        return new Container(1000, 1000, 1000)
                .add(new Cargo<>(new Size(100, 50, 50), 4, 8, 2, new Size(300, 300, 300)))
                .add(new Cargo<>(new Size(100, 50, 50), 4, 8, 4, new Size(300, 300, 400)));
    }

    public static Container test7() {
        final Pallet pallet = Pallet.pallet(new Size(600, 500, 300), 50)
                .add(new Cargo<>(new Size(100, 50, 50), 4, 4, 2, Rotation.wlh, new Size(300, 50, 0)))
                .add(new Cargo<>(new Size(100, 50, 50), 4, 4, 3, Rotation.wlh, new Size(100, 50, 100)));
        return new Container(new Size(1000, 1000, 1000))
                .add(new Cargo<>(pallet, new Size(400, 0, 0)))
                .add(new Cargo<>(pallet, Rotation.whl, new Size(0, 0, 0)));
    }

    public static Container test8() {
        return new Container(1000, 1000, 1000)
                .add(new Cargo<>(new Size(300, 200, 400), new Size(150, 0, 150)))
                .add(new Cargo<>(new Size(200, 200, 300), new Size(200, 0, 550)))
                .add(new Cargo<>(new Size(200, 200, 200), new Size(450, 0, 150)));
    }
}
