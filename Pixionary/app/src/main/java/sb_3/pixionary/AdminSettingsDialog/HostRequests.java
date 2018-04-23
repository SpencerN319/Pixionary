package sb_3.pixionary.AdminSettingsDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.AdminSettings.RequestHosts;
import sb_3.pixionary.Utilities.AdminSettings.RequestMakeHost;

public class HostRequests extends Activity {

    private RequestQueue requestQueue;
    private TextView[] users = new TextView[5];
    private ImageView[] checkmarks = new ImageView[5];
    private String[] usernames = new String[5];
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_requests);
        requestQueue = Volley.newRequestQueue(this);
        dialog = new ProgressDialog(this);
        users[0] = (TextView) findViewById(R.id.tv_0);
        users[1] = (TextView) findViewById(R.id.tv_1);
        users[2] = (TextView) findViewById(R.id.tv_2);
        users[3] = (TextView) findViewById(R.id.tv_3);
        users[4] = (TextView) findViewById(R.id.tv_4);

        checkmarks[0] = (ImageView) findViewById(R.id.iv_0);
        checkmarks[1] = (ImageView) findViewById(R.id.iv_1);
        checkmarks[2] = (ImageView) findViewById(R.id.iv_2);
        checkmarks[3] = (ImageView) findViewById(R.id.iv_3);
        checkmarks[4] = (ImageView) findViewById(R.id.iv_4);

        pull_requests();

        for(ImageView check: checkmarks){
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                            case R.id.iv_0:
                                accept(0);
                                break;
                            case R.id.iv_1:
                                accept(1);
                                break;
                            case R.id.iv_2:
                                accept(2);
                                break;
                            case R.id.iv_3:
                                accept(3);
                                break;
                            case R.id.iv_4:
                                accept(4);
                                break;
                    }
                }
            });
        }
    }

    private void accept(int user_num){
        RequestMakeHost grant_host = new RequestMakeHost(usernames[user_num], new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.setTitle("Updating");
                dialog.setMessage("Update in progress please wait");
                dialog.show();
                dialog.setCancelable(false);
                if(!(response.equals("success"))){ Log.i("RESPONSE ", response); }

            }
        });
        requestQueue.add(grant_host);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                pull_requests();
                dialog.cancel();
                timer.cancel();
            }
        }, 1000);
    }

    private void pull_requests(){
        RequestHosts requestHosts = new RequestHosts(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("success")){
                        JSONArray hosts = data.getJSONArray("hosts");
                        for(int i = 0; i < hosts.length(); i++){
                            users[i].setText(hosts.getString(i));
                            users[i].setVisibility(View.VISIBLE);
                            usernames[i] = hosts.getString(i);
                            checkmarks[i].setVisibility(View.VISIBLE);
                            if(hosts.length() < 5){
                                for(int j = hosts.length(); j < 5; j++){
                                    users[j].setText("");
                                    users[j].setVisibility(View.INVISIBLE);
                                    usernames[j] = "";
                                    checkmarks[j].setClickable(false);
                                    checkmarks[j].setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }); requestQueue.add(requestHosts);
    }


}
