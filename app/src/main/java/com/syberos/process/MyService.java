package com.syberos.process;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.IBinder;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MyService extends Service implements View.OnTouchListener {
    private View windowView;
    private Button button;
    private WindowManager windowManager;
    private GestureDetector gestureDetector;
    private WindowManager.LayoutParams layoutParams;
    private boolean start = true;
    private File signal = new File("/sdcard/game");

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(android.R.drawable.ic_btn_speak_now);
        builder.setContentText("text");
        builder.setContentTitle("title");
        startForeground(1, builder.getNotification());
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowView = LayoutInflater.from(this).inflate(R.layout.window, null);
        button = windowView.findViewById(R.id.button);
        button.setOnTouchListener(this);
        layoutParams = new WindowManager.LayoutParams(-2,
                                                      -2,
                                                      WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                                                      WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                                      PixelFormat.RGBA_8888);
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        windowManager.addView(windowView, layoutParams);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                toggle();
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                layoutParams.x = (int) (e2.getRawX() - windowView.getWidth() / 2);
                layoutParams.y = (int) (e2.getRawY() - windowView.getHeight() / 2);
                windowManager.updateViewLayout(windowView, layoutParams);
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });

        new NetTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private void toggle() {
        if (start) {
            button.setText("暂停");
            if (!signal.exists()) {
                try {
                    signal.createNewFile();
                } catch (IOException e) {
                }
            }
        } else {
            button.setText("开始");
            if (signal.exists()) signal.delete();
        }
        start = !start;
    }

    private class NetTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                Thread.sleep(10 * 60 * 1000);
            } catch (Exception e) {
            }
            String result = "1";
            HashMap<String, String> map = new HashMap<>();
            map.put("opr", "pwdLogin");
            map.put("userName", "liangtiangang");
            map.put("pwd", "2wsx1qaz");
            map.put("rememberPwd", "0");
            try {
                HttpRequest post = HttpRequest.post("http://1.1.1.3/ac_portal/login.php", map, true);
                result = String.valueOf(post.code());
            } catch (Exception e) {
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            CharSequence old = button.getText();
            if (old.length() > 1) {
                button.setText(String.format("%s(%s)", old.subSequence(0, 2), s));
            } else {
                button.setText(s);
            }
            new NetTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }


}
