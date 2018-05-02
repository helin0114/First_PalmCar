package com.cango.adpickcar.jdetail.jcarinfo;


import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.jdetail.JDetailFragment;
import com.cango.adpickcar.jdetail.JDetailPresenter;
import com.cango.adpickcar.model.DeliveryTaskList.DataBean.CarDeliveryTaskListBean;
import com.cango.adpickcar.model.JCarInfo;

import butterknife.BindView;

public class JCarInfoFragment extends BaseFragment {

    public static JCarInfoFragment getInstance(CarDeliveryTaskListBean carDeliveryTaskListBean) {
        JCarInfoFragment jCarInfoFragment = new JCarInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bean", carDeliveryTaskListBean);
        jCarInfoFragment.setArguments(bundle);
        return jCarInfoFragment;
    }

    @BindView(R.id.nsv_jcar)
    NestedScrollView nsvCar;
    @BindView(R.id.tv_jcar_no)
    TextView tvCarNo;
    @BindView(R.id.tv_japplycd)
    TextView tvApplyCD;
    @BindView(R.id.tv_jfininstid)
    TextView tvFininstid;
    @BindView(R.id.tv_jorgid)
    TextView tvJorgid;
    @BindView(R.id.tv_jvin)
    TextView tvJvin;
    @BindView(R.id.tv_jcustname)
    TextView tvJcustname;
    @BindView(R.id.tv_jlicenseplatetype)
    TextView tvJlicenseplatetype;
    @BindView(R.id.tv_jphoto_number)
    TextView tvJphotoNumber;
    @BindView(R.id.tv_jcarcertno)
    TextView tvJcarcertno;
    @BindView(R.id.tv_jmfyear)
    TextView tvJmfyear;
    @BindView(R.id.tv_jengineno)
    TextView tvJengineno;
    @BindView(R.id.tv_jcarregno)
    TextView tvJcarregno;
    @BindView(R.id.tv_jbrand)
    TextView tvJbrand;
    @BindView(R.id.tv_jchexi)
    TextView tvJchexi;
    @BindView(R.id.tv_jcartype)
    TextView tvJcartype;
    @BindView(R.id.tv_jcartype_real)
    TextView tvJcartypeReal;
    @BindView(R.id.tv_jcarcolor)
    TextView tvJcarcolor;
    @BindView(R.id.tv_jcaroldtype)
    TextView tvJcaroldtype;
    @BindView(R.id.tv_jdeploy)
    TextView tvJdeploy;
    @BindView(R.id.tv_jpailiang)
    TextView tvJpailiang;
    @BindView(R.id.tv_jzhidao)
    TextView tvjzhidao;
    @BindView(R.id.tv_jkaipiao)
    TextView tvJkaipiao;
    @BindView(R.id.tv_jkaipiaotime)
    TextView tvJkaipiaotime;
    @BindView(R.id.tv_jfapiao)
    TextView tvJfapiao;
    @BindView(R.id.tv_jgouzhi)
    TextView tvJgouzhi;
    @BindView(R.id.tv_jshangpaitime)
    TextView tvJshangpaitime;
    @BindView(R.id.tv_jbaodantime)
    TextView tvJbaodantime;
    @BindView(R.id.tv_jbaoxian)
    TextView tvJbaoxian;
    @BindView(R.id.tv_jnianjiantime)
    TextView tvJnianjiantime;
    @BindView(R.id.tv_jpinggu)
    TextView tvJpinggu;
    @BindView(R.id.tv_jkudian)
    TextView tvJkudian;
    @BindView(R.id.tv_jkuwei)
    TextView tvJkuwei;
    @BindView(R.id.tv_jshuxing)
    TextView tvJshuxing;
    @BindView(R.id.tv_jzhuangtai)
    TextView tvJzhuangtai;
    @BindView(R.id.tv_jzichan)
    TextView tvJzichan;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;

    private JDetailPresenter mPresenter;
    public CarDeliveryTaskListBean mCarDeliveryTaskListBean;

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_jcar_info;
    }

    @Override
    protected void initView() {
//        getData();
    }

    @Override
    protected void initData() {
        mPresenter = (JDetailPresenter) ((JDetailFragment) getParentFragment()).mPresenter;
        mCarDeliveryTaskListBean = getArguments().getParcelable("bean");
    }

    public void getData() {
        if (mCarDeliveryTaskListBean != null && mPresenter != null) {
            mPresenter.GetCarInfo(true,mCarDeliveryTaskListBean.getDisCarID()+"");
        }
    }

    public void updateUI(JCarInfo.DataBean carDataBean) {
        tvCarNo.setText(carDataBean.getDisCarNo());
        tvApplyCD.setText(carDataBean.getApplyCD());
        tvFininstid.setText(carDataBean.getFininstName());
        tvJorgid.setText(carDataBean.getOrgName());
        tvJvin.setText(carDataBean.getVin());
        tvJcustname.setText(carDataBean.getCustName());
        tvJlicenseplatetype.setText(carDataBean.getLicensePlateType());
        tvJphotoNumber.setText(carDataBean.getLicensePlateNo());
        tvJcarcertno.setText(carDataBean.getCarcertNO());
        tvJmfyear.setText(carDataBean.getMfYear());
        tvJengineno.setText(carDataBean.getEngineNO());
        tvJcarregno.setText(carDataBean.getCarRegNO());
        tvJbrand.setText(carDataBean.getCarBrandName());
        tvJchexi.setText(carDataBean.getCarSeriesName());
        tvJcartype.setText(carDataBean.getCarModelName());
        if("1".equals(carDataBean.getIsErpMapping())){
            tvJcartypeReal.setText(carDataBean.getCarModelName());
        }else{
            tvJcartypeReal.setText(carDataBean.getRealCarModel());
        }
        tvJcarcolor.setText(carDataBean.getColor());
        tvJcaroldtype.setText(carDataBean.getCarModelText());
        tvJdeploy.setText(carDataBean.getCarGrade());
        tvJpailiang.setText(carDataBean.getCarDISPLACEMENT());
        tvjzhidao.setText(carDataBean.getGUIDEPrice());
        tvJkaipiao.setText(carDataBean.getInvoicePrice());
        tvJkaipiaotime.setText(carDataBean.getInvoiceDate());
        tvJfapiao.setText(carDataBean.getInvoiceNO());
        tvJgouzhi.setText(carDataBean.getPurchaseTax());
        tvJshangpaitime.setText(carDataBean.getLicenseRegDate());
        tvJbaodantime.setText(carDataBean.getInsuranceENDDate());
        tvJbaoxian.setText(carDataBean.getInsuranceCO());
        tvJnianjiantime.setText(carDataBean.getInspectionDate());
        tvJpinggu.setText(carDataBean.getEVAPrice());
        tvJkudian.setText(carDataBean.getWHName());
        tvJkuwei.setText(carDataBean.getWHPosition());
        tvJshuxing.setText(carDataBean.getCarPropertyTitle());
        tvJzhuangtai.setText(carDataBean.getCarStateTitle());
        tvJzichan.setText(carDataBean.getCarOwnershipTitle());

        nsvCar.setVisibility(View.VISIBLE);
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
    }

    public void showError() {
        nsvCar.setVisibility(View.GONE);
        llSorry.setVisibility(View.VISIBLE);
        llNoData.setVisibility(View.GONE);
    }

    public void showNoData() {
        nsvCar.setVisibility(View.GONE);
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.VISIBLE);
    }

}
