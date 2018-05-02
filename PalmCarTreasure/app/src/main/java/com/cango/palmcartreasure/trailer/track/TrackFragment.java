package com.cango.palmcartreasure.trailer.track;


import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.model.CarTrackQuery;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.ScreenUtil;
import com.cango.palmcartreasure.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class TrackFragment extends BaseFragment implements TrackContract.View {


    public final static String IMEI = "imei";
    public final static String START_TIME = "start_time";
    public final static String END_TIME = "end_time";
    @BindView(R.id.toolbar_trailer_map)
    Toolbar mToolbar;
    @BindView(R.id.fab_start)
    ImageView fabStart;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    @BindView(R.id.fl_nodata)
    FrameLayout flNoData;
    private List<LatLng> points;

    @OnClick({R.id.fab_start})
    public void onClick(View view) {
        switch (view.getId()) {
            //开始或暂定或再次开始
            case R.id.fab_start:
                if (mList != null) {
                    if (isStart) {
                        if (!CommUtil.checkIsNull(smoothMarker)) {
                            LatLng position = smoothMarker.getPosition();
                            LatLng latLng = points.get(points.size() - 1);
                            float distance = AMapUtils.calculateLineDistance(position, latLng);
                            Logger.d(distance);
                            if (distance < 5f) {
                                smoothMarker.destroy();
                                startMove();
                                isStart = true;
                            } else {
                                smoothMarker.stopMove();
                                isStart = false;
                            }
                        }
                    } else {
                        if (smoothMarker != null) {
                            smoothMarker.startSmoothMove();
                            isStart = true;
                        } else {
                            startMove();
                            isStart = true;
                        }
                    }
                }
                break;
        }
    }

    private TrackContract.Presenter mPresenter;
    private TrackActivity mActivity;
    /**
     * 当前的IMEI
     */
    private String mIMEI;
    private Date mStartTime, mEndTime;
    private List<CarTrackQuery.DataBean.TrackListBean> mList;
    //地图相关
    private AMap mMap;
    private Polyline mPolyline;
    private boolean isStart;
    private SmoothMoveMarker smoothMarker;

    public static TrackFragment newInstance(String imei, Date startTime, Date endTime) {
        TrackFragment trajectoryFragment = new TrackFragment();
        Bundle args = new Bundle();
        args.putString(IMEI, imei);
        args.putSerializable(START_TIME, startTime);
        args.putSerializable(END_TIME, endTime);
        trajectoryFragment.setArguments(args);
        return trajectoryFragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_track;
    }

    @Override
    protected void initView() {
        showInfoIndicator(false);
        fabStart.setVisibility(View.GONE);
        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= 21) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbar.setLayoutParams(layoutParams);
            mToolbar.setPadding(0, statusBarHeight, 0, 0);
        }
        mActivity.setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mActivity.onBackPressed();
