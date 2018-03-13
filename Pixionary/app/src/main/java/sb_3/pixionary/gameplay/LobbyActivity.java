package sb_3.pixionary.gameplay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import sb_3.pixionary.R;

public class LobbyActivity extends AppCompatActivity {

    TextView id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_wait);

        id = (TextView) findViewById(R.id.displayTEMP);

        Bundle bundle = getIntent().getExtras();
        int value = -1;
        if (bundle != null) {
            value = bundle.getInt("id");
        }
        id.setText(String.valueOf(value));

    }
}
