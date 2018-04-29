package pe.chaskihero.chaskihero;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    //SharedPreferences utilpreferences;
    //Reciclador reciclador;

    //INotificationPresenter iNotificationPresenter;

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
    }

    public void registerToken(final String token) {
        /*
        iNotificationPresenter = new NotificationPresenter();
        try{
            utilpreferences = UtilPreferences.getDefaultPreferences(MvpApp.getContext());
            reciclador = new Gson().fromJson(
                    UtilPreferences.findStringPreference(utilpreferences, UtilPreferences.PREFERENCES_USER_TEMPORARY,""),
                    Reciclador.class);
            Map<String, String> pushAndroid = new HashMap<String,String>();
            pushAndroid.put(ExtraUtil.getUniqueID(),token);

            iNotificationPresenter.updateToken(pushAndroid);
        }catch (Exception e){
            e.printStackTrace();
        }
        */

    }

}