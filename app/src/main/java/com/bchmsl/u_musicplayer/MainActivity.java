package com.bchmsl.u_musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.SeekBar;

import com.bchmsl.u_musicplayer.databinding.ActivityMainBinding;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    boolean isPlaying = false;
    MediaPlayer player;

    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        setupAudioPlayer();
        listeners();
    }

    private void setupAudioPlayer() {
        player = MediaPlayer.create(this, R.raw.stuff);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        setupVolumeSeekBar();
        setupDurationSeekBar();
    }


    private void setupVolumeSeekBar() {
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        binding.sbVolume.setMax(maxVolume);
    }

    private void setupDurationSeekBar() {
        binding.sbDuration.setMax(player.getDuration());
        setupDurationTimer();
    }

    private void setupDurationTimer() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                binding.sbDuration.setProgress(player.getCurrentPosition());
            }
        }, 0, 1000);
    }

    private void listeners() {
        binding.ibtnPlayPause.setOnClickListener(v -> {
            if (isPlaying) pause();
            else play();
        });
        binding.ibtnPrevious.setOnClickListener(v -> {
            setToStart();
        });
        binding.ibtnNext.setOnClickListener(v -> {
            setToEnd();
        });

        binding.sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.sbDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        player.setOnCompletionListener(mp -> pause());
    }

    private void setToStart() {
        player.seekTo(0);
    }

    private void setToEnd() {
        player.seekTo(player.getDuration());
    }

    private void play() {
        player.start();
        isPlaying = true;
        binding.ibtnPlayPause.setImageResource(R.drawable.ic_pause);
    }

    private void pause() {
        player.pause();
        isPlaying = false;
        binding.ibtnPlayPause.setImageResource(R.drawable.ic_play);
    }
}