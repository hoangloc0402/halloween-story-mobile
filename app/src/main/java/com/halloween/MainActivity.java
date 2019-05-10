package com.halloween;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.halloween.GameScreens.GamePanel;

public class MainActivity extends Activity {

//    private static GamePanel gamePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Constants.CURRENT_CONTEXT = getApplicationContext();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Constants.SCREEN_WIDTH = displayMetrics.widthPixels;
        Constants.SCREEN_HEIGHT = displayMetrics.heightPixels;
        Constants.MAIN_ACTIVITY = this;

//        setContentView(R.layout.activity_main);
//
//        Thread myThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final GamePanel gamePanel = new GamePanel(Constants.MAIN_ACTIVITY);
//                Constants.MAIN_ACTIVITY.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Constants.MAIN_ACTIVITY.setContentView(gamePanel);
//                        getWindow().getDecorView().getRootView().invalidate();
//                    }
//                });
//            }
//        });
//        myThread.start();
        GamePanel gamePanel = new GamePanel(this);
        setContentView(gamePanel);
//        setContentView(R.layout.activity_main);
//        while (true) {
//            Log.d("game", Constants.THREAD_IS_RUNNING + "");
//            if (Constants.THREAD_IS_RUNNING) {
//                setContentView(gamePanel);
//                break;
//            }
//        }
    }

    @Override
    protected void onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
}
