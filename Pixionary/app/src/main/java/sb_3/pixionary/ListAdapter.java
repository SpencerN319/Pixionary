package sb_3.pixionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fastn on 2/5/2018.
 */

public class ListAdapter extends ArrayAdapter<String> {

    int id;
    String[] items;
    ArrayList<String> itemArrList;
    Context context;

    public ListAdapter(Context context,int vg, int id, String[] items) {
        super(context, vg, id, items);
        this.id = vg;
        this.items = items;
        this.context = context;
    }

    static class ViewHolder {
        public TextView text;
        public Button btn;
    }

    public View getView(int position, View convert, ViewGroup parent) {
        View view = convert;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(id, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.textForBox);
            viewHolder.btn = (Button) view.findViewById(R.id.btn);
            view.setTag(viewHolder);
        }

        //Set text for each view.
        ViewHolder hold = (ViewHolder) view.getTag();
        hold.text.setText(items[position]);
        hold.btn.setText("Play!");

        return view;
    }
}