package com.cango.adpickcar.detail.carinfo;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.detail.DetailActivity;
import com.cango.adpickcar.detail.DetailFragment;
import com.cango.adpickcar.detail.DetailPresenter;
import com.cango.adpickcar.model.CarInfo;
import com.cango.adpickcar.model.CarTakeTaskList;
import com.cango.adpickcar.util.SnackbarUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class CarInfoFragment extends BaseFragment {
    public static CarInfoFragment getInstance(CarTakeTaskList.DataBean.CarTakeTaskListBean bean) {
        CarInfoFragment carInfoFragment = new CarInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bean",bean);
        carInfoFragment.setArguments(bundle);
        return carInfoFragment;
    }

    @BindView(R.id.nsv_car)
    NestedScrollView nsvCar;
    @BindView(R.id.tv_CarBrandName)
    TextView tvCarBrandName;
//    @BindView(R.id.sp_carmodelname)
//    Spinner spCarModelName;
    @BindView(R.id.switch_isERPMapping)
    SwitchCompat switchIsERPMapping;
    @BindView(R.id.tv_carSeriesName)
    TextView tvCarSeriesName;
    @BindView(R.id.tv_color)
    TextView tvColor;
    @BindView(R.id.tv_discarno)
    TextView tvDiscarno;
    @BindView(R.id.tv_applycd)
    TextView tvApplyCD;
    @BindView(R.id.tv_fininstid)
    TextView tvFininstid;
    @BindView(R.id.tv_orgid)
    TextView tvOrgid;
    @BindView(R.id.tv_vin)
    TextView tvVin;
    @BindView(R.id.tv_custname)
    TextView tvCustName;
    @BindView(R.id.tv_licenseplatetype)
    TextView tvLicenseplateType;
    @BindView(R.id.tv_model_name)
    TextView tvModelName;
    @BindView(R.id.tv_model_name_inpractice)
    TextView tvModelNameInpractice;
    @BindView(R.id.tv_car_model_name_alter)
    TextView tvModelNameAlter;
    @BindView(R.id.tv_photo_number)
    TextView tvPhotoNumber;
    @BindView(R.id.tv_carcertno)
    TextView tvCarCertNO;
    @BindView(R.id.tv_mfyear)
    TextView tvMfyear;
    @BindView(R.id.tv_engineno)
    TextView tvEngineno;
    @BindView(R.id.tv_carregno)
    TextView tvCarRegno;
    @BindView(R.id.tv_carmodeltext)
    TextView tvCarModelText;
    @BindView(R.id.layout_model_name_inpractice)
    LinearLayout layoutModelNameInpractice;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;

    @OnClick({R.id.tv_car_info_alter, R.id.tv_car_model_name_alter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_car_info_alter:
                if (isEdit) {
                    final EditText et = new EditText(mActivity);
                    new AlertDialog.Builder(mActivity).setTitle("更新车牌号码")
                            .setView(et)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String input = et.getText().toString();
                                    if (input.replaceAll(" ","").equals("")) {
//                                        ToastUtils.showShort("请输入内容");
                                        SnackbarUtils.showLongDisSnackBar(mActivity.findViewById(R.id.layout_main_car_info), "请输入内容");
                                    } else {
                                        tvPhotoNumber.setText(input);
                                        detailFragment.saveCarInfo(null,null,null);
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
                break;
            case R.id.tv_car_model_name_alter:
                if (isEdit) {
                    showchangeModelDialog(true);
                }
                break;
        }
    }

    private DetailActivity mActivity;
    private DetailFragment detailFragment;
    private DetailPresenter presenter;
    private CarTakeTaskList.DataBean.CarTakeTaskListBean mCarTakeTaskListBean;
    private boolean isEdit;
    private boolean isChangeToNo;
    private CarInfo mCarInfo;

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_car_info;
    }

    @Override
    protected void initView() {
//        getData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("CarInfo",mCarInfo);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null){
            mCarInfo = savedInstanceState.getParcelable("CarInfo");
        }
    }

    public void getData() {
        if (mCarTakeTaskListBean!=null){
            presenter.GetCarTakeStoreCarInfo(true, mCarTakeTaskListBean.getDisCarID() + "");
        }
    }

    @Override
    protected void initData() {
        mActivity = (DetailActivity) getActivity();
        detailFragment = (DetailFragment) getParentFragment();
        presenter = (DetailPresenter) ((DetailFragment) getParentFragment()).mPresenter;
        mCarTakeTaskListBean = getArguments().getParcelable("bean");
        isEdit = ((DetailFragment) getParentFragment()).isEdit;
    }

    public void updateUI(CarInfo carInfo) {
        mCarInfo = carInfo;
        if (isEdit) {
        } else {
            switchIsERPMapping.setClickable(false);
            switchIsERPMapping.setFocusable(false);
            switchIsERPMapping.setFocusableInTouchMode(false);
//            spCarModelName.setClickable(false);
//            spCarModelName.setFocusable(false);
//            spCarModelName.setFocusableInTouchMode(false);
//            spCarModelName.setEnabled(false);
            tvModelNameAlter.setEnabled(false);
        }
        CarInfo.DataBean dataBean = mCarInfo.getData();
        tvCarBrandName.setText(dataBean.getCarBrandName());
//        tvCarModelName.setText(dataBean.getCarModelName());
        switchIsERPMapping.setChecked(dataBean.getIsErpMapping().equals("1"));
        if (switchIsERPMapping.isChecked()){
            switchIsERPMapping.setSwitchTextAppearance(getActivity(),R.style.on);
            if (isEdit) {
//                spCarModelName.setClickable(false);
//                spCarModelName.setFocusable(false);
//                spCarModelName.setFocusableInTouchMode(false);
//                spCarModelName.setEnabled(false);
                tvModelNameAlter.setEnabled(false);
            } else {
            }
            layoutModelNameInpractice.setVisibility(View.GONE);
            isChangeToNo = false;
        }else {
            switchIsERPMapping.setSwitchTextAppearance(getActivity(),R.style.off);
            if (isEdit) {
//                spCarModelName.setEnabled(true);
                tvModelNameAlter.setEnabled(true);
            } else {
            }
            layoutModelNameInpractice.setVisibility(View.VISIBLE);
            isChangeToNo = true;
        }
        tvModelNameInpractice.setText(dataBean.getRealCarModel());
        tvCarSeriesName.setText(dataBean.getCarSeriesName());
        tvColor.setText(dataBean.getColor());
        tvDiscarno.setText(dataBean.getDisCarNo());
        tvApplyCD.setText(dataBean.getApplyCD());
        tvFininstid.setText(dataBean.getFininstName());
        tvOrgid.setText(dataBean.getOrgName());
        tvVin.setText(dataBean.getVin());
        tvCustName.setText(dataBean.getCustName());
        tvLicenseplateType.setText(dataBean.getLicensePlateType());
        tvPhotoNumber.setText(dataBean.getLicensePlateNo());
        tvCarCertNO.setText(dataBean.getCarcertNO());
        tvMfyear.setText(dataBean.getMfYear());
        tvEngineno.setText(dataBean.getEngineNO());
        tvCarRegno.setText(dataBean.getCarRegNO());
        tvCarModelText.setText(dataBean.getCarModelText());
        switchIsERPMapping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    switchIsERPMapping.setSwitchTextAppearance(getActivity(),R.style.on);
                    if (isEdit) {
//                        spCarModelName.setClickable(false);
//                        spCarModelName.setFocusable(false);
//                        spCarModelName.setFocusableInTouchMode(false);
//                        spCarModelName.setEnabled(false);
                        tvModelNameAlter.setEnabled(false);
                    } else {
                    }
                    layoutModelNameInpractice.setVisibility(View.GONE);
                    if(isChangeToNo){
                        detailFragment.saveCarInfo("1",null,null);
                    }
                }else {
                    if (isEdit) {
//                        spCarModelName.setEnabled(true);
                        tvModelNameAlter.setEnabled(true);
                        layoutModelNameInpractice.setVisibility(View.VISIBLE);
                        showchangeModelDialog(false);
                    } else {
                    }
                    switchIsERPMapping.setSwitchTextAppearance(getActivity(),R.style.off);
                }
            }
        });
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        nsvCar.setVisibility(View.VISIBLE);

        //初始化
