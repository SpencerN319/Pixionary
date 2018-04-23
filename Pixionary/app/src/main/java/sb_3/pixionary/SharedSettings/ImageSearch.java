package sb_3.pixionary.SharedSettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import sb_3.pixionary.R;

public class ImageSearch extends Activity {

    private EditText word;
    private Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);

        word = (EditText) findViewById(R.id.et_search);
        search = (Button) findViewById(R.id.bt_search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key_word = word.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("word", key_word);
                setResult(3,intent);
                finish();
            }
        });
    }
}
