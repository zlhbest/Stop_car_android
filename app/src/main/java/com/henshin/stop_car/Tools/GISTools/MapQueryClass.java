package com.henshin.stop_car.Tools.GISTools;

import android.graphics.Point;
import android.util.Log;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.QueryParameters.SpatialRelationship;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.FeatureLayer.SelectionMode;
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.henshin.stop_car.Tools.AttributeMethodsClass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * Created by henshin on 2018/3/11.
 */

public class MapQueryClass {
    protected IQueryResult mainQueryResultReturn;
    protected List<MapQueryResult> mainMapQueryResult;
    private int TotalCount = 0;
    private int LoadedCount = 0;

    /***
     * 通过MapView对象上的鼠标点击，查询MapView对象中的可见图层
     * @param mainMapView
     * @param screenPoint
     * @param screenTolerance
     * @param iQueryResult
     */
    public void Identify(MapView mainMapView,
                         Point screenPoint,
                         int screenTolerance,
                         IQueryResult iQueryResult) {
        mainMapQueryResult = new ArrayList<MapQueryResult>();
        mainQueryResultReturn = iQueryResult;
        final ListenableFuture<List<IdentifyLayerResult>> identifyFuture =
                mainMapView.identifyLayersAsync(screenPoint, screenTolerance, false, 100);
        identifyFuture.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    List<IdentifyLayerResult> identifyLayersResults = identifyFuture.get();
                    for (IdentifyLayerResult identifyLayerResult : identifyLayersResults) {
                        MapQueryResult mapQueryResult = new MapQueryResult();
                        mapQueryResult.featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
                        mapQueryResult.features = new ArrayList<Feature>();
                        for (GeoElement identifiedElement : identifyLayerResult.getElements()) {
                            identifyLayerResult.getLayerContent();
                            if (identifiedElement instanceof Feature) {
                                Feature identifiedFeature = (Feature) identifiedElement;
                                mapQueryResult.features.add(identifiedFeature);
                            }
                        }
                        mainMapQueryResult.add(mapQueryResult);
                    }
                    mainQueryResultReturn.getQuery();
                }
                catch (Exception e) {
                }
            }
        });
    }
    /***
     * 通过Geometry对象，查询一组FeatureLayer对象中的要素
     * @param featureLayers
     * @param geometry
     * @param iQueryResult
     */
    public void Identify(List<FeatureLayer> featureLayers,
                         Geometry geometry,
                         IQueryResult iQueryResult) {
        final List<FeatureLayer> mainFeatureLayer = featureLayers;
        TotalCount = featureLayers.size();
        mainMapQueryResult = new ArrayList<MapQueryResult>();
        mainQueryResultReturn = iQueryResult;
        QueryParameters query = new QueryParameters();
        query.setGeometry(geometry);
        try {
            for (final FeatureLayer featureLayer : featureLayers
                    ) {
                final ListenableFuture<FeatureQueryResult> featureQueryResult
                        = featureLayer.getFeatureTable().queryFeaturesAsync(query);
                featureQueryResult.addDoneListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FeatureQueryResult result = featureQueryResult.get();
                            Iterator<Feature> iterator = result.iterator();
                            MapQueryResult mapQueryResult = new MapQueryResult();
                            mapQueryResult.features = new ArrayList<Feature>();
                            Feature feature;
                            while (iterator.hasNext()) {
                                feature = iterator.next();
                                mapQueryResult.features.add(feature);
                            }
                            mapQueryResult.featureLayer = featureLayer;
                            mainMapQueryResult.add(mapQueryResult);
                            LoadedCount++;
                            if (LoadedCount == TotalCount) {
                                List<MapQueryResult> mainNewMapQueryResult =
                                        new ArrayList<MapQueryResult>();
                                for (int i = 0; i < mainFeatureLayer.size(); i++) {
                                    for (MapQueryResult mapQueryResult1 : mainMapQueryResult
                                            ) {
                                        if (mapQueryResult1.featureLayer.equals(mainFeatureLayer.get(i))) {
                                            if (mapQueryResult1.features.size() > 0) {
                                                mainNewMapQueryResult.add(mapQueryResult1);
                                            }
                                            break;
                                        }
                                    }
                                }
                                mainMapQueryResult = mainNewMapQueryResult;
                                mainQueryResultReturn.getQuery();
                            }
                        } catch (Exception e) {

                        }
                    }
                });
            }

        } catch (Exception e) {

        }
    }
    /***
     * 通过Geometry对象，查询一个FeatureLayer对象中的要素
     * @param featureLayer
     * @param geometry
     * @param iQueryResult
     */
    public void Identify(FeatureLayer featureLayer,
                         Geometry geometry,
                         IQueryResult iQueryResult) {
        try {
            final FeatureLayer mainFeatureLayer = featureLayer;
            mainMapQueryResult = new ArrayList<MapQueryResult>();
            mainQueryResultReturn = iQueryResult;
            QueryParameters query = new QueryParameters();
            query.setGeometry(geometry);
            if(featureLayer.getFeatureTable().getGeometryType() == GeometryType.POLYGON)
            {
                query.setSpatialRelationship(QueryParameters.SpatialRelationship.INTERSECTS);
            }
            final ListenableFuture<FeatureQueryResult> featureQueryResult
                    = featureLayer.getFeatureTable().queryFeaturesAsync(query);
            featureQueryResult.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        FeatureQueryResult result = featureQueryResult.get();
                        Iterator<Feature> iterator = result.iterator();
                        MapQueryResult mapQueryResult = new MapQueryResult();
                        mapQueryResult.features = new ArrayList<Feature>();
                        Feature feature;
                        while (iterator.hasNext()) {
                            feature = iterator.next();
                            mapQueryResult.features.add(feature);
                        }
                        mapQueryResult.featureLayer = mainFeatureLayer;
                        mainMapQueryResult.add(mapQueryResult);
                        mainQueryResultReturn.getQuery();
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    /***
     * 通过Geometry对象，选择一个FeatureLayer对象中的要素
     * @param featureLayer
     * @param geometry
     * @param iQueryResult
     */
    public void Select(FeatureLayer featureLayer,
                       Geometry geometry,
                       IQueryResult iQueryResult) {
        try {
            final FeatureLayer mainFeatureLayer = featureLayer;
            mainMapQueryResult = new ArrayList<MapQueryResult>();
            mainQueryResultReturn = iQueryResult;
            QueryParameters query = new QueryParameters();
            query.setGeometry(geometry);
            if(featureLayer.getFeatureTable().getGeometryType() == GeometryType.POLYGON
                    || featureLayer.getFeatureTable().getGeometryType() == GeometryType.POLYLINE)
            {
                query.setSpatialRelationship(QueryParameters.SpatialRelationship.INTERSECTS);
            }
            final ListenableFuture<FeatureQueryResult> featureQueryResult
                    = featureLayer.selectFeaturesAsync(query, FeatureLayer.SelectionMode.NEW);
            featureQueryResult.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        mainFeatureLayer.clearSelection();
                        FeatureQueryResult result = featureQueryResult.get();
                        Iterator<Feature> iterator = result.iterator();
                        MapQueryResult mapQueryResult = new MapQueryResult();
                        mapQueryResult.features = new ArrayList<Feature>();
                        Feature feature;
                        while (iterator.hasNext()) {
                            feature = iterator.next();
                            mapQueryResult.features.add(feature);
                        }
                        mapQueryResult.featureLayer = mainFeatureLayer;
                        mainMapQueryResult.add(mapQueryResult);
                        mainQueryResultReturn.getQuery();
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    /***
     * 通过Geometry对象，选择一组FeatureLayer对象中的要素
     * @param featureLayers
     * @param geometry
     * @param iQueryResult
     */
    public void Select(List<FeatureLayer> featureLayers,
                       Geometry geometry,
                       IQueryResult iQueryResult) {
        final List<FeatureLayer> mainFeatureLayer = featureLayers;
        TotalCount = featureLayers.size();
        mainMapQueryResult = new ArrayList<MapQueryResult>();
        mainQueryResultReturn = iQueryResult;
        QueryParameters query = new QueryParameters();
        query.setGeometry(geometry);
        try {
            for (final FeatureLayer featureLayer : featureLayers
                    ) {
                final ListenableFuture<FeatureQueryResult> featureQueryResult
                        = featureLayer.selectFeaturesAsync(query, FeatureLayer.SelectionMode.NEW);
                featureQueryResult.addDoneListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            featureLayer.clearSelection();
                            FeatureQueryResult result = featureQueryResult.get();
                            Iterator<Feature> iterator = result.iterator();
                            MapQueryResult mapQueryResult = new MapQueryResult();
                            mapQueryResult.features = new ArrayList<Feature>();
                            Feature feature;
                            while (iterator.hasNext()) {
                                feature = iterator.next();
                                mapQueryResult.features.add(feature);
                            }
                            mapQueryResult.featureLayer = featureLayer;
                            mainMapQueryResult.add(mapQueryResult);
                            LoadedCount++;
                            if (LoadedCount == TotalCount) {
                                List<MapQueryResult> mainNewMapQueryResult =
                                        new ArrayList<MapQueryResult>();
                                for (int i = 0; i < mainFeatureLayer.size(); i++) {
                                    for (MapQueryResult mapQueryResult1 : mainMapQueryResult
                                            ) {
                                        if (mapQueryResult1.featureLayer.equals(mainFeatureLayer.get(i))) {
                                            if (mapQueryResult1.features.size() > 0) {
                                                mainNewMapQueryResult.add(mapQueryResult1);
                                            }
                                            break;
                                        }
                                    }
                                }
                                mainMapQueryResult = mainNewMapQueryResult;
                                mainQueryResultReturn.getQuery();
                            }
                        } catch (Exception e) {

                        }
                    }
                });
            }

        } catch (Exception e) {

        }
    }
    /***
     * 通过输入的文本（数字），查询一个FeatureLayer对象中的要素
     * @param featureLayer
     * @param search
     * @param iQueryResult
     */
    public void Search(FeatureLayer featureLayer,
                       String search,
                       IQueryResult iQueryResult){
        Search(featureLayer,search,iQueryResult,null);
    }

    /***
     * 通过输入的文本（数字），查询一个FeatureLayer对象中指定范围内的要素
     * @param featureLayer
     * @param search
     * @param iQueryResult
     * @param geometry
     */
    public void Search(FeatureLayer featureLayer,
                       String search,
                       IQueryResult iQueryResult,
                       Geometry geometry) {
        final FeatureLayer mainFeatureLayer = featureLayer;
        mainMapQueryResult = new ArrayList<MapQueryResult>();
        mainQueryResultReturn = iQueryResult;
        QueryParameters query = new QueryParameters();
        String whereStr = GetWhereStrFunction(featureLayer,search);
        query.setWhereClause(whereStr);
        if(geometry != null)
        {
            query.setGeometry(geometry);
        }
        final ListenableFuture<FeatureQueryResult> featureQueryResult
                = featureLayer.getFeatureTable().queryFeaturesAsync(query);
        featureQueryResult.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult result = featureQueryResult.get();
                    Iterator<Feature> iterator = result.iterator();
                    MapQueryResult mapQueryResult = new MapQueryResult();
                    mapQueryResult.features = new ArrayList<Feature>();
                    Feature feature;
                    while (iterator.hasNext()) {
                        feature = iterator.next();
                        mapQueryResult.features.add(feature);
                    }
                    mapQueryResult.featureLayer = mainFeatureLayer;
                    mainMapQueryResult.add(mapQueryResult);
                    mainQueryResultReturn.getQuery();
                } catch (Exception e) {
                    String message = e.getMessage();
                    Log.i("",e.getMessage());
                }
            }
        });
    }
    /***
     * 通过输入的文本（数字），查询一组FeatureLayer对象中的要素
     * @param featureLayers
     * @param search
     * @param iQueryResult
     */
    public void Search(List<FeatureLayer> featureLayers,
                       String search,
                       IQueryResult iQueryResult){
        Search(featureLayers,search,iQueryResult,null);
    }
    /***
     * 通过输入的文本（数字），查询一组FeatureLayer对象中指定范围内的要素
     * @param featureLayers
     * @param search
     * @param iQueryResult
     * @param geometry
     */
    public void Search(List<FeatureLayer> featureLayers,
                       String search,
                       IQueryResult iQueryResult,
                       Geometry geometry) {
        final List<FeatureLayer> mainFeatureLayer = featureLayers;
        TotalCount = featureLayers.size();
        mainMapQueryResult = new ArrayList<MapQueryResult>();
        mainQueryResultReturn = iQueryResult;

        try {
            for (final FeatureLayer featureLayer : featureLayers
                    ) {
                QueryParameters query = new QueryParameters();
                String whereStr = GetWhereStrFunction(featureLayer,search);
                query.setWhereClause(whereStr);
                if(geometry != null)
                {
                    query.setGeometry(geometry);
                }
                final ListenableFuture<FeatureQueryResult> featureQueryResult
                        = featureLayer.getFeatureTable().queryFeaturesAsync(query);
                featureQueryResult.addDoneListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FeatureQueryResult result = featureQueryResult.get();
                            Iterator<Feature> iterator = result.iterator();
                            MapQueryResult mapQueryResult = new MapQueryResult();
                            mapQueryResult.features = new ArrayList<Feature>();
                            Feature feature;
                            while (iterator.hasNext()) {
                                feature = iterator.next();
                                mapQueryResult.features.add(feature);
                            }
                            mapQueryResult.featureLayer = featureLayer;
                            mainMapQueryResult.add(mapQueryResult);
                            LoadedCount++;
                            if (LoadedCount == TotalCount) {
                                List<MapQueryResult> mainNewMapQueryResult =
                                        new ArrayList<MapQueryResult>();
                                for (int i = 0; i < mainFeatureLayer.size(); i++) {
                                    for (MapQueryResult mapQueryResult1 : mainMapQueryResult
                                            ) {
                                        if (mapQueryResult1.featureLayer.equals(mainFeatureLayer.get(i))) {
                                            if (mapQueryResult1.features.size() > 0) {
                                                mainNewMapQueryResult.add(mapQueryResult1);
                                            }
                                            break;
                                        }
                                    }
                                }
                                mainMapQueryResult = mainNewMapQueryResult;
                                mainQueryResultReturn.getQuery();
                            }
                        } catch (Exception e) {

                        }
                    }
                });
            }

        } catch (Exception e) {

        }
    }
    public void Query(FeatureLayer featureLayer, String where, IQueryResult iQueryResult)
    {
        Query(featureLayer, where, iQueryResult, null);
    }

    public void Query(ServiceFeatureTable serviceFeatureTable,FeatureLayer featureLayer, String where, IQueryResult iQueryResult)
    {
        final FeatureLayer mainFeatureLayer = featureLayer;
        this.mainMapQueryResult = new ArrayList();
        this.mainQueryResultReturn = iQueryResult;
        QueryParameters query = new QueryParameters();
        String whereStr = where;
        query.setWhereClause(whereStr);
        final ListenableFuture<FeatureQueryResult> featureQueryResult =
                serviceFeatureTable.queryFeaturesAsync (query, ServiceFeatureTable.QueryFeatureFields.LOAD_ALL);
        featureQueryResult.addDoneListener(new Runnable()
        {
            public void run()
            {
                try
                {
                    FeatureQueryResult result = (FeatureQueryResult)featureQueryResult.get();
                    Iterator<Feature> iterator = result.iterator();
                    MapQueryClass.MapQueryResult mapQueryResult = new MapQueryClass.MapQueryResult();
                    mapQueryResult.features = new ArrayList();
                    while (iterator.hasNext())
                    {
                        Feature feature = (Feature)iterator.next();
                        mapQueryResult.features.add(feature);
                    }
                    mapQueryResult.featureLayer = mainFeatureLayer;
                    MapQueryClass.this.mainMapQueryResult.add(mapQueryResult);
                    MapQueryClass.this.mainQueryResultReturn.getQuery();
                }
                catch (Exception localException) {}
            }
        });
    }
    public void Query(FeatureLayer featureLayer, String where, IQueryResult iQueryResult, Geometry geometry)
    {
        final FeatureLayer mainFeatureLayer = featureLayer;
        this.mainMapQueryResult = new ArrayList();
        this.mainQueryResultReturn = iQueryResult;
        QueryParameters query = new QueryParameters();
        String whereStr = where;
        query.setWhereClause(whereStr);
        if (geometry != null) {
            query.setGeometry(geometry);
        }
        final ListenableFuture<FeatureQueryResult> featureQueryResult =
                featureLayer.getFeatureTable().queryFeaturesAsync (query);
        featureQueryResult.addDoneListener(new Runnable()
        {
            public void run()
            {
                try
                {
                    FeatureQueryResult result = (FeatureQueryResult)featureQueryResult.get();
                    Iterator<Feature> iterator = result.iterator();
                    MapQueryClass.MapQueryResult mapQueryResult = new MapQueryClass.MapQueryResult();
                    mapQueryResult.features = new ArrayList();
                    while (iterator.hasNext())
                    {
                        Feature feature = (Feature)iterator.next();
                        mapQueryResult.features.add(feature);
                    }
                    mapQueryResult.featureLayer = mainFeatureLayer;
                    MapQueryClass.this.mainMapQueryResult.add(mapQueryResult);
                    MapQueryClass.this.mainQueryResultReturn.getQuery();
                }
                catch (Exception localException) {}
            }
        });
    }

    public static String GetWhereStrFunction(FeatureLayer featureLayer, String search)
    {
        StringBuilder stringBuilder = new StringBuilder();
        List<Field> fields = featureLayer.getFeatureTable().getFields();
        boolean isNumber = AttributeMethodsClass.isNumberFunction(search);
        for (Field field : fields) {
            switch (field.getFieldType())
            {
                case TEXT:
                    stringBuilder.append(" upper(");
                    stringBuilder.append(field.getName());
                    stringBuilder.append(") LIKE '%");
                    stringBuilder.append(search.toUpperCase());
                    stringBuilder.append("%' or");
                    break;
                case SHORT:
                case INTEGER:
                case FLOAT:
                case DOUBLE:
                case OID:
                    if (isNumber == true)
                    {
                        stringBuilder.append(field.getName());
                        stringBuilder.append("=");
                        stringBuilder.append(search);
                        stringBuilder.append("or");
                    }
                    break;
            }
        }
        String result = stringBuilder.toString();
        return result.substring(0, result.length() - 2);
    }
    public List<MapQueryResult> getMapQueryResult()
    {
        return this.mainMapQueryResult;
    }
    public static class MapQueryResult
    {
        public FeatureLayer featureLayer;
        public List<Feature> features;
        public String labelField;

        public void selectFeature(Feature feature)
        {
            this.featureLayer.clearSelection();
            this.featureLayer.setSelectionColor(-2534577);
            this.featureLayer.setSelectionWidth(3.0D);
            this.featureLayer.selectFeatures(this.features);
        }

        public void setFeatureLayerSelected(boolean selected)
        {
            if (selected == true)
            {
                this.featureLayer.clearSelection();
                this.featureLayer.setSelectionColor(-2534577);
                this.featureLayer.setSelectionWidth(3.0D);
                this.featureLayer.selectFeatures(this.features);
            }
            else
            {
                this.featureLayer.clearSelection();
            }
        }

        public String getTitle(int index)
        {
            String result = "";
            try
            {
                if ((this.labelField != null) && (this.labelField != "")) {
                    result = ((Feature)this.features.get(index)).getAttributes().get(this.labelField).toString();
                }
            }
            catch (Exception localException) {}
            return result;
        }

        public String getLayerTitle()
        {
            String result = "";
            try
            {
                result = this.featureLayer.getName();
            }
            catch (Exception localException) {}
            return result;
        }

        public int getFeatureCount()
        {
            int result = 0;
            try
            {
                result = this.features.size();
            }
            catch (Exception localException) {}
            return result;
        }
    }
}
