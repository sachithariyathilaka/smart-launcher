package io.github.sachithariyathilaka.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import com.sachith.popupsnackbar.PopupSnackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.github.sachithariyathilaka.R;
import io.github.sachithariyathilaka.adapter.GridViewAdapter;
import io.github.sachithariyathilaka.database.DbConnection;
import io.github.sachithariyathilaka.model.AppPackage;
import io.github.sachithariyathilaka.model.Apps;
import io.github.sachithariyathilaka.service.ClickListenerService;

/**
 * App list activity class.
 *
 * @author  Sachith Ariyathilaka
 * @version 1.0.0
 * @since   2024/10/04
 */
public class AppListActivity extends AppCompatActivity implements ClickListenerService {

    private PackageManager packageManager;
    private List<Apps> apps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        init();
    }

    @Override
    public void onAppClick(Apps app) {
        if(app.getDisable()){
            ConstraintLayout view = findViewById(R.id.activity_app_list);
            new PopupSnackbar().snackBar(this,view,"This App is Disabled", 1);
        } else{
            startActivity(new Intent((packageManager.getLaunchIntentForPackage(app.getLabel().toString()))));
        }
    }

    /**
     * Initiate UI.
     */
    private void init() {
        loadApps();
        sortApps();
        goToSettings();
    }

    /**
     * Go to settings.
     */
    private void goToSettings() {
        findViewById(R.id.settings).setOnClickListener(v -> startActivity(new Intent(AppListActivity.this,SettingsActivity.class)));
    }

    /**
     * Sort apps.
     */
    private void sortApps() {
        TextView sortText = findViewById(R.id.sort);
        sortText.setOnClickListener(v -> {
            apps.sort(Comparator.comparing((Apps app) -> String.valueOf(app.getName())));
            displayApps();
        });
    }

    /**
     * Load apps.
     */
    private void loadApps() {
        packageManager = getPackageManager();
        apps = new ArrayList<>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        @SuppressLint("QueryPermissionsNeeded") List<ResolveInfo> availableActivities = packageManager.queryIntentActivities(i,0);
        DbConnection database = Room.databaseBuilder(getApplicationContext(), DbConnection.class, "Apps").allowMainThreadQueries().build();
        List<AppPackage> disabledApps = database.getAppDao().getData();

        for(ResolveInfo resolveInfo: availableActivities){
            Apps newApp = new Apps();
            newApp.setLabel(resolveInfo.activityInfo.packageName);
            newApp.setName(resolveInfo.loadLabel(packageManager));
            newApp.setIcon(resolveInfo.loadIcon(packageManager));
            newApp.setDisable(false);
            apps.add(newApp);
        }

        for(int j = 0; j<disabledApps.size(); j++)
            for(int k=0; k<apps.size(); k++)
                if(apps.get(k).getLabel().equals(disabledApps.get(j).getName()))
                    apps.get(k).setDisable(true);

        displayApps();
    }

    /**
     * Display apps.
     */
    private void displayApps() {
        GridViewAdapter gridViewAdapter = new GridViewAdapter(apps,this,this);
        GridView appsView = findViewById(R.id.appGrid);
        appsView.setAdapter(gridViewAdapter);
    }
}