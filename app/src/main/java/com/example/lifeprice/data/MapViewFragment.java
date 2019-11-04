package com.example.lifeprice.data;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.lifeprice.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapViewFragment extends Fragment {

    private MapView mapView=null;
    public MapViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_map_view, container, false);
        mapView=view.findViewById(R.id.bmap_view);


        BaiduMap baiduMap = mapView.getMap();
        //修改百度地图的初始位置
        LatLng centerPoint =new LatLng(22.2559,113.541112);
        MapStatus mapStatus=new MapStatus.Builder().target(centerPoint).zoom(17).build();
        MapStatusUpdate mapStatusUpdate= MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.setMapStatus(mapStatusUpdate);

        //在地图中心添加标记点
        BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory.fromResource(R.drawable.icon);
        MarkerOptions markerOptions=new MarkerOptions().icon(bitmapDescriptor).position(centerPoint);
        Marker maker=(Marker)baiduMap.addOverlay(markerOptions);

        //添加文字
        OverlayOptions textOption=new TextOptions().bgColor(0xFF00FF).fontSize(50).fontColor(0XFF0000)
                .text("暨南大学珠海校区").rotate(0).position(centerPoint);
        baiduMap.addOverlay(textOption);

        //响应事件
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getContext(),"点击成功！",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }

}
