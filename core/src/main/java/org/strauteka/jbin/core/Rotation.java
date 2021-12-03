package org.strauteka.jbin.core;

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
    Rotation(final int value) {
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
