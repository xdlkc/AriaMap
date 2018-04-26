package com.xidian.aria.ariamap;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.io.Serializable;

public class SerializableBaiduMap implements Parcelable {
    private BaiduMap baiduMap;
    // 步行线路检索结果
    private WalkingRouteResult mWalkRes = null;
    // 自行车线路检索结果
    private BikingRouteResult mBikeRes = null;
    // 公交线路检索结果
    private TransitRouteResult mTransitRes = null;
    // 驾车线路检索结果
    private DrivingRouteResult mDriveRes = null;
    // 跨城公交线路检索结果
    private MassTransitRouteResult mMassRes = null;
    // 城市
    private String city;
    // 中心点
    private LatLng center;
    private int zoomLevel;
    private LatLng  startPoi;
    private LatLng endPoi;

    public SerializableBaiduMap(BaiduMap baiduMap, WalkingRouteResult mWalkRes, BikingRouteResult mBikeRes, TransitRouteResult mTransitRes, DrivingRouteResult mDriveRes, MassTransitRouteResult mMassRes, String city, LatLng center, int zoomLevel, LatLng startPoi, LatLng endPoi) {
        this.baiduMap = baiduMap;
        this.mWalkRes = mWalkRes;
        this.mBikeRes = mBikeRes;
        this.mTransitRes = mTransitRes;
        this.mDriveRes = mDriveRes;
        this.mMassRes = mMassRes;
        this.city = city;
        this.center = center;
        this.zoomLevel = zoomLevel;
        this.startPoi = startPoi;
        this.endPoi = endPoi;
    }

    protected SerializableBaiduMap(Parcel in) {
        mWalkRes = in.readParcelable(WalkingRouteResult.class.getClassLoader());
        mBikeRes = in.readParcelable(BikingRouteResult.class.getClassLoader());
        mTransitRes = in.readParcelable(TransitRouteResult.class.getClassLoader());
        mDriveRes = in.readParcelable(DrivingRouteResult.class.getClassLoader());
        mMassRes = in.readParcelable(MassTransitRouteResult.class.getClassLoader());
        city = in.readString();
        center = in.readParcelable(LatLng.class.getClassLoader());
        zoomLevel = in.readInt();
        startPoi = in.readParcelable(LatLng.class.getClassLoader());
        endPoi = in.readParcelable(LatLng.class.getClassLoader());
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

    public void setBaiduMap(BaiduMap baiduMap) {

        this.baiduMap = baiduMap;
    }

    public BaiduMap getBaiduMap() {

        return baiduMap;
    }

    public WalkingRouteResult getmWalkRes() {
        return mWalkRes;
    }

    public void setmWalkRes(WalkingRouteResult mWalkRes) {
        this.mWalkRes = mWalkRes;
    }

    public BikingRouteResult getmBikeRes() {
        return mBikeRes;
    }

    public void setmBikeRes(BikingRouteResult mBikeRes) {
        this.mBikeRes = mBikeRes;
    }

    public TransitRouteResult getmTransitRes() {
        return mTransitRes;
    }

    public void setmTransitRes(TransitRouteResult mTransitRes) {
        this.mTransitRes = mTransitRes;
    }

    public DrivingRouteResult getmDriveRes() {
        return mDriveRes;
    }

    public void setmDriveRes(DrivingRouteResult mDriveRes) {
        this.mDriveRes = mDriveRes;
    }

    public MassTransitRouteResult getmMassRes() {
        return mMassRes;
    }

    public void setmMassRes(MassTransitRouteResult mMassRes) {
        this.mMassRes = mMassRes;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(mWalkRes, flags);
        dest.writeParcelable(mBikeRes, flags);
        dest.writeParcelable(mTransitRes, flags);
        dest.writeParcelable(mDriveRes, flags);
        dest.writeParcelable(mMassRes, flags);
        dest.writeString(city);
        dest.writeParcelable(center, flags);
        dest.writeInt(zoomLevel);
        dest.writeParcelable(startPoi, flags);
        dest.writeParcelable(endPoi, flags);
    }
}
