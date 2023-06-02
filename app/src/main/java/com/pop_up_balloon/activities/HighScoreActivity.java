package com.pop_up_balloon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.pop_up_balloon.databinding.ActivityHighScoreBinding;
import com.pop_up_balloon.objects.Player;
import com.pop_up_balloon.utils.DataProvider;
import com.pop_up_balloon.utils.PlayerAdapter;

/**
 * <h1>HighScoreActivity</h1>
 *
 * <p><b>HighScoreActivity</b> represent saving high score logic in the scoreboard</p>
 */
public class HighScoreActivity extends AppCompatActivity {

    private ActivityHighScoreBinding binding;
    private final PlayerAdapter adapter = new PlayerAdapter();
    private DataProvider dataProvider;
    private final List<Player> players = new ArrayList<>();

    /**
     * This method initializes recycler view with player's data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHighScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dataProvider = new DataProvider(this);
        updatePlayers(dataProvider.getPlayers());

        binding.rcv.setAdapter(adapter);
    }
    /**
     * Show/Hide empty state UI and recycler view, based on updated list
     */
    @SuppressLint("NewApi")
    private void updatePlayers(List<Player> updatedPlayers) {

        if (updatedPlayers.isEmpty()) {
            binding.rcv.setVisibility(View.GONE);
        } else {
            binding.rcv.setVisibility(View.VISIBLE);
        }

        players.clear();
        players.addAll(updatedPlayers);
        adapter.updateList(players);
    }
}