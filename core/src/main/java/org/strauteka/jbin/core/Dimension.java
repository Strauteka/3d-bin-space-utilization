package org.strauteka.jbin.core;

public interface Dimension {

    int l();

    int h();

    int w();

    Dimension rotate(Rotation rotation);

    long value();
}
