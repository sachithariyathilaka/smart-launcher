package com.sachith.applauncher.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import com.sachith.applauncher.R;
import com.sachith.applauncher.adapter.GridViewAdapter;
import com.sachith.applauncher.database.DbConnection;
import com.sachith.applauncher.model.AppPackage;
import com.sachith.applauncher.model.Apps;
import com.sachith.applauncher.service.ClickListenerService;
import com.sachith.popupsnackbar.PopupSnackbar;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements ClickListenerService {
    private final List<Apps> appList = new ArrayList<>();
    private Dialog disableApps;
    List<AppPackage> disabledApps = new ArrayList<AppPackage>();
    List<Apps> disabledAppList = new ArrayList<Apps>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Setup UI
        init();
    }

    @Override
    public void onAppClick(Apps app) {
        if(app.getDisable()){
            ConstraintLayout view = findViewById(R.id.activity_app_list);
            new PopupSnackbar().snackBar(this,view,"This App is Disabled", 1);
        } else{
            DbConnection database = Room.databaseBuilder(getApplicationContext(), DbConnection.class, "Apps").allowMainThreadQueries().build();
            database.getAppDao().insert(new AppPackage((String) app.getLabel()));
            ConstraintLayout view = findViewById(R.id.activity_setting);
            new PopupSnackbar().snackBar(this, view, "App Disable Successful", 0);
            loadDisabledApps();
        }
    }

    //<editor-fold desc="Private Methods">

    //Setup UI
    private void init() {
        //Load disabled apps
        loadDisabledApps();

        //Open add disable apps
        TextView addButton = findViewById(R.id.disable);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open add disable apps alert
                openAddDisableAppsAlert();
            }
        });

        //Load system apps
        loadApps();

        //Display Disabled apps
        displayApps(disabledApps);

    }

    //Load disabled apps
    private void loadDisabledApps() {
        DbConnection database = Room.databaseBuilder(getApplicationContext(), DbConnection.class, "Apps").allowMainThreadQueries().build();
        disabledApps = database.getAppDao().getData();

        //Display disable apps
        displayApps(disabledApps);
    }

    //Display disable apps
    private void displayApps(List<AppPackage> list) {
        for(int i=0;  i<list.size(); i++){
            for(int j=0; j<appList.size(); j++){
                if(list.get(i).getName().equals(appList.get(j).getLabel())){
                    disabledAppList.add(appList.get(j));
                }
            }
        }
        GridViewAdapter gridViewAdapter = new GridViewAdapter(disabledAppList,this,this);
        GridView appsView = findViewById(R.id.appGrid);
        appsView.setAdapter(gridViewAdapter);
    }

    //Open add disable apps alert
    private void openAddDisableAppsAlert() {
        //Configure alert dialog
        disableApps = new Dialog(this);
        disableApps.requestWindowFeature(Window.FEATURE_NO_TITLE);
        disableApps.setCancelable(false);
        disableApps.setContentView(R.layout.add_disable_apps);
        disableApps.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int j = 0; j<disabledApps.size(); j++){
            for(int k=0; k<appList.size(); k++){
                if(appList.get(k).getLabel().equals(disabledApps.get(j).getName())){
                    appList.get(k).setDisable(true);
                }
            }
        }
        GridViewAdapter gridViewAdapter = new GridViewAdapter(appList,this,this);
        GridView appsView = disableApps.findViewById(R.id.appGrid);
        appsView.setAdapter(gridViewAdapter);
        disableApps.show();

       //Close dialog
        disableApps.findViewById(R.id.closeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableApps.dismiss();
            }
        });
    }

    //Load apps from system
    private void loadApps() {
        PackageManager packageManager = getPackageManager();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        @SuppressLint("QueryPermissionsNeeded") List<ResolveInfo> availableActivities = packageManager.queryIntentActivities(i,0);
        for(ResolveInfo resolveInfo: availableActivities){
            Apps newApp = new Apps();
            newApp.setLabel(resolveInfo.activityInfo.packageName);
            newApp.setName(resolveInfo.loadLabel(packageManager));
            newApp.setIcon(resolveInfo.loadIcon(packageManager));
            newApp.setDisable(false);
            appList.add(newApp);
        }
    }

    //</editor-fold>
}