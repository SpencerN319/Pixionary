package sb_3.pixionary.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.GameClasses.Playlist;
import sb_3.pixionary.interfaces.DataTransferInterface;

/**
 * Created by fastn on 3/12/2018.
 */

//TODO Add a functionality to the preview button where it will display a preview image of the playlist.
public class PlaylistsAdapter extends BaseAdapter implements android.widget.ListAdapter {

    DataTransferInterface dtInterface;

    private ArrayList<Playlist> items;
    private Context context;

    public PlaylistsAdapter(Context context, ArrayList<Playlist> items, DataTransferInterface dtInterface) {
        this.items = items;
        this.context = context;
        this.dtInterface = dtInterface;
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
        return -1;
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

        Button startBtn = (Button) view.findViewById(R.id.play);


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dtInterface.setValuesAndReact(position);
            }
        });

        return view;
    }


//    private void returnPlaylistAndFinish(int position) {
//        Intent intent = new Intent(context, HostGameActivity.class);
//        intent.putExtra("PlaylistName", items.get(position).getName());
//        ((Activity)context).setResult(Activity.RESULT_OK, intent);
//        ((Activity)context).finish();
//    }
//
//    private void startHostWaitScreen(int position) {
//        Intent intent = new Intent(context, GameActivity.class);
//        Bundle gameAccess = new Bundle();
//        gameAccess.putInt("id", items.get(position).getId());
//        intent.putExtras(gameAccess);
//        context.startActivity(intent);
//        ((Activity)context).finish();
//    }
}