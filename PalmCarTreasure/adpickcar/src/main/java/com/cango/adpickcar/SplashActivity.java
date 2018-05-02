package com.cango.adpickcar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.TextView;

import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.fc.main.fcmain.FcMainActivity;
import com.cango.adpickcar.login.LoginActivity;
import com.cango.adpickcar.main.MainActivity;
import com.cango.adpickcar.util.AppUtils;
import com.cango.adpickcar.util.SnackbarUtils;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("v" + AppUtils.getVersionName(this));

        if (ADApplication.mSPUtils.getString(Api.USERID) != null) {
            String role = ADApplication.mSPUtils.getString(Api.ROLE);
            if (Api.AD.equals(role)) {
                startDelay(MainActivity.class);
            } else if (Api.FCEmp.equals(role) || Api.FCS.equals(role)) {
//                startDelay(com.cango.adpickcar.fc.main.MainActivity.class);
                startDelay(FcMainActivity.class);
//                用来测试IMEI定位功能
//                startDelay(LocationActivity.class);

//                String test = test();
//                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//                String path = storageDir.getAbsolutePath()+File.separator+"JPEG_20180123_100930_10042905.jpg";
//                String test = bitmapToString(path);
//                Logger.d(test);
            } else {
                startDelay(LoginActivity.class);
                SnackbarUtils.showLongDisSnackBar(findViewById(R.id.layout_splash_main),
                        R.string.data_unusual);
            }
        } else {
            startDelay(LoginActivity.class);
        }
    }

    private void startDelay(final Class cls) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, cls);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

//    private String test() {
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.test);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
//        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
//        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
//        byte[] b = baos.toByteArray();
//        return Base64.encodeToString(b, Base64.DEFAULT);
//    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //把bitmap转换成String
    public static String bitmapToString(String filePath) {
        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}
