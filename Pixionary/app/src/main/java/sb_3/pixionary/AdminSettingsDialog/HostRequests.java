package sb_3.pixionary.AdminSettingsDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.AdminSettings.RequestHosts;
import sb_3.pixionary.Utilities.AdminSettings.RequestMakeHost;

public class HostRequests extends Activity {

    private RequestQueue requestQueue;
    private Button accept;
    private CheckedTextView[] users = new CheckedTextView[5];
    private String[] usernames = new String[5];
    private ImageView check0, check1, check2, check3, check4;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_requests);

        dialog = new ProgressDialog(this);
        accept = (Button) findViewById(R.id.bt_accept);
        users[0] = (CheckedTextView) findViewById(R.id.tv_0);
        users[1] = (CheckedTextView) findViewById(R.id.tv_1);
        users[2] = (CheckedTextView) findViewById(R.id.tv_2);
        users[3] = (CheckedTextView) findViewById(R.id.tv_3);
        users[4] = (CheckedTextView) findViewById(R.id.tv_4);
        check0 = (ImageView) findViewById(R.id.iv_0);
        check1 = (ImageView) findViewById(R.id.iv_1);
        check2 = (ImageView) findViewById(R.id.iv_2);
        check3 = (ImageView) findViewById(R.id.iv_3);
        check4 = (ImageView) findViewById(R.id.iv_4);

        for(CheckedTextView user: users){
            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.tv_0:
                            if(!(users[0].isChecked())){
                                users[0].setChecked(true);
                                check0.setVisibility(View.VISIBLE);
                            } else {
                                users[0].setChecked(false);
                                check0.setVisibility(View.INVISIBLE);
                            }
                            break;
                            case R.id.tv_1:
                                if(!(users[1].isChecked())){
                                    users[1].setChecked(true);
                                    check1.setVisibility(View.VISIBLE);
                                } else {
                                    users[1].setChecked(false);
                                    check1.setVisibility(View.INVISIBLE);
                                }
                                break;
                            case R.id.tv_2:
                                if(!(users[2].isChecked())){
                                    users[2].setChecked(true);
                                    check2.setVisibility(View.VISIBLE);
                                } else {
                                    users[2].setChecked(false);
                                    check2.setVisibility(View.INVISIBLE);
                                }
                                break;
                            case R.id.tv_3:
                                if(!(users[3].isChecked())){
                                    users[3].setChecked(true);
                                    check3.setVisibility(View.VISIBLE);
                                } else {
                                    users[3].setChecked(false);
                                    check3.setVisibility(View.INVISIBLE);
                                }
                                break;
                            case R.id.tv_4:
                                if(!(users[4].isChecked())){
                                    users[4].setChecked(true);
                                    check4.setVisibility(View.VISIBLE);
                                } else {
                                    users[4].setChecked(false);
                                    check4.setVisibility(View.INVISIBLE);
                                }
                                break;
                    }
                }
            });
        }


        requestQueue = Volley.newRequestQueue(this);

        pull_requests();

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setTitle("Updating");
                dialog.setMessage("Update in progress please wait");
                dialog.setCancelable(false);
                for(int i = 0; i < users.length; i++){
                    if(users[i].isChecked()){
                        RequestMakeHost grant_host = new RequestMakeHost(usernames[i], new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("success")){
                                    //TODO something maybe nothing here?
                                    Log.i("RESPONSE", response);
                                } else{
                                }
                            }
                        }); requestQueue.add(grant_host);
                    }
                }
                pull_requests();
                dialog.cancel();
            }
        });
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
                            users[i].setClickable(true);
                            usernames[i] = hosts.getString(i);
                            if(hosts.length() < 5){
                                for(int j = hosts.length(); j < 5; j++){
                                    users[j].setText("");
                                    users[j].setVisibility(View.INVISIBLE);
                                    users[j].setClickable(false);
                                    usernames[i] = "";
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
