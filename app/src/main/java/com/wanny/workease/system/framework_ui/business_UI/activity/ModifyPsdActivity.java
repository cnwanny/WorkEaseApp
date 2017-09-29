package com.wanny.workease.system.framework_ui.business_UI.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.PreferenceUtil;
import com.wanny.workease.system.framework_care.ActivityStackManager;
import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpActivity;
import com.wanny.workease.system.framework_ui.customer_UI.activity.LoginActivity;
import com.wanny.workease.system.framework_uikite.dialog.HiFoToast;
import com.wanny.workease.system.workease_business.business.modify_psd_mvp.ModifyPsdImpl;
import com.wanny.workease.system.workease_business.business.modify_psd_mvp.ModifyPsdPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件名： ModifyPsdActivity
 * 功能：
 * 作者： wanny
 * 时间： 9:58 2017/8/23
 */
public class ModifyPsdActivity extends MvpActivity<ModifyPsdPresenter> implements ModifyPsdImpl {

    //返回
    @BindView(R.id.title_left)
    TextView titleLeft;
    //标题
    @BindView(R.id.title_title)
    TextView titleTitle;
    //
    @BindView(R.id.modify_psd_new)
    EditText modifyPsdNew;
    //
    @BindView(R.id.modify_psd_sure)
    EditText modifyPsdSure;
    //
    @BindView(R.id.modify_psd_save)
    TextView modifyPsdSave;

    public static final int MODE_CUSTOMRE = 0x0001;
    public static final int MODE_BUSINESS = 0x0002;
    private int mode ;
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifypsd_activity_view);
        ButterKnife.bind(this);
        if(getIntent() != null){
            mode = getIntent().getIntExtra("mode",MODE_CUSTOMRE);
        }
        if(mode == MODE_BUSINESS){
            userId = PreferenceUtil.getInstance(mContext).getString("bususerId","");
        }else{
            userId = PreferenceUtil.getInstance(mContext).getString("userId","");
        }
        modifyPsdSure.addTextChangedListener(textWatcher);
        if(titleTitle != null){
            titleTitle.setText("修改密码");
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
          if(!s.toString().equals(modifyPsdNew.getText().toString())){
              modifyPsdSure.setTextColor(ContextCompat.getColor(mContext,R.color.red));
          }else{
              modifyPsdSure.setTextColor(ContextCompat.getColor(mContext,R.color.gray_normal));
          }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    public void success(OrdinalResultEntity entity) {
      if(entity.isSuccess()){
          //需要重新登录
          if(mode == MODE_BUSINESS){
              PreferenceUtil.getInstance(mContext).saveString("busmobile", "");
              PreferenceUtil.getInstance(mContext).saveString("busname", "");
              PreferenceUtil.getInstance(mContext).saveString("bususerId", "");
              Intent intent  = new Intent(ModifyPsdActivity.this,BusLoginActivity.class);
              startActivity(intent);
              ActivityStackManager.getInstance().exitActivity(mActivity);
          }else{
              PreferenceUtil.getInstance(mContext).saveString("mobile", "");
              PreferenceUtil.getInstance(mContext).saveString("name","");
              PreferenceUtil.getInstance(mContext).saveString("userId", "");
              Intent intent = new Intent(ModifyPsdActivity.this, LoginActivity.class);
              startActivity(intent);
              ActivityStackManager.getInstance().exitActivity(mActivity);
          }
      }else{
          if(!TextUtils.isEmpty(entity.getMsg())){
              new HiFoToast(mContext,entity.getMsg());
          }else{
              new HiFoToast(mContext,"密码修改失败");
          }
      }
    }

    @Override
    public void fail(String errorMessage) {

    }

    @Override
    public void loadIng(String title) {

    }

    @Override
    public void hide() {

    }

    @Override
    protected ModifyPsdPresenter createPresenter() {
        return new ModifyPsdPresenter(this);
    }



    @OnClick(R.id.modify_psd_save)
    void savePsd(View view){
        if(TextUtils.isEmpty(modifyPsdSure.getText().toString())){
            new HiFoToast(mContext,"请输入确认密码");
            return;
        }
        if(TextUtils.isEmpty(modifyPsdNew.getText().toString())){
            new HiFoToast(mContext,"请输入新密码");
            return;
        }
        if(mvpPresenter != null){
            mvpPresenter.modifypsd(userId,"",modifyPsdSure.getText().toString(),"正在保存");
        }
    }

    @OnClick(R.id.title_left)
    void backActivity(View view){
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }
}
