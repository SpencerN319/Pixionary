package sb_3.pixionary.SharedSettings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import sb_3.pixionary.MainMenuActivity;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.AdminSettings.RequestAddCategory;
import sb_3.pixionary.Utilities.POJO.User;

public class AddCategory extends Activity{

    Button create;
    CheckBox confirm;
    EditText name;
    RequestQueue requestQueue;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        final ProgressDialog pd = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(AddCategory.this);
        create = (Button) findViewById(R.id.bt_create);
        create.setClickable(false);
        confirm = (CheckBox) findViewById(R.id.cb_confirm);
        name = (EditText) findViewById(R.id.et_name);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(confirm.isChecked()){
                    create.setClickable(true);
                } else {
                    create.setClickable(false);
                }
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = MainMenuActivity.user;
                String category_name = name.getText().toString();
                if(validate_name(category_name)){
                    RequestAddCategory addCategory = new RequestAddCategory(user.getUsername(), user.getPassword(), category_name, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("success")){
                                setResult(1);
                                finish();
                            } else {
                                pd.setTitle("Error");
                                pd.setMessage("category not created");
                                pd.setCancelable(true);
                                pd.show();
                            }
                        }
                    }); requestQueue.add(addCategory);

                }

            }
        });

    }

    private boolean validate_name(String name){
        if(name == null){
            return false;
        } else if(name.length() > 20){
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Error");
            pd.setMessage("Category name cannot exceed 20 characters");
            pd.show();
            return false;
        } else if(name.length() < 3){
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Error");
            pd.setMessage("category name must be a minimum of 3 characters");
            pd.show();
            return false;
        } else {
            return true;
        }
    }
}
