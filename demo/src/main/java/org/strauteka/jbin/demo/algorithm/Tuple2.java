package org.strauteka.jbin.demo.algorithm;

import java.util.Objects;

public class Tuple2<K, V> {
    public final K _1;
    public final V _2;

    public Tuple2(K _1, V _2) {
        this._1 = _1;
        this._2 = _2;
    }

    public static <KK, VV> Tuple2<KK, VV> of(KK key, VV value) {
        return new Tuple2<>(key, value);
    }

    @Override
    public String toString() {
        return "Tuple2{" +
                "_1=" + _1 +
                ", _2=" + _2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;
        return _1.equals(tuple2._1) && _2.equals(tuple2._2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1, _2);
    }
}