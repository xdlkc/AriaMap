package com.xidian.aria.ariamap.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.mapapi.model.LatLng;

public class ParcelableNearBuildingData implements Parcelable{
    private LatLng centerPoi;

    protected ParcelableNearBuildingData(Parcel in) {
        centerPoi = in.readParcelable(LatLng.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(centerPoi, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelableNearBuildingData> CREATOR = new Creator<ParcelableNearBuildingData>() {
        @Override
        public ParcelableNearBuildingData createFromParcel(Parcel in) {
            return new ParcelableNearBuildingData(in);
        }

        @Override
        public ParcelableNearBuildingData[] newArray(int size) {
            return new ParcelableNearBuildingData[size];
        }
    };
}
