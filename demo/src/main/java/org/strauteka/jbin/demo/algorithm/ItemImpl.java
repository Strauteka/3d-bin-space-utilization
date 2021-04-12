package org.strauteka.jbin.demo.algorithm;

import org.strauteka.jbin.core.Dimension;
import org.strauteka.jbin.core.Utils.Rotation;

public interface ItemImpl extends Dimension {

    public int qty();

    public Rotation[] rotations();

    public int priority();
}
