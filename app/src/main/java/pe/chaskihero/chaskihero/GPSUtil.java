package pe.chaskihero.chaskihero;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

public class GPSUtil {

    private Activity activity;

    public GPSUtil(Activity activity) {
        this.activity = activity;
    }

    public boolean checkGPSEnabled() {
        boolean isGPSEnabled = false;
        LocationManager locationManager;
        try {
            locationManager = (LocationManager) activity
                    .getSystemService(Service.LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isGPSEnabled;
    }

    public void showSettingsGPS(@StringRes int content) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(activity)
                .title("GPS")
                .content(content)
                .titleGravity(GravityEnum.CENTER)
                .backgroundColorRes(R.color.colorBackgroundDialog)
                .titleColorRes(R.color.colorTitleDialog)
                .contentColorRes(R.color.color8080)
                .contentGravity(GravityEnum.CENTER)
                .positiveText("OK")
                .negativeText("Cancelar")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivity(intent);
                        dialog.dismiss();
                    }
                });
        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    public Location getLastPosition(){
        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
            return null;
        }

        LocationManager locManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(loc==null ) {
            loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(loc == null ){
                loc = locManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }
        }
        return loc;
    }

}