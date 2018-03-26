package sb_3.pixionary.AdminSettingsDialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import sb_3.pixionary.R;

public class ViewSelectedUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_view_selected_user);
    }
}
