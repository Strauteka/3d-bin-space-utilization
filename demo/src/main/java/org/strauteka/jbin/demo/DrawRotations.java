package org.strauteka.jbin.demo;

import org.strauteka.jbin.core.Dimension;
import org.strauteka.jbin.core.*;
import org.strauteka.jbin.demo.algorithm.Container;
import org.strauteka.jbin.draw3d.Draw3d;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class DrawRotations {
    public static void main(String[] args) {
        final Container bin = new Container(500, 500, 500);
        final Size s = new Size(150, 300, 500);
        final Size pos = new Size(0, 0, 0);

        Map<Dimension, Color> colors = new HashMap<>();
        colors.put(s, new Color(100, 100, 100));

        Draw3d.draw(true, false, 1200, 800, colors,
                Stream.of(
                        bin.add(new Cargo<>(s, Rotation.lhw, pos)),
                        bin.add(new Cargo<>(s, Rotation.whl, pos)),
                        bin.add(new Cargo<>(s, Rotation.wlh, pos)),
                        bin.add(new Cargo<>(s, Rotation.hlw, pos)),
                        bin.add(new Cargo<>(s, Rotation.hwl, pos)),
                        bin.add(new Cargo<>(s, Rotation.lwh, pos))
                ).toArray(Bin[]::new));
    }
}
