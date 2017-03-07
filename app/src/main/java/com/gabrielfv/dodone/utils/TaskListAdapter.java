package com.gabrielfv.dodone.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.gabrielfv.dodone.models.Task;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

import static android.os.Build.VERSION_CODES.N;

/**
 * Created by gabriel on 22/02/17.
 */

public class TaskListAdapter extends RealmBaseAdapter<Task> implements ListAdapter {

    private static class ViewHolder {
        TextView task;
    }

    public TaskListAdapter(Context context, OrderedRealmCollection<Task> realmResults) {
        super(context, realmResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.task = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Task item = adapterData.get(position);
        viewHolder.task.setText(item.getDescription());
        return convertView;
    }
}
