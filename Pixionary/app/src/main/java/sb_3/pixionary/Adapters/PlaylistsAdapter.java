package sb_3.pixionary.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

import sb_3.pixionary.gameplay.LobbyActivity;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.GameClasses.Playlist;

/**
 * Created by fastn on 3/12/2018.
 */

//TODO Add a functionality to the preview button where it will display a preview image of the playlist.
public class PlaylistsAdapter extends BaseAdapter implements android.widget.ListAdapter {

    ArrayList<Playlist> items;
    Context context;

    public PlaylistsAdapter(Context context, ArrayList<Playlist> items) {
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
        return items.get(position).getId();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.categories_list_view, null);
        }

        //Set the name of the playlist.
        TextView tvPlaylistName = (TextView) view.findViewById(R.id.textPlaylistName);
        tvPlaylistName.setText(items.get(position).getName());

        //Set the creator of the playlist.
        TextView tvPlaylistCreator = (TextView) view.findViewById(R.id.textPlaylistCreator);
        tvPlaylistCreator.setText(items.get(position).getCreator());

        Button previewBtn = (Button) view.findViewById(R.id.preview);
        Button startBtn = (Button) view.findViewById(R.id.play);

        previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //should start dialog that will display a requested image from server.
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHostWaitScreen(position);
            }
        });

        return view;
    }

    private void startHostWaitScreen(int position) {
        Intent intent = new Intent(context, LobbyActivity.class);
        Bundle gameAccess = new Bundle();
        gameAccess.putInt("id", items.get(position).getId());
        intent.putExtras(gameAccess);
        context.startActivity(intent);
        ((Activity)context).finish();
    }
}