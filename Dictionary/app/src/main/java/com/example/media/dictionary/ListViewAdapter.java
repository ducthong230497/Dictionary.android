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
    private List<String> listAllWord = null;
    ArrayList<String>[] listWord;
    private ArrayList<String> arraylist;

    public ListViewAdapter(Context context, List<String> wordList, ArrayList<String>[] listWord) {
        mContext = context;
        this.listAllWord = wordList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<String>();
        this.arraylist.addAll(wordList);
        this.listWord = listWord;
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return listAllWord.size();
    }

    @Override
    public String getItem(int position) {
        return listAllWord.get(position);
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
        holder.name.setText(listAllWord.get(position).toString());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        listAllWord.clear();
        //if (charText.length() == 0) {
        //    listAllWord.addAll(arraylist);
        //} else {
        int i = charText.charAt(0);
        if (i >= 97){
            i -= 97;
        }
        else if (i >= 65){
            i -= 65;
        }
        else {
            i = 26;
        }
            for (String wp : listWord[i]) {
                if (wp.toString().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                    listAllWord.add(wp);
                }
            }
        //}
        notifyDataSetChanged();
    }

}