package org.strauteka.jbin.core.configuration;

public class StackConfig {
    private final int w_;
    private final int w;
    private final int l_;
    private final int l;

    public StackConfig(int l, int l_, int w, int w_) {
        this.l = l;
        this.l_ = l_;
        this.w = w;
        this.w_ = w_;
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
}
