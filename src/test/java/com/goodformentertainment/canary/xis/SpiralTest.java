package com.goodformentertainment.canary.xis;

import org.junit.Test;

import java.awt.*;

public class SpiralTest {
    @Test
    public void test() {
        System.out.println("-1: " + spiral(-1));
        System.out.println("0: " + spiral(0));
        System.out.println("1: " + spiral(1));
        System.out.println("2: " + spiral(2));
        System.out.println("3: " + spiral(3));
        System.out.println("4: " + spiral(4));
        System.out.println("5: " + spiral(5));
        System.out.println("6: " + spiral(6));
        System.out.println("7: " + spiral(7));
        System.out.println("8: " + spiral(8));
        System.out.println("9: " + spiral(9));
        System.out.println("10: " + spiral(10));
        System.out.println("11: " + spiral(11));
        System.out.println("12: " + spiral(12));
        System.out.println("13: " + spiral(13));
        System.out.println("14: " + spiral(14));
        System.out.println("15: " + spiral(15));
        System.out.println("16: " + spiral(16));
        System.out.println("17: " + spiral(17));
        System.out.println("18: " + spiral(18));
        System.out.println("19: " + spiral(19));
        System.out.println("20: " + spiral(20));
        System.out.println("21: " + spiral(21));
        System.out.println("22: " + spiral(22));
        System.out.println("23: " + spiral(23));
    }

    private Point spiral(final int n) {
        final Point pos = new Point(0, 0);

        // given n an index in the squared spiral
        // p the sum of point in inner square
        // a the position on the current square
        // n = p + a

        double r = Math.floor((Math.sqrt(n + 1) - 1) / 2) + 1;

        // compute radius : inverse arithmetic sum of 8+16+24+...=
        double p = (8 * r * (r - 1)) / 2;
        // compute total point on radius -1 : arithmetic sum of 8+16+24+...

        double en = r * 2;
        // points by face

        double a = (1 + n - p) % (r * 8);
        // compute de position and shift it so the first is (-r,-r) but (-r+1,-r)
        // so square can connect

        switch ((int) Math.floor(a / (r * 2))) {
            // find the face : 0 top, 1 right, 2, bottom, 3 left
            case 0:
                pos.setLocation(a - r, -r);
                break;
            case 1:
                pos.setLocation(r, (a % en) - r);
                break;
            case 2:
                pos.setLocation(r - (a % en), r);
                break;
            case 3:
                pos.setLocation(-r, r - (a % en));
                break;
        }

        // System.out.println("n : " + n + " r : " + r + " p : " + p + " a : " + a + "  -->  " + pos);
        return pos;
    }
}
