package com.sachith.applauncher.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.GridView;

import com.sachith.applauncher.R;
import com.sachith.applauncher.adapter.GridViewAdapter;
import com.sachith.applauncher.model.Apps;
import com.sachith.applauncher.service.ClickListenerService;

import java.util.ArrayList;
import java.util.List;

public class AppListActivity extends AppCompatActivity implements ClickListenerService {

    private PackageManager packageManager;
    private List<Apps> appList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        //Setup UI
        init();
    }

    @Override
    public void openApp(Apps app) {
        startActivity(new Intent((packageManager.getLaunchIntentForPackage(app.getLabel().toString()))));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_none, R.anim.anim_slide_out_bottom);
    }

    //<editor-fold desc="Private Methods">

    //Setup UI
    private void init() {
        //Load apps from system
        loadApps();
    }

    //Load apps from system
    private void loadApps() {
        packageManager = getPackageManager();
        appList = new ArrayList<>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = packageManager.queryIntentActivities(i,0);
        for(ResolveInfo resolveInfo: availableActivities){
            Apps newApp = new Apps();
            newApp.setLabel(resolveInfo.activityInfo.packageName);
            newApp.setName(resolveInfo.loadLabel(packageManager));
            newApp.setIcon(resolveInfo.loadIcon(packageManager));
            appList.add(newApp);
        }

        //Display apps in grid view
        displayApps();
    }

    //Display apps in grid view
    private void displayApps() {
        GridViewAdapter gridViewAdapter = new GridViewAdapter(appList,this,this);
        GridView appsView = findViewById(R.id.appGrid);
        appsView.setAdapter(gridViewAdapter);
    }

    //</editor-fold>
}