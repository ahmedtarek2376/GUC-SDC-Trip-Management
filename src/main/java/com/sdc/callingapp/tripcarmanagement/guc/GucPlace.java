package com.sdc.callingapp.tripcarmanagement.guc;

import com.sdc.callingapp.tripcarmanagement.LatLng;

public class GucPlace {
    private String name;
    private LatLng latLng;

    public GucPlace(String name, LatLng latLng){
        this.name = name;
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}