//            }
//        });
//        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
//        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
//        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        setUpMapIfNeeded();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = df.format(mStartTime);
        String end = df.format(mEndTime);
        mPresenter.carTrackQuery(true, MtApplication.mSPUtils.getInt(Api.USERID), mIMEI, start, end, 5);
    }

    @Override
    protected void initData() {
        mActivity = (TrackActivity) getActivity();
        Bundle arguments = getArguments();
        mIMEI = arguments.getString(IMEI);
        mStartTime = (Date) arguments.getSerializable(START_TIME);
        mEndTime = (Date) arguments.getSerializable(END_TIME);
    }

    @Override
    public void setPresenter(TrackContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showInfoIndicator(boolean active) {
        if (active) {
            mLoadView.smoothToShow();
        } else {
            mLoadView.smoothToHide();
        }
    }

    @Override
    public void showInfoError() {

    }

    @Override
    public void showInfoDataError() {
        ToastUtils.showShort(R.string.data_error);
        flNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInfoSuccess(boolean isSuccess, CarTrackQuery.DataBean dataBean) {
        fabStart.setVisibility(View.VISIBLE);
        mList = dataBean.getTrackList();
        setLine();
    }

    @Override
    public void openOtherUi() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    /**
     * 获取Amap 对象
     */
    private void setUpMapIfNeeded() {
        if (mMap == null) {
            SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fg_map);
            mMap = supportMapFragment.getMap();
            UiSettings uiSettings = mMap.getUiSettings();
            //设置不显示+-符号
//            uiSettings.setZoomControlsEnabled(false);
            uiSettings.setScaleControlsEnabled(true);
            uiSettings.setCompassEnabled(true);
            uiSettings
                    .setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
//            setLocationBluePoint();
//            setCarGPSLocationPoint();
//            setLine();
        }
    }

    /**
     * 设置线
     */
    public void setLine() {
        addPolylineInPlayGround();

        LatLng latLng = new LatLng(mList.get(0).getResultLAT(), mList.get(0).getResultLON());
        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//                .title("title")
                .snippet("起点")
                .position(latLng)
                .visible(true)
                .draggable(true);
        Marker marker = mMap.addMarker(markerOption);
        marker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 14, 0, 0)));

        //循环添加特殊点上的文字描述，将textview转成bitmap
        for (int i = 0; i < mList.size(); i++) {
            CarTrackQuery.DataBean.TrackListBean bean = mList.get(i);
            if (!TextUtils.isEmpty(bean.getStopRemark())) {
                Logger.d("aaa");
                LatLng beanLng = new LatLng(bean.getResultLAT(), bean.getResultLON());
//                TextOptions textOption = new TextOptions()
//                        .position(beanLng)
//                        .text(bean.getTrackTime() + "\r\n" + bean.getStopRemark())
//                        .fontColor(Color.WHITE)
//                        .backgroundColor(getResources().getColor(R.color.colorPrimaryDark))
//                        .fontSize(SizeUtil.sp2px(mActivity,12))
//                        .align(Text.ALIGN_CENTER_HORIZONTAL, Text.ALIGN_CENTER_VERTICAL);
//                mMap.addText(textOption);

                TextView textView = new TextView(mActivity);
                textView.setGravity(Gravity.CENTER);
//                        textView.setBackgroundResource(R.drawable.icon_gcoding);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.BLACK);
//                textView.setShadowLayer(3, 0, 0, Color.BLACK);
                textView.setText(bean.getTrackTime() + "\r\n" + bean.getStopRemark());
                textView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());

//                textView.setDrawingCacheEnabled(true);
//                Bitmap bitmapText = textView.getDrawingCache(true);
//                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmapText);
//                MarkerOptions oo = new MarkerOptions()
//                        .icon(bitmapDescriptor)
//                        .position(beanLng);
//                mMap.addMarker(oo);
//                bitmapText.recycle();

                BitmapDescriptor bd = BitmapDescriptorFactory.fromView(textView);
                Logger.d(bd.getBitmap().getByteCount());
                MarkerOptions oo = new MarkerOptions()
                        .icon(bd)
                        .position(beanLng);
                mMap.addMarker(oo);
//                bd.getBitmap().recycle();

                MarkerOptions beanOption = new MarkerOptions().icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .position(beanLng)
                        .draggable(false);
                mMap.addMarker(beanOption);
            }
        }
    }


    /**
     * 开始移动
     */
    public void startMove() {
        // 读取轨迹点
        if (CommUtil.checkIsNull(mList)) {
            return;
        }
        if (CommUtil.checkIsNull(points))
            points = readLatLngs();
        // 构建 轨迹的显示区域
//        LatLngBounds bounds = new LatLngBounds(points.get(0), points.get(points.size() - 2));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

        // 实例 SmoothMoveMarker 对象
        smoothMarker = new SmoothMoveMarker(mMap);
        // 设置 平滑移动的 图标
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.car));

        // 取轨迹点的第一个点 作为 平滑移动的启动
        LatLng drivePoint = points.get(0);
        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
        points.set(pair.first, drivePoint);
        List<LatLng> subList = points.subList(pair.first, points.size());


        // 设置轨迹点
        smoothMarker.setPoints(subList);
        // 设置平滑移动的总时间  单位  秒
        smoothMarker.setTotalDuration(60);
        smoothMarker.startSmoothMove();
        float zoom = mMap.getCameraPosition().zoom;
        LatLng latLng = new LatLng(mList.get(0).getResultLAT(), mList.get(0).getResultLON());
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, zoom, 0, 0)));

//        mLatLngList=subList;
//        testMove(subList);

//        // 设置  自定义的InfoWindow 适配器
//        mMap.setInfoWindowAdapter(infoWindowAdapter);
//        // 显示 infowindow
//        smoothMarker.getMarker().showInfoWindow();

//         设置移动的监听事件  返回 距终点的距离  单位 米
        final int screenWidth = ScreenUtil.getScreenWidth(getContext());
        final int screenHeight = ScreenUtil.getScreenHeight(getContext());
        final int navigationBarHeight = ScreenUtil.getNavigationBarHeight(mActivity);
