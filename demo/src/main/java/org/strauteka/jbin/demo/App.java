package org.strauteka.jbin.demo;

import java.util.HashMap;
import java.util.Map;

import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.core.Dimension;
import org.strauteka.jbin.core.Size;
import org.strauteka.jbin.demo.algorithm.PackerEntry;
import org.strauteka.jbin.draw3d.Draw3d;
import java.awt.Color;

public class App {
    public static void main(String[] args) {
        // Development.draw();
        // Demo.drawRotations();
        Demo.draw();
        final long l = System.currentTimeMillis();

        Map<Dimension, Color> colors = new HashMap<>();
        colors.put(new Size(400, 500, 1000), new Color(255, 0, 0));
        Draw3d.draw(false, false, 1200, 800, colors, PackerEntry.calculate(3000, 3).toArray(new Bin[0]));
        System.out.println("Calc time: " + (System.currentTimeMillis() - l));
    }
}
