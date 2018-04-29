package pe.chaskihero.chaskihero;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private Notificacion notificacion;
    private String title="",body="";
    private Map<String,String> message;

    @Override
    public void onCreate() {
        super.onCreate();
        notificacion = Notificacion.getInstance(getApplicationContext());
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try{
            Log.i("INFO","notificación:: ");
            Log.i("INFO","remoteMessage.getNotification() :: "+remoteMessage.getNotification());
            if (remoteMessage.getNotification() != null) {
                title = remoteMessage.getNotification().getTitle();
                body = remoteMessage.getNotification().getBody();
                message = remoteMessage.getData();
            }
            if (remoteMessage.getData().size() > 0) {
                title = remoteMessage.getData().get("title");
                body = remoteMessage.getData().get("message");
                message = remoteMessage.getData();
            }
            Log.i("INFO"," title "+title);
            Log.i("INFO"," body "+body);
            Log.i("INFO"," message "+message.toString());
            notificacion.sendNotification(title,body,message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}