package org.strauteka.jbin.demo.algorithm;

public class Tuple2<K, V> {
    public final K _1;
    public final V _2;

    public Tuple2(K _1, V _2) {
        this._1 = _1;
        this._2 = _2;
    }

    @Override
    public String toString() {
        return _1.toString() + " : " + _2.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (obj instanceof Tuple2<?, ?> && ((Tuple2<?, ?>) obj)._1.equals(_1)
                && ((Tuple2<?, ?>) obj)._2.equals(_2)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return _1.hashCode() * _2.hashCode();
    }
}