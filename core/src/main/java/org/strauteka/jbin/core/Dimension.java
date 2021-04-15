package org.strauteka.jbin.core;

public interface Dimension {

    public int l();

    public int h();

    public int w();

    public Dimension rotate(Rotation rotation);

    public long value();
}
