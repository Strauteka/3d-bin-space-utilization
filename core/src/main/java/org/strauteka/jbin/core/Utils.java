package org.strauteka.jbin.core;

public class Utils {
    public enum Rotation {

        lhw(0),

        whl(1),

        wlh(2),

        hlw(3),

        hwl(4),

        lwh(5);

        public final int value;

        /**
         * Instantiates a new code with the specified code value.
         *
         * @param value the integer value of the code
         */
        private Rotation(final int value) {
            this.value = value;
        }

        /**
         * Converts the specified integer value to a request code.
         *
         * @param value the integer value
         * @return the request code
         */
        public static Rotation valueOf(final int value) {
            switch (value) {
            case 0:
                return lhw;
            case 1:
                return whl;
            case 2:
                return wlh;
            case 3:
                return hlw;
            case 4:
                return hwl;
            default:
                return lwh;
            }
        }
    }

    public static Cargo<? extends Dimension> cargoRotate(Size holder, Rotation rotation,
            Cargo<? extends Dimension> cargo) {
        final Dimension newStack = cargo.stack().rotate(rotation);
        return new Cargo<Dimension>(cargo.cargo(), newStack.l(), newStack.h(), newStack.w(),
                newRotate(rotation, cargo.rotation()), newPosition(holder, cargo, rotation));
    }

    private static Rotation newRotate(Rotation rotate, Rotation cargo) {
        return createRotation(sidePos(rotate), cargo);
    }

    private static Size sidePos(Rotation rotation) {
        return new Size(pos(rotation.name().charAt(0)), pos(rotation.name().charAt(1)), pos(rotation.name().charAt(2)));
    }

    private static int pos(final char a) {
        switch (a) {
        case 'l':
            return 0;
        case 'h':
            return 1;
        default:
            return 2;
        }
    }

    private static Rotation createRotation(Size rotate, Rotation cargo) {
        return Rotation.valueOf(new StringBuilder().append(cargo.name().charAt(rotate.l()))
                .append(cargo.name().charAt(rotate.h())).append(cargo.name().charAt(rotate.w())).toString());
    }

    private static Dimension newPosition(Size holder, Space position, Rotation rotation) {
        final Space s = new Space(position.rotate(rotation), position.position().rotate(rotation));
        final Size pos = sidePos(rotation);
        final int l = pos.l() != 0 && pos.h() == 1 ? holder.l() - s.l__() : s.l_();
        final int h = pos.h() != 1 && pos.w() == 2 ? holder.h() - s.h__() : s.h_();
        final int w = pos.w() != 2 && pos.l() == 0 ? holder.w() - s.w__() : s.w_();
        return new Size(l, h, w);
    }
}
