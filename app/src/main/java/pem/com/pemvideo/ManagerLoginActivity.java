package pem.com.pemvideo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pem.com.pemvideo.a1.R;

public class ManagerLoginActivity extends Activity {

    private Button managerbut;
    private EditText managername;
    private EditText managerpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_manager_login);
        init();
        initAllEvent();
    }
    private void init(){
        managername = (EditText) findViewById(R.id.managername);
        managerpwd = (EditText) findViewById(R.id.managerpwd);
        managerbut = (Button) findViewById(R.id.managerbut);
    }
    public void initAllEvent() {
        managerbut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = managername.getText().toString();
                String pwd = managerpwd.getText().toString();
                if (name.trim().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            R.string.managernameisnull, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!name.trim().equals("admin")) {
                    Toast.makeText(getApplicationContext(),
                            R.string.zhanghaoerror, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!pwd.trim().equals("123456")) {
                    Toast.makeText(getApplicationContext(),
                            R.string.pwderror, Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(ManagerLoginActivity.this,ConfigurationActivity.class);
                ManagerLoginActivity.this.startActivity(intent);
                finish();
            }
        });
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ManagerLoginActivity.this,
                    Waiting.class);
            ManagerLoginActivity.this.startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
