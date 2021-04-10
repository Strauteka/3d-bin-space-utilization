package org.strauteka.jbin.demo.algorithm;

public class Tuple2<K, V> {
    public final K _1;
    public final V _2;

    public Tuple2(K _1, V _2) {
        this._1 = _1;
        this._2 = _2;
    }

    public static <KK, VV> Tuple2<KK, VV> of(KK key, VV value) {
        return new Tuple2<KK, VV>(key, value);
    }

    @Override
    public String toString() {
        return _1.toString() + " : " + _2.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (obj instanceof Tuple2<?, ?> && ((Tuple2<?, ?>) obj)._1.equals(_1) && ((Tuple2<?, ?>) obj)._2.equals(_2))
            return true;

        return false;
    }

    @Override
    public int hashCode() {
        return _1.hashCode() * _2.hashCode();
    }
}