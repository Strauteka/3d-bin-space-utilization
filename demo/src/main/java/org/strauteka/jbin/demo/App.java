package org.strauteka.jbin.demo;

import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.demo.algorithm.PackerEntry;
import org.strauteka.jbin.draw3d.Draw3d;

public class App {
    public static void main(String[] args) {
        Development.draw();
        Demo.drawRotations();
        Demo.draw();
        Draw3d.draw(false, PackerEntry.calculate(2000, 3).toArray(new Bin[0]));
    }
}
