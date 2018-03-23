package com.xidian.aria.ariamap;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.io.Serializable;

/**
 * Created by lkc on 18-3-23.
 */

public class SerializableBaiduMap implements Serializable {
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
    // todo：构造方法没有写


    public SerializableBaiduMap(WalkingRouteResult mWalkRes, TransitRouteResult mTransitRes, DrivingRouteResult mDriveRes, String city, LatLng center, int zoomLevel) {
        this.mWalkRes = mWalkRes;
        this.mTransitRes = mTransitRes;
        this.mDriveRes = mDriveRes;
        this.city = city;
        this.center = center;
        this.zoomLevel = zoomLevel;
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
}
