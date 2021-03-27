package org.strauteka.jbin.core;

import org.strauteka.jbin.core.Utils.Rotation;

public interface Dimension {

    public int l();

    public int h();

    public int w();

    public Dimension rotate(Rotation rotation);

    public long value();
}
