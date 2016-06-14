package com.alienleeh.familychat.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.base.BaseActivity;
import com.alienleeh.familychat.utils.ApplicationUtils;
import com.alienleeh.familychat.utils.ToastUtils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.netease.nimlib.sdk.msg.attachment.LocationAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by AlienLeeH on 2016/7/16..Hour:05
 * Email:alienleeh@foxmail.com
 * Description:选取位置Activity
 */
public class MapPickActivity extends BaseActivity{

    private MapView mapView;
    private LocationClient client;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor mCurrentMarker;
    private GeoCoder geoCoder;
    LatLng cacheLocation;
    private boolean onlyView = false;
    private TextView displayAdd;

    @Override
    public void initView() {
        IMMessage info = (IMMessage) getIntent().getSerializableExtra("viewmode");
        if (info == null){
            onlyView = false;
        }else {
            onlyView = true;
        }
        setContentView(R.layout.activity_mappick);
        mapView = (MapView) findViewById(R.id.mapview_pick);
        displayAdd = (TextView) findViewById(R.id.location_address_pick);
        displayAdd.setMaxWidth((int) (ApplicationUtils.getWidth()*0.8));
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                displayAdd.setText("");
                if (reverseGeoCodeResult != null){
                    ReverseGeoCodeResult.AddressComponent component = reverseGeoCodeResult.getAddressDetail();
                    String addr = component.province + component.city+component.district+component.street+component.streetNumber;
                    if (!TextUtils.isEmpty(addr)){
                        displayAdd.setText(addr);
                    }
                }
            }
        });
        if (!onlyView){
            initLocation();
        }else {
            findViewById(R.id.marker_mappick).setVisibility(View.GONE);
            LocationAttachment lo = (LocationAttachment) info.getAttachment();
            LatLng latLng = new LatLng(lo.getLatitude(),lo.getLongitude());
            displayAdd.setText(lo.getAddress());
            buildMarker(latLng);
        }

    }

    private void buildMarker(LatLng latLng) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromView(displayAdd);
        displayAdd.setVisibility(View.GONE);
        OverlayOptions options = new MarkerOptions().position(latLng).icon(icon);
        mBaiduMap.addOverlay(options);

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng);
        builder.zoom(16f);
        MapStatus status = builder.build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        mBaiduMap.animateMapStatus(update);
    }

    private void initLocation() {
        client = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setScanSpan(2000);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setIsNeedLocationDescribe(true);
        option.setIgnoreKillProcess(false);
        client.setLocOption(option);
        client.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation != null){
                    displayAdd.setText(bdLocation.getAddrStr());
                    navigateToLocation(new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude()));
                    client.stop();
                }
            }
        });
        client.start();
    }

    public static void startActivityForResult(Activity activity, int RequestCOde) {
        Intent intent = new Intent(activity,MapPickActivity.class);
        activity.startActivityForResult(intent,RequestCOde);
    }

    @Override
    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){

            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar_editinfo);
            TextView title = (TextView) findViewById(R.id.midtext_actionbar);
            title.setText("位置信息");
            TextView commit = (TextView) findViewById(R.id.commit_editinfo);
            TextView cancel = (TextView) findViewById(R.id.cancel_editinfo);
            if (onlyView){
                commit.setVisibility(View.GONE);
            }else {
                commit.setOnClickListener(this);
            }
            cancel.setOnClickListener(this);
        }
    }

    @Override
    public void initListener() {

    }

    private void navigateToLocation(LatLng lng) {
        mBaiduMap.setMyLocationEnabled(true);
        MapStatus.Builder builder = new MapStatus.Builder();

        cacheLocation = lng;
        builder.target(lng);
        MapStatus status = builder.build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        mBaiduMap.setMapStatus(update);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16f));
        if (!onlyView){
            mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
                @Override
                public void onMapStatusChangeStart(MapStatus mapStatus) {

                }

                @Override
                public void onMapStatusChange(MapStatus mapStatus) {

                }

                @Override
                public void onMapStatusChangeFinish(MapStatus mapStatus) {
                    int height = mapView.getHeight() / 2;
                    int width = ApplicationUtils.getWidth() / 2;
                    cacheLocation = mBaiduMap.getProjection().fromScreenLocation(new Point(width,height));
                    geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(cacheLocation));

                }
            });
        }
    }

    @Override
    public void initData() {

    }

    @Override
    protected void processClick(View view) {
        switch (view.getId()){
            case R.id.commit_editinfo:
                commitPickLocation();
                break;
            case R.id.cancel_editinfo:
                finish();
                break;

        }
    }

    private void commitPickLocation() {
        if (cacheLocation != null){
            Intent data = new Intent();
            data.putExtra("location",cacheLocation);
            data.putExtra("address",displayAdd.getText().toString());
            setResult(RESULT_OK,data);
            finish();
        }else {
            ToastUtils.showToast(this,"还未查询到定位讯息");
        }
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    public static void start(Context context, IMMessage message) {
        Intent intent = new Intent(context,MapPickActivity.class);
        intent.putExtra("viewmode",message);
        context.startActivity(intent);
    }
}
