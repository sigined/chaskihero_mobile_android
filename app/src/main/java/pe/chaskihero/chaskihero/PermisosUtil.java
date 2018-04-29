package pe.chaskihero.chaskihero;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

public class PermisosUtil {

    public static final int REQUEST_CODE_READ_PERMISSION = 22;
    public static final int PERMISSION_CAMERA = 85;
    public static final int PERMISSION_SMS = 45;
    public static final int PERMISSION_LOCATION = 50;
    public static final int PERMISSION_ALL = 100;

    public static boolean hasRealExternalPermission(){
        return ActivityCompat.checkSelfPermission(MyApplication.getContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void askForGalleryPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_READ_PERMISSION);
    }

    public static boolean hasCameraPermission() {
        return ActivityCompat.checkSelfPermission(MyApplication.getContext(),
                android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void askForCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{android.Manifest.permission.CAMERA},
                PERMISSION_CAMERA);
    }

    public static boolean hasPhonePermission() {
        return ActivityCompat.checkSelfPermission(MyApplication.getContext(),
                android.Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void askForPhonePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{android.Manifest.permission.CALL_PHONE},
                PERMISSION_SMS);
    }

    public static boolean hasSMSPermission() {
        return ActivityCompat.checkSelfPermission(MyApplication.getContext(),
                android.Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void askForSMSPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{android.Manifest.permission.SEND_SMS},
                PERMISSION_SMS);
    }

    public static boolean hasLocationPermission(){
        return ( ActivityCompat.checkSelfPermission(MyApplication.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED);
    }

    public static void askLocationPermission(Activity activity){
        ActivityCompat.requestPermissions(activity,
                new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION },
                PERMISSION_LOCATION);
    }

    public static boolean askCheckLocationPermission(final Activity activity){
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(activity)
                        .setTitle("Permiso requerido")
                        .setMessage("Esta aplicación necesita acceder a tu ubicación para mostrar las entregas en el mapa")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSION_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public static boolean hasForAllPermission(){
        if( hasRealExternalPermission() && hasPhonePermission() && hasSMSPermission() && hasLocationPermission() ){
            return true;
        }
        else return  false;
    }

    public static void askForAllPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{ android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.SEND_SMS, android.Manifest.permission.CALL_PHONE,
                        android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION },
                PERMISSION_ALL);
    }

}
