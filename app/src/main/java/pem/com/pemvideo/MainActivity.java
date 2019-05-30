package pem.com.pemvideo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import pem.com.pemvideo.a1.R;

public class MainActivity extends Activity {
		private VideoView videoView;
        public static String ip;
		private ListView listView;
        String userid;
		String parm;
		String model;
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
	    public void onCreate(Bundle savedInstanceState) {
            SharedPreferences sp2 = getSharedPreferences("urlFinal", Context.MODE_PRIVATE);
			ip = sp2.getString("ip", null);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        super.onCreate(savedInstanceState);

	        setContentView(R.layout.activity_main);



			listView=(ListView) findViewById(R.id.listView);

			try {
				videoplay();
			} catch (JSONException e) {
				e.printStackTrace();
			};
			check();
	    };
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        finish();
        super.onPause();
    }

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public void videoplay() throws JSONException {
		Bundle bundle = this.getIntent().getExtras();
		parm = bundle.getString("parm");
		model = bundle.getString("model");
		JSONObject object = new JSONObject(parm);

		userid = object.getString("u_id");
		JSONObject obj = new JSONObject(object.getString("vid_name"));
		Iterator<?> it = obj.keys();
		String videotitle = "";
		String videolist = null;
		final List list = new ArrayList();

		final List listtitle = new ArrayList();

		while(it.hasNext()){
			videolist = (String) it.next().toString();
			videotitle = obj.getString(videolist);
			//listtitle.add(videotitle);

			if(model.equals("true")){
                videolist = videolist.replace("./","/");
            }
//			if(videolist.indexOf("img/_____") > -1){
//				String [] videolists =  videolist.replace("img/_____","vid/"+videotitle+".mp4?code=").split("\\?");
//				list.add(videolists[0]);
//			}else{
//				list.add(videolist);
//			}
			ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.array_adapter, listtitle);
			listView.setAdapter(adapter);
		}
		listtitle.add("充分整合的冥想");
		listtitle.add("大爱冥想");
		listtitle.add("莲花冥想");
		listtitle.add("脉轮冥想");
		list.add("/sdcard/Android/data/1.mp4");
		list.add("/sdcard/Android/data/2.mp4");
		list.add("/sdcard/Android/data/3.mp4");
		list.add("/sdcard/Android/data/4.mp4");

		final int[] num = {0};
		final int[] nums = {0};
		final int[] sibnum = {0};
        Uri uri;
        if(model.equals("true")){
		    uri = Uri.parse(getUSBPaths(getApplicationContext())+list.get(num[0]));
        }else{
            uri = Uri.parse(ip+list.get(num[0]));
        }
		uri = Uri.parse("/sdcard/Android/data/1.mp4");
        //uri = Uri.parse("/sdcard/Android/data/smmx.mp4");
		videoView = (VideoView)this.findViewById(R.id.void1);
		videoView.setMediaController(new MediaController(this));
		videoView.setVideoURI(uri);
		videoView.requestFocus();
		videoView.start();
		initlist(0);
		videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				return false;
			}
		});
		videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				listView.getChildAt(sibnum[0]++).setBackgroundColor(0x30000000);
				initlist(++nums[0]);
				if(list.size()-1 > num[0]){
//					if(model.equals("true")){
//						videoView.setVideoURI(Uri.parse(getUSBPaths(getApplicationContext())+list.get(++num[0])));
//					}else{
//						videoView.setVideoURI(Uri.parse(ip+list.get(++num[0])));
//					}
					videoView.setVideoURI(Uri.parse(""+list.get(++num[0])));
					videoView.start();
				}else{
                    Runnable runnables = new Runnable() {
                        public void run() {
                        while (true) {
                            try {
                                requestuserstatu(userid);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        }
                    };
					Thread thread = new Thread(runnables);
					thread.start();
				}
			}
		});
    }
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
    public void initlist(final int nums){
		listView.post(new Runnable() {
			@Override
			public void run() {
				listView.getChildAt(nums).setMinimumWidth(250);
				listView.getChildAt(nums).setBackgroundColor(0xFF3197FF);
			}
		});
	}
	public void check() {
		final long timeInterval = 2000;
		Runnable runnable = new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(timeInterval);
						String user = requestuser();
						if(null != user || user.equals("")){
							if(user.equals("not video")){
								Intent intent = new Intent( getApplicationContext(), Waiting.class);
								startActivity(intent);
								finish();
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
	};
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
	public void requestuserstatu(String userid) throws JSONException {
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		String line = null;
		String result = "";
		try {
			URL url = new URL(ip+"index.php/index/index/trainend.html?u_id="+userid);
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
		if(result.equals("1")){
			Intent intent = new Intent(this, Waiting.class);
			startActivity(intent);
			finish();
		}else{
			Toast.makeText(this,"提交记录失败",Toast.LENGTH_LONG).show();
		}
		return;
	}
}
