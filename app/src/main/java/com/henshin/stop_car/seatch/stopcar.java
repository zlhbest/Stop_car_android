package com.henshin.stop_car.seatch;

import com.esri.arcgisruntime.geometry.Point;

/**
 * Created by henshin on 2018/3/14.
 */

public class stopcar {
    private String ID;
    private Point point;
    private double distance;
    public stopcar(String id,Point point )
    {
        this.point =point;
        this.ID = id;
    }
    public void setID(String id)
    {
        this.ID = id;
    }
    public String getID()
    {
        return this.ID;
    }
    public void setPoint(Point point)
    {
        this.point = point;
    }
    public Point getPoint()
    {
        return this.point;
    }
    public void setDistance(double distance)
    {
        this.distance = distance;
    }
    public double getDistance()
    {
        return this.distance;
    }
}
