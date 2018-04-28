package com.xidian.aria.ariamap;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;

public class SerializableBaiduMap implements Parcelable {
    private BaiduMap map;
    // 城市
    private String city;
    // 中心点
    private LatLng center;
    private int zoomLevel;
    private LatLng  startPoi;
    private LatLng endPoi;

    public SerializableBaiduMap(BaiduMap map, String city, LatLng center, int zoomLevel, LatLng startPoi, LatLng endPoi) {
        this.map = map;
        this.city = city;
        this.center = center;
        this.zoomLevel = zoomLevel;
        this.startPoi = startPoi;
        this.endPoi = endPoi;
    }

    protected SerializableBaiduMap(Parcel in) {
        city = in.readString();
        center = in.readParcelable(LatLng.class.getClassLoader());
        zoomLevel = in.readInt();
        startPoi = in.readParcelable(LatLng.class.getClassLoader());
        endPoi = in.readParcelable(LatLng.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeParcelable(center, flags);
        dest.writeInt(zoomLevel);
        dest.writeParcelable(startPoi, flags);
        dest.writeParcelable(endPoi, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SerializableBaiduMap> CREATOR = new Creator<SerializableBaiduMap>() {
        @Override
        public SerializableBaiduMap createFromParcel(Parcel in) {
            return new SerializableBaiduMap(in);
        }

        @Override
        public SerializableBaiduMap[] newArray(int size) {
            return new SerializableBaiduMap[size];
        }
    };

    public BaiduMap getMap() {
        return map;
    }

    public void setMap(BaiduMap map) {
        this.map = map;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LatLng getCenter() {
        return center;
    }

    public void setCenter(LatLng center) {
        this.center = center;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public LatLng getStartPoi() {
        return startPoi;
    }

    public void setStartPoi(LatLng startPoi) {
        this.startPoi = startPoi;
    }

    public LatLng getEndPoi() {
        return endPoi;
    }

    public void setEndPoi(LatLng endPoi) {
        this.endPoi = endPoi;
    }
}
