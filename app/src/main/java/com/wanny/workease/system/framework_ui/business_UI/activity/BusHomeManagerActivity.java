package com.wanny.workease.system.framework_ui.business_UI.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_care.ActivityStackManager;
import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpActivity;
import com.wanny.workease.system.framework_ui.business_UI.fragment.BusMainFragment;
import com.wanny.workease.system.framework_ui.business_UI.fragment.BusSendInfoFragment;
import com.wanny.workease.system.framework_ui.business_UI.fragment.BusUserCenterFragment;
import com.wanny.workease.system.framework_ui.customer_UI.fragment.MainFragment;
import com.wanny.workease.system.framework_ui.customer_UI.fragment.SendMessageFragment;
import com.wanny.workease.system.framework_ui.customer_UI.fragment.UserCenterFragment;
import com.wanny.workease.system.framework_uikite.dialog.HiFoToast;
import com.wanny.workease.system.workease_business.customer.homeMvc.HiFoFragmentTableItem;
import com.wanny.workease.system.workease_business.customer.homeMvc.HomeOperateView;
import com.wanny.workease.system.workease_business.customer.homeMvc.HomePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件名： HomeManagerActivity
 * 功能：
 * 作者： wanny
 * 时间： 14:23 2017/7/5
 */
public class BusHomeManagerActivity extends MvpActivity<HomePresenter> implements HomeOperateView {

    //当前页面菜单栏只有三个
    private final static int TABCOUNT = 3;
    //
    @BindView(R.id.realtabcontent)
    FrameLayout realtabcontent;
    //
    @BindView(android.R.id.tabcontent)
    FrameLayout tabcontent;
    //mTabHost
    @BindView(android.R.id.tabhost)
    FragmentTabHost tabhost;

    //底部 tab-bus 首页
    private HiFoFragmentTableItem mainTab = null;
    //底部 tab-发布用工需求
    private HiFoFragmentTableItem contactTab = null;
    //底部tab-个人中心
    private HiFoFragmentTableItem youselfTab = null;

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_fragment_activity);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        tabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        tabhost.getTabWidget().setDividerDrawable(null);
        mvpPresenter.setupTab();
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public FragmentTabHost getTabHost() {
        return tabhost;
    }


    @Override
    public int getTabCount() {
        return TABCOUNT;
    }

    @Override
    public HiFoFragmentTableItem getTabItem(int tabIndex) {
        switch (tabIndex) {
            case 0:
                return getMainTab();
            case 1:
                return getSendMessageTab();
//                return getReplyPriceTab();
            case 2:
                return getUserCenterTab();
            default:
                break;
        }
        return null;
    }

    /**
     * 设置fragment
     * @return
     */
    private HiFoFragmentTableItem getMainTab() {
        if (mainTab == null) {
            mainTab = new HiFoFragmentTableItem();
            mainTab.setName("首页");
            mainTab.setFragment(BusMainFragment.class);
            mainTab.setIconResourceId(R.drawable.icon_main_drawable);
        }
        return mainTab;
    }

    /**
     * 设置fragment
     * @return
     */
    private HiFoFragmentTableItem getSendMessageTab() {
        if (contactTab == null) {
            contactTab = new HiFoFragmentTableItem();
            contactTab.setName("信息发布");
            contactTab.setFragment(BusSendInfoFragment.class);
            contactTab.setIconResourceId(R.drawable.icon_find_drawable);
        }
        return contactTab;
    }




    /**
     * 设置fragment
     * @return
     */
    private HiFoFragmentTableItem getUserCenterTab() {
        if (youselfTab == null) {
            youselfTab = new HiFoFragmentTableItem();
            youselfTab.setName("我的");
            youselfTab.setFragment(BusUserCenterFragment.class);
            youselfTab.setIconResourceId(R.drawable.icon_myself_drawable);
        }
        return youselfTab;
    }

    @Override
    public View getTabView(int tabIndex) {
        return null;
    }


    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (tabhost.getCurrentTabTag().equals("信息发布")) {
            BusSendInfoFragment currFagment = (BusSendInfoFragment) getSupportFragmentManager().findFragmentByTag(tabhost.getCurrentTabTag());
            currFagment.onActivityResult(arg0, arg1, arg2);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null) {
            return;
        }
    }

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        super.startActivityFromFragment(fragment, intent, requestCode);
    }
    private long firstTime = 0;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 800) {// 如果两次按键时间间隔大于800毫秒，则不退出
                new HiFoToast(mContext, "再按一次退出易知工");
                firstTime = secondTime;// 更新firstTime
                return true;
            } else {
                ActivityStackManager.getInstance().exitAllActivityExceptOne();
                System.exit(0);// 否则退出程序
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void showLoading() {

    }


    @Override
    public void success(OrdinalResultEntity ordinalResultEntity) {

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
}
