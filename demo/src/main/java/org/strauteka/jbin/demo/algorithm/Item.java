package org.strauteka.jbin.demo.algorithm;

import org.strauteka.jbin.core.Dimension;
import org.strauteka.jbin.core.Rotation;

public interface Item extends Dimension {

    int qty();

    Rotation[] rotations();

    int priority();
}
