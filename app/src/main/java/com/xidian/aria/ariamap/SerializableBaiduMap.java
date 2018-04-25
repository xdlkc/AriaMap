package com.xidian.aria.ariamap;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.io.Serializable;

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
    private PlanNode stNode;
    private PlanNode enNode;

    public void setStNode(PlanNode stNode) {
        this.stNode = stNode;
    }

    public void setEnNode(PlanNode enNode) {
        this.enNode = enNode;
    }

    public PlanNode getStNode() {

        return stNode;
    }

    public PlanNode getEnNode() {
        return enNode;
    }

    public SerializableBaiduMap(WalkingRouteResult mWalkRes, TransitRouteResult mTransitRes, DrivingRouteResult mDriveRes,
                                String city, LatLng center, int zoomLevel) {
        this.mWalkRes = mWalkRes;
        this.mTransitRes = mTransitRes;
        this.mDriveRes = mDriveRes;
        this.city = city;
        this.center = center;
        this.zoomLevel = zoomLevel;
    }

    public SerializableBaiduMap(BaiduMap baiduMap, WalkingRouteResult mWalkRes, BikingRouteResult mBikeRes, TransitRouteResult mTransitRes,
                                DrivingRouteResult mDriveRes, MassTransitRouteResult mMassRes,
                                String city, LatLng center, int zoomLevel, PlanNode stNode, PlanNode enNode) {
        this.baiduMap = baiduMap;
        this.mWalkRes = mWalkRes;
        this.mBikeRes = mBikeRes;
        this.mTransitRes = mTransitRes;
        this.mDriveRes = mDriveRes;
        this.mMassRes = mMassRes;
        this.city = city;
        this.center = center;
        this.zoomLevel = zoomLevel;
        this.stNode = stNode;
        this.enNode = enNode;
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
