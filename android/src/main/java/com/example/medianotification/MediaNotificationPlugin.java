package com.example.medianotification;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/** MediaNotificationPlugin */
public class MediaNotificationPlugin implements MethodCallHandler {
    private static final String CHANNEL_ID = "media_notification";
    private static Registrar registrar;
    private static NotificationPanel nPanel;
    private static MethodChannel channel;

    private MediaNotificationPlugin(Registrar r) {
        registrar = r;
    }

    /** Plugin registration. */
    public static void registerWith(Registrar registrar) {
      MediaNotificationPlugin plugin = new MediaNotificationPlugin(registrar);

      MediaNotificationPlugin.channel = new MethodChannel(registrar.messenger(), "media_notification");
      MediaNotificationPlugin.channel.setMethodCallHandler(new MediaNotificationPlugin(registrar));
    }

 @Override
 public void onMethodCall(MethodCall call, Result result) {
      switch (call.method) {
          case "show":
              final String title = call.argument("title");
              final String author = call.argument("author");
              final boolean play = call.argument("play");
              final String backgroundColor = call.argument("backgroundColor");
              final String color = call.argument("color");
              show(title, author,backgroundColor,color, play);
              result.success(null);
              break;
            case "hide":
              hide();
              result.success(null);
              break;
          default:
              result.notImplemented();
      }
  }

  public static void callEvent(String event) {

      MediaNotificationPlugin.channel.invokeMethod(event, null, new Result() {
          @Override
          public void success(Object o) {
              // this will be called with o = "some string"
          }

          @Override
          public void error(String s, String s1, Object o) {}

          @Override
          public void notImplemented() {}
      });
  }

  public static void show(String title, String author, String backgroudColor, String color, boolean play) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          int importance = NotificationManager.IMPORTANCE_DEFAULT;
          NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
          channel.enableVibration(false);
          channel.setSound(null, null);
          NotificationManager notificationManager = registrar.context().getSystemService(NotificationManager.class);
          notificationManager.createNotificationChannel(channel);
      }

      nPanel = new NotificationPanel(registrar.context(), title, author,backgroudColor, color, play);
  }

  private void hide() {
      nPanel.notificationCancel();
  }
}




