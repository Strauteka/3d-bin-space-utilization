package org.strauteka.jbin.core;

import java.util.ArrayList;
import java.util.List;

import org.strauteka.jbin.core.configuration.StackConfig;

public class Space extends Size {

    private final Size position;

    public Space(int l, int h, int w, Dimension position) {
        super(l, h, w);
        this.position = new Size(position);
    }

    public Space(Dimension size, Dimension position) {
        super(size);
        this.position = new Size(position);
    }

    public Dimension position() {
        return position;
    }

    public int l_() {
        return position.l();
    }

    public int h_() {
        return position.h();
    }

    public int w_() {
        return position.w();
    }

    public int l__() {
        return position.l() + super.l();
    }

    public int h__() {
        return position.h() + super.h();
    }

    public int w__() {
        return position.w() + super.w();
    }

    public Boolean overlay(Space s) {
        return (h_() <= s.h_() && h__() >= s.h__() //
                && w_() <= s.w_() && w__() >= s.w__() //
                && l_() <= s.l_() && l__() >= s.l__());
    }

    public Boolean overlap(Space s) {
        return (h_() < s.h__() && h__() > s.h_() //
                && w_() < s.w__() && w__() > s.w_() //
                && l_() < s.l__() && l__() > s.l_());
    }

    public Size overlapSize(Space s) {
        return new Size(Math.min(l__(), s.l__()) - Math.max(l_(), s.l_()), //
                Math.min(h__(), s.h__()) - Math.max(h_(), s.h_()), //
                Math.min(w__(), s.w__()) - Math.max(w_(), s.w_()));
    }

    public boolean needToExpand(Space s, StackConfig conf) {
        final Size os = overlapSize(s);
        final boolean w = this.w__() == s.w_() && os.l() > 0 && os.h() > 0
                && ((h_() < s.h_() ? w() : s.w()) <= conf.w_() || h_() == s.h_());
        final boolean l = this.l__() == s.l_() && os.w() > 0 && os.h() > 0
                && ((h_() < s.h_() ? l() : s.l()) <= conf.l_() || h_() == s.h_());
        // Merging expanded space. Overlap exact config size
        final boolean wx = os.w() == conf.w() && os.l() > 0 && os.h() > 0 && h_() == s.h_();
        final boolean lx = os.l() == conf.l() && os.w() > 0 && os.h() > 0 && h_() == s.h_();
        return w || l || wx || lx;
    }

    // public boolean neighborLeftOrFront(Space s) {
    // final Size os = overlapSize(s);
    // return (this.w__() == s.w_() && os.l() > 0 || this.l__() == s.l_() && os.w()
    // > 0) && os.h() > 0;
    // }

    // public boolean neighborLeft(Space s) {
    // final Size os = overlapSize(s);
    // return this.w__() == s.w_() && os.l() > 0 && os.h() > 0;
    // }

    // public boolean neighborFront(Space s) {
    // final Size os = overlapSize(s);
    // return this.l__() == s.l_() && os.w() > 0 && os.h() > 0;
    // }

