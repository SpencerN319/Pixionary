package sb_3.pixionary.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sb_3.pixionary.R;

/**
 * Created by fastn on 3/22/2018.
 */

public class GuessListAdapter extends BaseAdapter implements ListAdapter{

    Context context;
    ArrayList<String> possibleWords;

    public GuessListAdapter(Context context, ArrayList<String> items) {
        this.possibleWords = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return possibleWords.size();
    }

    @Override
    public Object getItem(int position) {
        return  possibleWords.get(position);
    }

    @Override
    public long getItemId(int position) {
        //Don't need this.
        return -1;
    }

    public View getView(final int position, View convert, ViewGroup parent) {
        View view = convert;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.guess_list, null);
        }

        TextView guess1 = (TextView) view.findViewById(R.id.guess1);
        guess1.setText(possibleWords.get(position));

        return view;
    }


}
