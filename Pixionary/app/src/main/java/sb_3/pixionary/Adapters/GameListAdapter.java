package sb_3.pixionary.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.GameClasses.ShortGame;
import sb_3.pixionary.gameplay.GameActivity;

/**
 * Created by fastn on 2/5/2018.
 */

public class GameListAdapter extends BaseAdapter implements android.widget.ListAdapter{

    Context context;
    ArrayList<ShortGame> items;

    public GameListAdapter(Context context, ArrayList<ShortGame> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convert, ViewGroup parent) {
        View view = convert;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.child_listview, null);
        }

        //Set text for Name.
        TextView tvGameName = (TextView) view.findViewById(R.id.textPlaylistName);
        tvGameName.setText(items.get(position).getPlaylist().getName());

        //Set text for host.
        TextView tvHost = (TextView) view.findViewById(R.id.textGameHost);
        tvHost.setText(items.get(position).getHost());

        //TODO Change tvPlayers setup.
        TextView tvPlayers = (TextView) view.findViewById(R.id.textPlayers);
        String text = String.valueOf(items.get(position).getPlayers()) + "/" + String.valueOf(items.get(position).getMax());
        tvPlayers.setText(text);

        Button playBtn = (Button) view.findViewById(R.id.join);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLobbyScreen(position);
            }
        });

        return view;
    }

    private void startLobbyScreen(int position) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra("id", items.get(position).getHost());
        intent.putExtra("gameType", items.get(position).getPlayers());
        intent.putExtra("playlist", items.get(position).getPlaylist().getName());
        context.startActivity(intent);
        ((Activity)context).finish();
    }
}