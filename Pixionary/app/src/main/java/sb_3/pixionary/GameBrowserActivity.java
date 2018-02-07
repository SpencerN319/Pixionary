package sb_3.pixionary;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


public class GameBrowserActivity extends AppCompatActivity {

    String[] items;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_browser);
        context = this;
        ListView games = (ListView) findViewById(R.id.gameList);
        games.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)  {

            }
        });

        items = getResources().getStringArray(R.array.games);

        ListAdapter adapter = new ListAdapter(context, R.layout.child_listview,R.id.textForBox, items);

        games.setAdapter(adapter);

    }

    public void clickMe(View view) {
        Button btn = (Button) view;

        //Some threading to communicate to the server that a player has joined the game.
        Intent i  = new Intent(GameBrowserActivity.this, PlayActivity.class);
        startActivity(i);

    }


}