//        final int screenHeight = ScreenUtil.getSceenHeight(mActivity);
        final int borderX = screenWidth / 6;
        final int borderY = screenHeight / 8;
        smoothMarker.setMoveListener(new SmoothMoveMarker.MoveListener() {
            @Override
            public void move(final double distance) {

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //到屏幕边缘的话移动地图中心点
//                        if (infoWindowLayout != null && title != null) {
//
//                            title.setText("距离终点还有： " + (int) distance + "米");
//                        }
                        float zoom = mMap.getCameraPosition().zoom;
//                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(smoothMarker.getPosition(), zoom, 0, 0)));
//                        int size = points.size();
//                        long itemTime = ((long) 60*1000)/size;
//                        Logger.d(itemTime);
//                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(smoothMarker.getPosition(),zoom,0,0)),itemTime,null);

                        Point mPoint = mMap.getProjection().toScreenLocation(smoothMarker.getPosition());
                        if (mPoint != null) {
                            int x = mPoint.x;
                            int y = mPoint.y;
                            if (x < borderX || screenWidth - x < borderX || y < borderY || screenHeight - y < borderY + navigationBarHeight) {
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(smoothMarker.getPosition(), zoom, 0, 0)));
                            }
                        }
                    }
                });

            }
        });
        // 开始移动

    }

    public final Handler mHnalder = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            if (msg.what == 0) {
                Logger.d(size);
                testMove(mLatLngList);
            }
        }
    };
    int size = 0;
    List<LatLng> mLatLngList;

    private void testMove(List<LatLng> subList) {
        // 设置轨迹点
        if (size == mLatLngList.size() - 1) {
            return;
        }
        float distance = AMapUtils.calculateLineDistance(subList.get(size), subList.get(size + 1));
        int time = (int) (distance / 2);
        if (time == 0)
            time = 1;
        smoothMarker.setPoints(subList.subList(size, ++size));
        // 设置平滑移动的总时间  单位  秒
        smoothMarker.setTotalDuration(time);
        smoothMarker.startSmoothMove();
        mHnalder.sendEmptyMessageDelayed(0, time);
    }


    /**
     * 个性化定制的信息窗口视图的类
     * 如果要定制化渲染这个信息窗口，需要重载getInfoWindow(Marker)方法。
     * 如果只是需要替换信息窗口的内容，则需要重载getInfoContents(Marker)方法。
     */
    AMap.InfoWindowAdapter infoWindowAdapter = new AMap.InfoWindowAdapter() {

        // 个性化Marker的InfoWindow 视图
        // 如果这个方法返回null，则将会使用默认的信息窗口风格，内容将会调用getInfoContents(Marker)方法获取
        @Override
        public View getInfoWindow(Marker marker) {

            return getInfoWindowView(marker);
        }

        // 这个方法只有在getInfoWindow(Marker)返回null 时才会被调用
        // 定制化的view 做这个信息窗口的内容，如果返回null 将以默认内容渲染
        @Override
        public View getInfoContents(Marker marker) {

            return getInfoWindowView(marker);
        }
    };

    LinearLayout infoWindowLayout;
    TextView title;
    TextView snippet;

    /**
     * 自定义View并且绑定数据方法
     *
     * @param marker 点击的Marker对象
     * @return 返回自定义窗口的视图
     */
    private View getInfoWindowView(Marker marker) {
        if (infoWindowLayout == null) {
            infoWindowLayout = new LinearLayout(mActivity);
            infoWindowLayout.setOrientation(LinearLayout.VERTICAL);
            title = new TextView(mActivity);
            snippet = new TextView(mActivity);
            title.setTextColor(Color.BLACK);
            snippet.setTextColor(Color.BLACK);
            infoWindowLayout.setBackgroundResource(R.drawable.infowindow_bg);

            infoWindowLayout.addView(title);
            infoWindowLayout.addView(snippet);
        }

        return infoWindowLayout;
    }

    /**
     * 添加轨迹线
     */
    private void addPolylineInPlayGround() {
        if (CommUtil.checkIsNull(points))
            points = readLatLngs();
        List<Integer> colorList = new ArrayList<Integer>();
        List<BitmapDescriptor> bitmapDescriptors = new ArrayList<BitmapDescriptor>();

        int[] colors = new int[]{Color.argb(255, 0, 255, 0), Color.argb(255, 255, 255, 0), Color.argb(255, 255, 0, 0)};

        //用一个数组来存放纹理
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        textureList.add(BitmapDescriptorFactory.fromResource(R.drawable.custtexture));

        List<Integer> texIndexList = new ArrayList<Integer>();
        texIndexList.add(0);//对应上面的第0个纹理
        texIndexList.add(1);
        texIndexList.add(2);

        Random random = new Random();
        for (int i = 0; i < points.size(); i++) {
            colorList.add(colors[random.nextInt(3)]);
            bitmapDescriptors.add(textureList.get(0));

        }

        mPolyline = mMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.custtexture)) //setCustomTextureList(bitmapDescriptors)
//				.setCustomTextureIndex(texIndexList)
                .addAll(points)
                .useGradient(true)
                .width(18));

