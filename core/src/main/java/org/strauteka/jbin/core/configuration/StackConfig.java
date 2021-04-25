package org.strauteka.jbin.core.configuration;

/**
 * All number parameters has to be positive. Configuration helps algorithm to
 * work better by extending space based on near bellow narrow spaces. w and w_
 * means that core can expand space(overhanging) till plus w size, if lower
 * narrow space is less than w_(usually, so narrow that can't be used). You can
 * write one algorithm principle and by changing this configuration get
 * different results, because it will generate spaces differently.
 */
public class StackConfig {
    // lowest space width should be less or equal than this parameter
    private final int w_;
    // expanding max over-stack width parameter
    private final int w;
    // lowest space length should be less or equal than this parameter
    private final int l_;
    // expanding max over-stack length parameter
    private final int l;
    // while merging empty spaces, if using detailed height dimension, assumes that
    // one space height 500 and second space height 495 is equal for merge, if
    // parameter can compensate shortage. !!Use wisely!! If your algorithm not
    // checking near space heights, leave it to 0, rarely to occur!.
    private final int equalizeH;
    // filters off not usable space (executes after merge), if one of dimensions is
    // less than this parameter. Core overlap and merging spaces performance is ~
    // n^2 type. So more items means drastically worse performance. Helps core to
    // work a lot faster.
    private final int minimumSpaceSide;
    // disable merging same height area, including over-stacking
    private final boolean disableMerge;

    public StackConfig(int l, int l_, int w, int w_, int equalizeH, int minimumSpaceSide, boolean disableMerge) {
        this.l = l;
        this.l_ = l_;
        this.w = w;
        this.w_ = w_;
        this.equalizeH = equalizeH;
        this.minimumSpaceSide = minimumSpaceSide;
        this.disableMerge = disableMerge;
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

    public int equalizeH() {
        return this.equalizeH;
    }

    public int minimumSpaceSide() {
        return this.minimumSpaceSide;
    }

    public boolean disableMerge() {
        return this.disableMerge;
    }
}
