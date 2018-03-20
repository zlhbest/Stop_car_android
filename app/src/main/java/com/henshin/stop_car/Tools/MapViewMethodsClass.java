package com.henshin.stop_car.Tools;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.MapView;

/**
 * Created by henshin on 2018/3/11.
 */

public class MapViewMethodsClass {
    public static Envelope GetEnvelopeFromMapPointFunction(MapView mainMapView, android.graphics.Point screenPoint, int screenTolerance) {
        Envelope result = null;

        try {
            Point clickPoint = mainMapView.screenToLocation(screenPoint);
            double mapTolerance = (double)screenTolerance * mainMapView.getUnitsPerDensityIndependentPixel();
            result = new Envelope(clickPoint.getX() - mapTolerance, clickPoint.getY() - mapTolerance, clickPoint.getX() + mapTolerance, clickPoint.getY() + mapTolerance, mainMapView.getMap().getSpatialReference());
        } catch (Exception var7) {
            ;
        }

        return result;
    }
}
