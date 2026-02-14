package com.blackspider.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView time_view;
    private ImageButton start_button, stop_button, hold_button, reset_button;
    private ListView lap_list;
    private AnalogStopwatch analog_stopwatch;

    private Handler handler;
    private List<Lap> lapTimes;
    private LapAdapter adapter;

    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private long lastLapTime = 0L;

    private boolean running;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            time_view.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", mins, secs, milliseconds / 10));
            analog_stopwatch.setTime(updatedTime);
            handler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time_view = findViewById(R.id.time_view);
        start_button = findViewById(R.id.start_button);
        stop_button = findViewById(R.id.stop_button);
        hold_button = findViewById(R.id.hold_button);
        reset_button = findViewById(R.id.reset_button);
        lap_list = findViewById(R.id.lap_list);
        analog_stopwatch = findViewById(R.id.analog_stopwatch);

        handler = new Handler();
        lapTimes = new ArrayList<>();
        adapter = new LapAdapter(this, lapTimes);
        lap_list.setAdapter(adapter);

        start_button.setOnClickListener(v -> {
            if (!running) {
                running = true;
                startTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                start_button.setVisibility(View.GONE);
                stop_button.setVisibility(View.VISIBLE);
            }
        });

        stop_button.setOnClickListener(v -> {
            if (running) {
                running = false;
                timeSwapBuff += timeInMilliseconds;
                handler.removeCallbacks(runnable);
                stop_button.setVisibility(View.GONE);
                start_button.setVisibility(View.VISIBLE);
            }
        });

        hold_button.setOnClickListener(v -> {
            if (running) {
                long currentTotalTime = updatedTime;
                long splitTimeMillis = currentTotalTime - lastLapTime;
                lastLapTime = currentTotalTime;

                String totalTimeString = formatTime(currentTotalTime);
                String splitTimeString = "+" + formatTime(splitTimeMillis);

                int lapNumber = lapTimes.size() + 1;
                Lap newLap = new Lap(lapNumber, splitTimeString, totalTimeString);
                lapTimes.add(0, newLap);
                adapter.notifyDataSetChanged();
            }
        });

        reset_button.setOnClickListener(v -> {
            running = false;
            startTime = 0L;
            timeInMilliseconds = 0L;
            timeSwapBuff = 0L;
            updatedTime = 0L;
            lastLapTime = 0L;
            time_view.setText("00:00:00");
            lapTimes.clear();
            adapter.notifyDataSetChanged();
            handler.removeCallbacks(runnable);
            analog_stopwatch.setTime(0);
            stop_button.setVisibility(View.GONE);
            start_button.setVisibility(View.VISIBLE);
        });
    }

    private String formatTime(long millis) {
        int secs = (int) (millis / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        int milliseconds = (int) (millis % 1000);
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", mins, secs, milliseconds / 10);
    }
}
