package com.airbnb.android.react.maps;

import android.content.Context;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AirMapPolyline extends AirMapFeature {

    private static final float PATTERN_GAP_LENGTH_PX = 20;

    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    private PolylineOptions polylineOptions;
    private Polyline polyline;

    private List<LatLng> coordinates;
    private int color;
    private float width;
    private boolean geodesic;
    private float zIndex;
    private Cap lineCap = new RoundCap();

    public AirMapPolyline(Context context) {
        super(context);
    }

    public void setCoordinates(ReadableArray coordinates) {
        this.coordinates = new ArrayList<>(coordinates.size());
        for (int i = 0; i < coordinates.size(); i++) {
            ReadableMap coordinate = coordinates.getMap(i);
            this.coordinates.add(i,
                    new LatLng(coordinate.getDouble("latitude"), coordinate.getDouble("longitude")));
        }
        if (polyline != null) {
            polyline.setPoints(this.coordinates);
        }
    }

    public void setColor(int color) {
        this.color = color;
        if (polyline != null) {
            polyline.setColor(color);
        }
    }

    public void setWidth(float width) {
        this.width = width;
        if (polyline != null) {
            polyline.setWidth(width);
        }
    }

    public void setZIndex(float zIndex) {
        this.zIndex = zIndex;
        if (polyline != null) {
            polyline.setZIndex(zIndex);
        }
    }

    public void setGeodesic(boolean geodesic) {
        this.geodesic = geodesic;
        if (polyline != null) {
            polyline.setGeodesic(geodesic);
        }
    }

    public void setLineCap(Cap cap) {
        this.lineCap = cap;
        if (polyline != null) {
            polyline.setStartCap(cap);
            polyline.setEndCap(cap);
        }
    }

    public void setDashed(boolean dashed) {
        if (polyline != null) {
            if (dashed) {
                List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);
                polyline.setPattern(PATTERN_POLYLINE_DOTTED);
            }
        }
    }

    public PolylineOptions getPolylineOptions() {
        if (polylineOptions == null) {
            polylineOptions = createPolylineOptions();
        }
        return polylineOptions;
    }

    private PolylineOptions createPolylineOptions() {
        PolylineOptions options = new PolylineOptions();
        options.addAll(coordinates);
        options.color(color);
        options.width(width);
        options.geodesic(geodesic);
        options.zIndex(zIndex);
        options.startCap(lineCap);
        options.endCap(lineCap);
        return options;
    }

    @Override
    public Object getFeature() {
        return polyline;
    }

    @Override
    public void addToMap(GoogleMap map) {
        polyline = map.addPolyline(getPolylineOptions());
        polyline.setClickable(true);
    }

    @Override
    public void removeFromMap(GoogleMap map) {
        polyline.remove();
    }
}