//

    }

    /**
     * 读取坐标点
     *
     * @return
     */
    private List<LatLng> readLatLngs() {
//        List<LatLng> points = new ArrayList<LatLng>();
//        for (int i = 0; i < coords.length; i += 2) {
//            points.add(new LatLng(coords[i + 1], coords[i]));
//        }
//        return points;
        List<LatLng> points = new ArrayList<LatLng>();
        for (CarTrackQuery.DataBean.TrackListBean bean : mList) {
//            CoordinateConverter converter = new CoordinateConverter(mActivity);
//            // CoordType.GPS 待转换坐标类型
//            converter.from(CoordinateConverter.CoordType.GPS);
//            // sourceLatLng待转换坐标点 LatLng类型
//            converter.coord(new LatLng(bean.getResultLAT(), bean.getResultLON()));
//            // 执行转换操作
//            LatLng desLatLng = converter.convert();
            points.add(new LatLng(bean.getResultLAT(), bean.getResultLON()));
        }
        return points;
    }

    /**
     * 坐标点数组数据
     */
    private double[] coords = {116.3499049793749, 39.97617053371078,
            116.34978804908442, 39.97619854213431, 116.349674596623,
            39.97623045687959, 116.34955525200917, 39.97626931100656,
            116.34943728748914, 39.976285626595036, 116.34930864705592,
            39.97628129172198, 116.34918981582413, 39.976260803938594,
            116.34906721558868, 39.97623535890678, 116.34895185151584,
            39.976214717128855, 116.34886935936889, 39.976280148755315,
            116.34873954611332, 39.97628182112874, 116.34860763527448,
            39.97626038855863, 116.3484658907622, 39.976306080391836,
            116.34834585430347, 39.976358252119745, 116.34831166130878,
            39.97645709321835, 116.34827643560175, 39.97655231226543,
            116.34824186261169, 39.976658372925556, 116.34825080406188,
            39.9767570732376, 116.34825631960626, 39.976869087779995,
            116.34822111635201, 39.97698451764595, 116.34822901510276,
            39.977079745909876, 116.34822234337618, 39.97718701787645,
            116.34821627457707, 39.97730766147824, 116.34820593515043,
            39.977417746816776, 116.34821013897107, 39.97753930933358
            , 116.34821304891533, 39.977652209132174, 116.34820923399242,
            39.977764016531076, 116.3482045955917, 39.97786190186833,
            116.34822159449203, 39.977958856930286, 116.3482256370537,
            39.97807288885813, 116.3482098441266, 39.978170063673524,
            116.34819564465377, 39.978266951404066, 116.34820541974412,
            39.978380693859116, 116.34819672351216, 39.97848741209275,
            116.34816588867105, 39.978593409607825, 116.34818489339459,
            39.97870216883567, 116.34818473446943, 39.978797222300166,
            116.34817728972234, 39.978893492422685, 116.34816491505472,
            39.978997133775266, 116.34815408537773, 39.97911413849568,
            116.34812908154862, 39.97920553614499, 116.34809495907906,
            39.979308267469264, 116.34805113358091, 39.97939658036473,
            116.3480310509613, 39.979491697188685, 116.3480082124968,
            39.979588529006875, 116.34799530586834, 39.979685789111635,
            116.34798818413954, 39.979801430587926, 116.3479996420353,
            39.97990758587515, 116.34798697544538, 39.980000796262615,
            116.3479912988137, 39.980116318796085, 116.34799204219203,
            39.98021407403913, 116.34798535084123, 39.980325006125696,
            116.34797702460183, 39.98042511477518, 116.34796288754136,
            39.98054129336908, 116.34797509821901, 39.980656820423505,
            116.34793922017285, 39.98074576792626, 116.34792586413015,
            39.98085620772756, 116.3478962642899, 39.98098214824056,
            116.34782449883967, 39.98108306010269, 116.34774758827285,
            39.98115277119176, 116.34761476652932, 39.98115430642997,
            116.34749135408349, 39.98114590845294, 116.34734772765582,
            39.98114337322547, 116.34722082902628, 39.98115066909245,
            116.34708205250223, 39.98114532232906, 116.346963237696,
            39.98112245161927, 116.34681500222743, 39.981136637759604,
            116.34669622104072, 39.981146248090866, 116.34658043260109,
            39.98112495260716, 116.34643721418927, 39.9811107163792,
            116.34631638374302, 39.981085081075676, 116.34614782996252,
            39.98108046779486, 116.3460256053666, 39.981049089345206,
            116.34588814050122, 39.98104839362087, 116.34575119741586,
            39.9810544889668, 116.34562885420186, 39.981040940565734,
            116.34549232235582, 39.98105271658809, 116.34537348820508,
            39.981052294975264, 116.3453513775533, 39.980956549928244
    };

}
