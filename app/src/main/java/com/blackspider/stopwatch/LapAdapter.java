package com.blackspider.stopwatch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class LapAdapter extends ArrayAdapter<Lap> {

    public LapAdapter(@NonNull Context context, List<Lap> laps) {
        super(context, 0, laps);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lap_list_item, parent, false);
        }

        Lap lap = getItem(position);

        TextView lapNumber = convertView.findViewById(R.id.lap_number);
        TextView lapSplitTime = convertView.findViewById(R.id.lap_split_time);
        TextView lapTotalTime = convertView.findViewById(R.id.lap_total_time);

        lapNumber.setText(String.format("%02d", lap.getLapNumber()));
        lapSplitTime.setText(lap.getSplitTime());
        lapTotalTime.setText(lap.getTotalTime());

        return convertView;
    }
}