    public List<Space> createSpace(Space cargo, boolean disableTop) {
        final List<Space> collector = new ArrayList<>();
        if (overlap(cargo)) {
            final int top = h__() - cargo.h__();
            final int bottom = cargo.h_() - h_();
            final int right = w__() - cargo.w__();
            final int left = cargo.w_() - w_();
            final int front = l__() - cargo.l__();
            final int rear = cargo.l_() - l_();

            if (left > 0) {
                final Space s = new Space(l(), h(), left, new Size(l_(), h_(), w_()));
                // System.out.println("left:" + s);
                collector.add(s);
            }
            if (right > 0) {
                final Space s = new Space(l(), h(), right, new Size(l_(), h_(), cargo.w__()));
                // System.out.println("right:" + s);
                collector.add(s);
            }
            if (front > 0) {
                final Space s = new Space(front, h(), w(), new Size(cargo.l__(), h_(), w_()));
                // System.out.println("front:" + s);
                collector.add(s);
            }
            if (rear > 0) {
                final Space s = new Space(rear, h(), w(), new Size(l_(), h_(), w_()));
                // System.out.println("rear:" + s);
                collector.add(s);
            }

            if (top > 0 && !disableTop) { // upper
                final Size overlap = overlapSize(cargo);
                final int pointW = Math.max(w_(), cargo.w_());
                final int pointL = Math.max(l_(), cargo.l_());
                final Space s = new Space(overlap.l(), top, overlap.w(), new Size(pointL, cargo.h__(), pointW));
                // System.out.println("top:" + s);
                collector.add(s);
            }
            // careful
            if (bottom > 0) { // under
                final Space s = new Space(l(), bottom, w(), new Size(l_(), h_(), w_()));
                // System.out.println("bottom:" + s);
                collector.add(s);
            }
        }
        return collector;
    }

    public List<Space> combineSpace(Space space, StackConfig conf) {
        final List<Space> collector = new ArrayList<>();
        final Size os = overlapSize(space);
        if (os.w() == 0 && os.l() > 0) {
            final int h_ = Math.max(h_(), space.h_());
            final int h__ = Math.min(h__(), space.h__());
            final Space s = new Space(os.l(), (h__ - h_), expand(h_() - space.h_(), w(), space.w(), conf.w()), new Size(
                    Math.max(l_(), space.l_()), h_, findPosition(h_() - space.h_(), w_(), space.w_(), conf.w())));
            collector.add(s);
        } else if (os.l() == 0 && os.w() > 0) {
            final int h_ = Math.max(h_(), space.h_());
            final int h__ = Math.min(h__(), space.h__());
            final Space s = new Space(expand(h_() - space.h_(), l(), space.l(), conf.l()), (h__ - h_), os.w(), new Size(
                    findPosition(h_() - space.h_(), l_(), space.l_(), conf.l()), h_, Math.max(w_(), space.w_())));
            collector.add(s);
        } else if (os.w() == conf.w() && os.l() > 0) { // Merging expanded space. Overlap exact config size
            final int h_ = Math.max(h_(), space.h_());
            final int h__ = Math.min(h__(), space.h__());
            final Space s = new Space(os.l(), (h__ - h_), w() + space.w() - os.w(),
                    new Size(Math.max(l_(), space.l_()), h_, Math.min(w_(), space.w_())));
            collector.add(s);
        } else if (os.l() == conf.l() && os.w() > 0) { // Merging expanded space. Overlap exact config size
            final int h_ = Math.max(h_(), space.h_());
            final int h__ = Math.min(h__(), space.h__());
            final Space s = new Space(l() + space.l() - os.l(), (h__ - h_), os.w(),
                    new Size(Math.min(l_(), space.l_()), h_, Math.max(w_(), space.w_())));
            collector.add(s);
        }
        return collector;
    }

    private static int findPosition(int h, int pos1, int pos2, int maxExpand) {
        if (h >= 0) {
            return Math.min(pos1, pos2);
        } else {
            return Math.max(pos1, pos2) - Math.min(Math.abs(pos1 - pos2), maxExpand);
        }
    }

    private static int expand(int h, int x1, int x2, int maxExpand) {
        if (h == 0) {
            return x1 + x2;
        } else if (h < 0) {
            return Math.min(x1, maxExpand) + x2;
        } else {
            return Math.min(x2, maxExpand) + x1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Space)) {
            return false;
        } else {
            return super.equals(o) && position.equals(((Space) o).position());
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode() * position.hashCode();
    }

    @Override
    public String toString() {
        return String.format("l: %d; h: %d; w: %d; Positions: %s; l__ %d; h__ %d; w__ %d;", l(), h(), w(),
                position.toString(), l__(), h__(), w__());
    }

}
