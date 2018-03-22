package com.xidian.aria.ariamap;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.xidian.aria.ariamap.overlayutil.BikingRouteOverlay;
import com.xidian.aria.ariamap.overlayutil.DrivingRouteOverlay;
import com.xidian.aria.ariamap.overlayutil.MassTransitRouteOverlay;
import com.xidian.aria.ariamap.overlayutil.TransitRouteOverlay;
import com.xidian.aria.ariamap.overlayutil.WalkingRouteOverlay;

/**
 * Created by lkc on 18-3-22.
 */

public class MyRouteOverlay {
}
// 定制RouteOverly
class MyDrivingRouteOverlay extends DrivingRouteOverlay {

    public MyDrivingRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    @Override
    public BitmapDescriptor getStartMarker() {
        return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
    }

    @Override
    public BitmapDescriptor getTerminalMarker() {
        return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
    }
}

class MyWalkingRouteOverlay extends WalkingRouteOverlay {

    public MyWalkingRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    @Override
    public BitmapDescriptor getStartMarker() {
        return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
    }

    @Override
    public BitmapDescriptor getTerminalMarker() {
        return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
    }
}

class MyTransitRouteOverlay extends TransitRouteOverlay {

    public MyTransitRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    @Override
    public BitmapDescriptor getStartMarker() {
        return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
    }

    @Override
    public BitmapDescriptor getTerminalMarker() {
        return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
    }
}

class MyBikingRouteOverlay extends BikingRouteOverlay {
    public MyBikingRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    @Override
    public BitmapDescriptor getStartMarker() {
        return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
    }

    @Override
    public BitmapDescriptor getTerminalMarker() {
        return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
    }


}

class MyMassTransitRouteOverlay extends MassTransitRouteOverlay {
    public MyMassTransitRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    @Override
    public BitmapDescriptor getStartMarker() {
        return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
    }

    @Override
    public BitmapDescriptor getTerminalMarker() {
        return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
    }
}