package pem.com.pemvideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pem.com.pemvideo.a1.R;

public class InitIpActivity extends Activity {
    private Button shezhi;
    private Button tuichu;
    private EditText ip;
    private TextView text1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_ip);
        init();
        initAllEvent();
    }
   
    private void init(){
        shezhi = (Button) findViewById(R.id.shezhi);
        ip = (EditText) findViewById(R.id.ip);
        tuichu = (Button) findViewById(R.id.tuichu);
        text1 = (TextView) findViewById(R.id.text1);

        SharedPreferences sp1 = getSharedPreferences("urlFinal", Context.MODE_PRIVATE);
        String ip1 = sp1.getString("ip", null);
        if(ip1 != null ){
            ip.setText(ip1);
        }
    }
 
    public void initAllEvent() {
        shezhi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String ips = ip.getText().toString();
                if (ips.trim().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            R.string.ipisnull, Toast.LENGTH_LONG).show();
                    return;
                }
               

                PropertiesUtil.getProperties().setProperty("ip", ips);
                SharedPreferences sp = getSharedPreferences("urlFinal", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("ip", ips);
                editor.commit();

                SharedPreferences sp1 = getSharedPreferences("urlFinal", Context.MODE_PRIVATE);
                String ip1 = sp1.getString("ip", null);
                String ports1 = sp1.getString("port", null);
                text1.setText(ip1);
                Toast.makeText(getApplicationContext(),
                        R.string.shezhisucess, Toast.LENGTH_LONG).show();
                return;

            }
        });
        tuichu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(InitIpActivity.this,  ConfigurationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           
            Intent intent = new Intent();
            intent.setClass(InitIpActivity.this,  Waiting.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
