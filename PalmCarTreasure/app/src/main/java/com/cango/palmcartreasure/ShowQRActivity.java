package com.cango.palmcartreasure;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.model.QRCodeBean;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.SizeUtil;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.EnumMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ShowQRActivity extends BaseActivity {

    //    @BindView(R.id.toolbar_code)
    Toolbar mToolbar;
    //    @BindView(R.id.iv_qrcode)
    ImageView ivQrCode;
    private QRCodeBean qrCodeBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qr);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_code);
        ivQrCode = (ImageView) findViewById(R.id.iv_qrcode);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        int statusBarHeight = BarUtil.getStatusBarHeight(this);
        int actionBarHeight = BarUtil.getActionBarHeight(this);
        if (Build.VERSION.SDK_INT >= 21) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbar.setLayoutParams(layoutParams);
            mToolbar.setPadding(0, statusBarHeight, 0, 0);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowQRActivity.this.onBackPressed();
            }
        });

        qrCodeBean = getIntent().getParcelableExtra("QRCodeBean");
        String content;
        try {
            content = new Gson().toJson(qrCodeBean);
            //使用rxjava将生成qrcode然后加载
            Observable.just(content)
                    .map(new Func1<String, byte[]>() {
                        @Override
                        public byte[] call(String s) {
                            return getQRCodeByString(s);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<byte[]>() {
                        @Override
                        public void call(byte[] bytes) {
                            Glide.with(ShowQRActivity.this)
                                    .load(bytes)
                                    .placeholder(R.drawable.qrcode)
                                    .error(R.drawable.wraing)
                                    .into(ivQrCode);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

//        qrCodeBean = getIntent().getParcelableExtra("QRCodeBean");
//        String content = new Gson().toJson(qrCodeBean);
//        Bitmap qrCode = null;
//        BitMatrix result;
//        Map<EncodeHintType, Object> hints = null;
//        String encoding = guessAppropriateEncoding(content);
//        if (encoding != null) {
//            hints = new EnumMap<>(EncodeHintType.class);
//            hints.put(EncodeHintType.CHARACTER_SET, encoding);
//        }
//        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//        try {
//            result = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, SizeUtil.dp2px(this, 300), SizeUtil.dp2px(this, 300), hints);
//            // 使用 ZXing Android Embedded 要写的代码
//            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//            qrCode = barcodeEncoder.createBitmap(result);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException iae) { // ?
//        }
//        if (qrCode != null) {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            qrCode.compress(Bitmap.CompressFormat.PNG, 100, baos);
//            byte[] bytes = baos.toByteArray();
//            Glide.with(this)
//                    .load(bytes)
//                    .placeholder(R.drawable.qrcode)
//                    .error(R.drawable.wraing)
//                    .into(ivQrCode);
//        }
    }

    private byte[] getQRCodeByString(String s) {
        Bitmap qrCode = null;
        byte[] qrBytes = new byte[0];
        BitMatrix result;
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(s);
        if (encoding != null) {
            hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(s, BarcodeFormat.QR_CODE,
                    SizeUtil.dp2px(ShowQRActivity.this, 300),
                    SizeUtil.dp2px(ShowQRActivity.this, 300), hints);
            // 使用 ZXing Android Embedded 要写的代码
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            qrCode = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) { // ?
        }
        if (qrCode != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            qrCode.compress(Bitmap.CompressFormat.PNG, 100, baos);
            qrBytes = baos.toByteArray();
        }
        return qrBytes;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }
}