//        final ArrayList<CarInfo.DataBean.ModelListBean> modelList =
//                (ArrayList<CarInfo.DataBean.ModelListBean>) (dataBean.getModelList());
//        ArrayAdapter<CarInfo.DataBean.ModelListBean> carModelAdapter = new ArrayAdapter<>(getActivity(),
//                R.layout.simple_spinner_item, modelList);
//        carModelAdapter.setDropDownViewResource(R.layout.my_drop_down_item);
//        spCarModelName.setAdapter(carModelAdapter);
//        for (CarInfo.DataBean.ModelListBean bean : modelList) {
//            if (bean.getId().equals(dataBean.getCarModelID())) {
//                isFirstSP = true;
////                spCarModelName.setSelection(modelList.indexOf(bean));
//                tvModelName.setText(bean.getValue());
//            }
//        }
        tvModelName.setText(dataBean.getCarModelName());
//        spCarModelName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (isFirstSP){
//                    isFirstSP = false;
//                    return;
//                }
//                detailFragment.saveCarInfo(modelList.get(position).getId());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }
    private boolean isFirstSP = false;
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

    public String getIsErpMapping() {
        mCarInfo.getData().setIsErpMapping(switchIsERPMapping.isChecked() ? "1" : "0");
        return switchIsERPMapping.isChecked() ? "1" : "0";
    }

    public String getLicenseplateNO() {
        mCarInfo.getData().setLicensePlateNo(tvPhotoNumber.getText().toString().trim());
        return tvPhotoNumber.getText().toString().trim();
    }

    /**
     * 显示更新车型的输入框
     * @param isFromAfter   （true：从实际车型后面的修改按钮点入
     *                        false：从关闭与ERP相同的switch按钮点入）
     */
    private void showchangeModelDialog(final boolean isFromAfter){
        final EditText et = new EditText(mActivity);
        new AlertDialog.Builder(mActivity).setTitle("实际车型")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if (input.replaceAll(" ","").equals("")) {
//                                        ToastUtils.showShort("请输入内容");
                            SnackbarUtils.showLongDisSnackBar(mActivity.findViewById(R.id.layout_main_car_info), "请输入内容");
                        } else {
                            tvModelNameInpractice.setText(input);
                            detailFragment.saveCarInfo("0",null,input);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //当从关闭与ERP相同的switch按钮点入的时候，
                        // dialog消失，如果输入框里面没有内容，则让switch重新标为“是”，并让“实际车型”不显示
                        String input = et.getText().toString();
                        if (!isFromAfter && input.replaceAll(" ","").equals("")) {
                            isChangeToNo = false;
                            layoutModelNameInpractice.setVisibility(View.GONE);
                            switchIsERPMapping.setSwitchTextAppearance(getActivity(), R.style.on);
                            switchIsERPMapping.setChecked(true);
                        }
                    }
                })
                .show();
    }
}
