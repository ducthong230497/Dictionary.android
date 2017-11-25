package com.example.media.dictionary;

/**
 * Created by Media on 11/23/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<String> wordList = null;
    private ArrayList<String> arraylist;

    public ListViewAdapter(Context context, List<String> wordList) {
        mContext = context;
        this.wordList = wordList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<String>();
        this.arraylist.addAll(wordList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return wordList.size();
    }

    @Override
    public String getItem(int position) {
        return wordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_items, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.word);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(wordList.get(position).toString());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        wordList.clear();
        if (charText.length() == 0) {
            wordList.addAll(arraylist);
        } else {
            for (String wp : arraylist) {
                if (wp.toString().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                    wordList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}