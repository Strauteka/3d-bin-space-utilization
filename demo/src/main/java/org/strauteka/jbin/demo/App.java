package org.strauteka.jbin.demo;

import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.demo.algorithm.PackerEntry;
import org.strauteka.jbin.draw3d.Draw3d;

public class App {
    public static void main(String[] args) {
        Development.draw();
        Demo.draw();
        Draw3d.draw(PackerEntry.calculate(2000, 2).toArray(new Bin[0]));
    }
}
