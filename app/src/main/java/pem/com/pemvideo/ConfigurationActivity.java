package pem.com.pemvideo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import pem.com.pemvideo.a1.R;


public class ConfigurationActivity extends Activity {
    private Button ip;

    private Button fanhui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_configuration);
        init();
        initAllEvent();
    }

    private void init(){
        ip = (Button) findViewById(R.id.ip);
        fanhui = (Button)findViewById(R.id.fanhui);
    }
   
    public void initAllEvent() {
        ip.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ConfigurationActivity.this,  InitIpActivity.class);
                startActivity(intent);
                finish();
            }

        });
        fanhui.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ConfigurationActivity.this,  Waiting.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK ){
            Intent intent = new Intent();
            intent.setClass(ConfigurationActivity.this,  Waiting.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
