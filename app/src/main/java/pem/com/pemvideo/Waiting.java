package pem.com.pemvideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pem.com.pemvideo.a1.R;

public class Waiting extends Activity {
    public static String ip;
    private RollPagerView mRollViewPager;
    private static String port;
    private boolean model = true;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences sp2 = getSharedPreferences("urlFinal", Context.MODE_PRIVATE);

        ip = sp2.getString("ip", null);
        port = sp2.getString("port", null);

        if(null !=  ip){
            PropertiesUtil.getProperties().setProperty("ip", ip);
        }

        if(null != ip){
           requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            checks();
        }else{
            Intent intent = new Intent(Waiting.this, ConfigurationActivity.class);
            Waiting.this.startActivity(intent);
//            Toast.makeText(getApplicationContext(), R.string.szip, Toast.LENGTH_LONG).show();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_waiting);
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        if(fileIsExists(getUSBPaths(this)+"/u.ini")){
            Toast.makeText(this,"当前播放模式为U盘播放！",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"当前播放模式为局域网播放！",Toast.LENGTH_LONG).show();
            model = false;
        }
        mRollViewPager = (RollPagerView) findViewById(R.id.roll_view_pager);

        mRollViewPager.setPlayDelay(5000);
        mRollViewPager.setAnimationDurtion(1000);

        mRollViewPager.setAdapter(new TestNormalAdapter());
        mRollViewPager.setHintView(new ColorPointHintView(this, Color.YELLOW,Color.WHITE));
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private class TestNormalAdapter extends StaticPagerAdapter {
        private int[] imgs = {
                R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3,
                R.drawable.img4,
        };

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        };

        @Override
        public int getCount() {
            return imgs.length;
        }
    }
    public void checks(){
        final long timeInterval = 2000;
        Runnable runnable = new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(timeInterval);
                        String user = requestuser();
                        if(null != user || user.equals("")){
                            if(!user.equals("not video")){
                                Intent intent = new Intent( getApplicationContext(), MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("parm", user);
                                bundle.putString("model", String.valueOf(model));
                                intent.putExtras(bundle);
                                startActivity(intent);
                                return;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setIconEnable(menu, true);

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private void setIconEnable(Menu menu, boolean enable){
        try{
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            m.invoke(menu, enable);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        Toast.makeText(getApplicationContext(),featureId+" | "+item.getTitle(),Toast.LENGTH_LONG).show();
        switch (item.getItemId()) {
            case R.id.option_item1:
                Intent intent= new Intent(Waiting.this,ManagerLoginActivity.class);
                intent.putExtra("data",item.getTitle());
                item.setIntent(intent);
                finish();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    public static String requestuser() throws JSONException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String line = null;
        String result = "";
        try {
            URL url = new URL(ip+"index.php/index/index/traincontent.html?screen_code="+1);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(60000);
            InputStream in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null){
                if(line != "null") {
                    result += line;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null){
                connection.disconnect();
            }
        }
        return result;
    }
    private long firstTime = 0;
    //@Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return false;
//        }
//
//        return super.onKeyUp(keyCode, event);
//    }
    //判断文件是否存在
    public boolean fileIsExists(String strFile) {
        try {
            File f=new File(strFile);
            if(!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }
    //反射获取路径
    public static String getUSBPaths(Context con) {
        String[] paths = null;
        List data = new ArrayList();
        StorageManager storageManager = (StorageManager) con.getSystemService(Context.STORAGE_SERVICE);
        try {
            paths = (String[]) StorageManager.class.getMethod("getVolumePaths", null).invoke(storageManager,null);
            for (String path : paths) {
                String state = (String) StorageManager.class.getMethod("getVolumeState",String.class).invoke(storageManager, path);
                if (state.equals(Environment.MEDIA_MOUNTED) && !path.contains("emulated")) {
                    data.add(path);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtils.strip(data.toString(),"[]");
    }
}
