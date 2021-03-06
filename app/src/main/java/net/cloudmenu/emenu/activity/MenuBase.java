package net.cloudmenu.emenu.activity;

import android.app.Activity;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import net.cloudmenu.emenu.R;
import net.cloudmenu.emenu.task.GetMenuTask;
import net.cloudmenu.emenu.utils.GlobalValue;
import net.cloudmenu.emenu.utils.MenuUtils;
import net.cloudmenu.emenu.utils.MenuUtils.GoodsCategory;

import java.util.List;

import cn.buding.common.asynctask.HandlerMessageTask;
import cn.buding.common.asynctask.HandlerMessageTask.Callback;
import cn.com.cloudstone.menu.server.thrift.api.Menu;
import greendroid.widget.PagedView;

public abstract class MenuBase extends SlideBase {
    private static final String TAG = "MenuBase";
    protected Menu mMenu;
    private GetMenuTask mGetMenuTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initElements();
        mMenu = GlobalValue.getIns().getMenu();
        if (mMenu == null)
            initData();
        else
            initContent();
    }

    @Override
    protected void initContent() {
        super.initContent();
        setParentLogoUrl();
    }

    private void setParentLogoUrl() {
        Activity context = getParent();
        while (context != null) {
//            if (context instanceof MainTabHost) {
//                MainTabHost host = (MainTabHost) context;
//                if (mMenu != null)
//                    host.setLogoUrl(mMenu.getMenuLogo());
//                else
//                    host.setLogoUrl(null);
//            }
            context = context.getParent();
        }
    }

    @Override
    protected void initElements() {
        super.initElements();
        setParentLogoUrl();
    }

    protected void initData() {
        initData(false);
    }

    protected void initData(boolean forseRefresh) {
        if (mGetMenuTask != null && mGetMenuTask.getStatus() == Status.RUNNING)
            return;
        mGetMenuTask = new GetMenuTask(this);
        mGetMenuTask.setForseRefresh(forseRefresh);
        mGetMenuTask.setCallback(new Callback() {
            @Override
            public void onSuccess(HandlerMessageTask task, Object t) {
                mMenu = mGetMenuTask.getResult();
                GlobalValue.getIns().setMenu(mMenu);
                initContent();
            }

            @Override
            public void onFail(HandlerMessageTask task, Object t) {
                Log.e("Menu", "failed to load" + t);
            }
        });
        mGetMenuTask.execute();
    }

    protected List<GoodsCategory> makeGoodsCategory() {
        return MenuUtils.getMenuGoodsCategories(mMenu);
    }

    @Override
    public void onPageChanged(PagedView pagedView, int previousPage, int newPage) {
        super.onPageChanged(pagedView, previousPage, newPage);

    }



}
