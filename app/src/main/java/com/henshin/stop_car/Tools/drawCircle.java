package com.henshin.stop_car.Tools;


import com.esri.arcgisruntime.geometry.Point;

/**
 * Created by henshin on 2018/1/12.
 */

public class drawCircle  {
    private static double EARTH_RADIUS = 6378.137;
    private static double JMTOM = 1/111319.5;
    public static   Point[] getpoint(Point center, double radius) {
        Point[] points = new Point[50];
        double sin;
        double cos;
        double x;
        double y;
        for (double i = 0; i < 50; i++) {
            sin = Math.sin(Math.PI * 2 * i / 50);
            cos = Math.cos(Math.PI * 2 * i / 50);
            x = center.getX() + radius * sin;
            y = center.getY() + radius * cos;
            points[(int) i] = new Point(x, y);
        }
        return points;
    }
    public static double getDistance(Point p1,Point p2) {
        double radLat1 = rad(p1.getY());
        double radLat2 = rad(p2.getY());
        double a = radLat1 - radLat2;
        double b = rad(p1.getX()) - rad(p2.getX());
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s*1000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
    public static double JWtoM(double m)
    {
        return m*JMTOM;
    }
}
