package pe.chaskihero.chaskihero;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Map;

public class Notificacion {

    private static Notificacion instance=null;
    private static Context mContext;

    private NotificationManager mNotificationManager;
    private int NOTIFICATION_ID = 0;

    public static Notificacion getInstance(Context contexto){
        if(Notificacion.instance==null){
            Notificacion.instance=new Notificacion(contexto);
        }
        return Notificacion.instance;
    }

    public Notificacion(Context contexto){
        this.mContext=contexto;
    }

    public void sendNotification(final String title, final String body, final Map<String,String> message) {
        try{
            Log.i("INFO","title "+ title+", body "+ body);
            //Log.i("INFO","message "+ message.toString());
            mNotificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            //Log.i("INFO","Ingresa a mostrar notificacion");
            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),
                    R.mipmap.ic_launcher);
            //AudioManager am = (AudioManager) mContext.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            Intent intent = new Intent(mContext, MainActivity.class);
            //intent.putExtra("data_message", new Gson().toJson(message));
            //intent.putExtra("title",title);
            //intent.putExtra("body",body);
            //intent.putExtra("notify",true);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            /*TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(intent);
            PendingIntent contentIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);*/
            //PendingIntent contentIntent = PendingIntent.getActivity(mContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                    .setLargeIcon(icon)
                    .setSmallIcon(R.mipmap.ic_launcher,2)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(body))
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentText(body);
            mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            ringtone();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //Log.i("INFO","termina normal???");
        }
    }

    public void ringtone() {
        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            MediaPlayer player = MediaPlayer.create(mContext, notification);
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}