package sb_3.pixionary.AdminSettingsDialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import sb_3.pixionary.R;

public class AddCategory extends AppCompatActivity {

    Button create;
    CheckBox confirm;
    EditText name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

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
                //TODO create an add category request and makedir in images
                finish();
            }
        });

    }
}
