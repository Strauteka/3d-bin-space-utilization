package org.strauteka.jbin.demo;

import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.demo.algorithm.PackerEntry;
import org.strauteka.jbin.draw3d.Draw3d;

public class App {
    public static void main(String[] args) {
        Development.draw();
        // Demo.drawRotations();
        Demo.draw();
        final long l = System.currentTimeMillis();
        Draw3d.draw(false, PackerEntry.calculate(1000, 3).toArray(new Bin[0]));
        System.out.println("Calc time: " + (System.currentTimeMillis() - l));
    }
}
