package com.xidian.aria.ariamap;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

/**
 * Created by Aria on 2018/3/18.
 */

public class MyOnGetRoutePlanResultListener implements OnGetRoutePlanResultListener {
    BaiduMap mBaiduMap;

    public MyOnGetRoutePlanResultListener(BaiduMap map) {
         mBaiduMap = map;
    }

    /**
     * 步行路线规划结果
     * @param result
     */
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        //获取步行线路规划结果
    }

    /**
     *
     * @param result
     */
    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {
         TransitRouteLine route = result.getRouteLines().get(0);
         System.out.println(route);
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            //未找到结果
//            return;
//        }
//        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
//            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
//            //result.getSuggestAddrInfo()
//            return;
//        }
//        if (result.error == SearchResult.ERRORNO.NO_ERROR) {;
//            TransitRouteLine route = result.getRouteLines().get(0);
//            System.out.println(route);
//            //创建公交路线规划线路覆盖物
//            TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
//            //设置公交路线规划数据
//            overlay.setData(route);
//            //将公交路线规划覆盖物添加到地图中
//            overlay.addToMap();
//            overlay.zoomToSpan();
//        }
    }

    /**
     * 跨城综合公共交通线路规划
     * @param result
     */
    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult result) {

    }

    /**
     * 开车
     * @param drivingRouteResult
     */
    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        System.out.println(drivingRouteResult.getRouteLines());
    }

    /**
     * 室内
     * @param indoorRouteResult
     */
    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    /**
     * 骑车
     * @param bikingRouteResult
     */
    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

}
