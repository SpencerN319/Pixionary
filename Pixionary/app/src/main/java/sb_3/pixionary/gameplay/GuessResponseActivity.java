package sb_3.pixionary.gameplay;

import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import sb_3.pixionary.R;

public class GuessResponseActivity extends Activity {

    private TextView timeWait;
    private TextView responseTV;
    private Button exit;
    private boolean canExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guess_response);
        this.setFinishOnTouchOutside(false);

        timeWait = (TextView) findViewById(R.id.tv_time_wait);
        responseTV = (TextView) findViewById(R.id.title_guess);
        exit = (Button) findViewById(R.id.button_close);

        String response = getIntent().getStringExtra("response");
        responseTV.setText(response);
        if (response.equals("Correct!")) {
            //What to display if correct
            canExit = true;
            timeWait.setText("Press OK to continue");
        } else {
            waitDisplay();
        }

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canExit) {
                    finish();
                }
            }
        });
    }

    //TODO This needs to be tested.
    @Override
    public void onBackPressed() {
        //DO nothing.
    }

    private void waitDisplay() {
        new CountDownTimer(3000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeWait.setText("Wait! " + (millisUntilFinished/1000 + 1) + " seconds");
            }

            @Override
            public void onFinish() {
                timeWait.setText("Press OK to continue");
                canExit = true;
            }
        }.start();
    }
}
