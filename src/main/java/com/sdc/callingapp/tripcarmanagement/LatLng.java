package com.sdc.callingapp.tripcarmanagement;

public final class LatLng{
    public double latitude;
    public double longitude;
    
    public LatLng() {
	}

    public LatLng(double latitude, double longitude) {
        if (-180.0D <= longitude && longitude < 180.0D) {
            this.longitude = longitude;
        } else {
            this.longitude = ((longitude - 180.0D) % 360.0D + 360.0D) % 360.0D - 180.0D;
        }

        this.latitude = Math.max(-90.0D, Math.min(90.0D, latitude));
    }

    public final boolean equals(Object var1) {
        if (this == var1) {
            return true;
        } else if (!(var1 instanceof LatLng)) {
            return false;
        } else {
            LatLng var2 = (LatLng)var1;
            return Double.doubleToLongBits(this.latitude) == Double.doubleToLongBits(var2.latitude) && Double.doubleToLongBits(this.longitude) == Double.doubleToLongBits(var2.longitude);
        }
    }

    public final String toString() {
        double var1 = this.latitude;
        double var3 = this.longitude;
        return (new StringBuilder(60)).append("lat/lng: (").append(var1).append(",").append(var3).append(")").toString();
    }
}
