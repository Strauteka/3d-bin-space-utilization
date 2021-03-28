package org.strauteka.jbin.core.configuration;

public class StackConfig {
    // lowest space width should be less or equal than this parameter
    private final int w_;
    // expanding max over-stack width parameter
    private final int w;
    // lowest space length should be less or equal than this parameter
    private final int l_;
    // expanding max over-stack length parameter
    private final int l;
    // filters off not usable space, if one of dimensions is less than this
    // parameter. Helps core to work a lot faster.
    private final int minimumSpaceSide;

    public StackConfig(int l, int l_, int w, int w_, int minimumSpaceSide) {
        this.l = l;
        this.l_ = l_;
        this.w = w;
        this.w_ = w_;
        this.minimumSpaceSide = minimumSpaceSide;
    }

    public int l() {
        return this.l;
    }

    public int l_() {
        return this.l_;
    }

    public int w() {
        return this.w;
    }

    public int w_() {
        return this.w_;
    }

    public int minimumSpaceSide() {
        return this.minimumSpaceSide;
    }
}
