package com.xidian.aria.ariamap.overlays;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.xidian.aria.ariamap.R;
import com.xidian.aria.ariamap.overlayutil.TransitRouteOverlay;

public class MyTransitRouteOverlay extends TransitRouteOverlay {

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
