package com.cango.adpickcar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cango.adpickcar.net.NetManager;
import com.cango.adpickcar.net.RxSubscriber;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final ImageView iv = (ImageView) findViewById(R.id.iv_test);

        Retrofit retrofit = new Retrofit.Builder()
                .client(NetManager.getInstance().getOkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://image.cangoonline.net")
                .build();
        TestService service = retrofit.create(TestService.class);
        service.getTest()
                .subscribeOn(Schedulers.io())
                .map(new Func1<Test, byte[]>() {
                    @Override
                    public byte[] call(Test test) {
                        return Base64.decode(test.getBase64Content(),Base64.DEFAULT);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<byte[]>() {
                    @Override
                    protected void _onNext(byte[] bytes) {
                        Glide.with(TestActivity.this)
                                .load(bytes)
                                .into(iv);
                    }

                    @Override
                    protected void _onError() {

                    }
                });
    }

    interface TestService{
        @GET("erp-image/IF/IDownload/PWPKacPDLzbfADW-w3Y45g%3D%3D_2b2d72d1e1fe0e00e949068f09285681cf36de1e.do")
        Observable<Test> getTest();
    }
    class Test{

        /**
         * result : 1
         * errMessage : null
         */

        private int result;
        private Object errMessage;
        private String base64Content;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public Object getErrMessage() {
            return errMessage;
        }

        public void setErrMessage(Object errMessage) {
            this.errMessage = errMessage;
        }

        public String getBase64Content() {
            return base64Content;
        }

        public void setBase64Content(String base64Content) {
            this.base64Content = base64Content;
        }
    }
}
