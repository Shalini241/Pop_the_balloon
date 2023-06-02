package com.pop_up_balloon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pop_up_balloon.databinding.ActivityCreateNewPlayerBinding;
import com.pop_up_balloon.objects.Player;
import com.pop_up_balloon.utils.DataProvider;
import com.pop_up_balloon.utils.DateUtils;
import com.pop_up_balloon.utils.Validator;

/**
 * Class to create new activity to add new player
 */
public class CreateNewPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityCreateNewPlayerBinding binding;
    private int score;
    private DataProvider dataProvider;
    private final DateUtils dateUtils = new DateUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNewPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        score = (int) intent.getExtras().getSerializable("Score");
        /* Show back arrow in toolbar to navigate back to previous activity */
        dataProvider = new DataProvider(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupUI();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /* android.R.id.home = Default resource ID for toolbar navigation icon */
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnSave) {
            /* Create player object with input */
            Player player = new Player(
                    binding.etName.getText().toString().trim(),
                    Long.parseLong(binding.etScore.getText().toString()),
                    dateUtils.convertStringToDate(binding.etDate.getText().toString())
            );

            /* Re-write all players to File */
            List<Player> players = new ArrayList<>();
            players.addAll(dataProvider.getPlayers());
            players.add(player);
            dataProvider.updateCache(players);
            Toast.makeText(this, "New High Score has been saved", Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            finish();
        }
    }

    /**
     * Function to validate input (Name and Score)
     */
    private void validateInput() {
        String name = binding.etName.getText().toString().trim();
        String date = binding.etDate.getText().toString();

        /* Enable action button iff, the inputs are valid */
        binding.btnSave.setEnabled(Validator.validateName(name) == null && Validator.validateDate(date) == null);
    }
    /**
     * Function to set input (Name, Score and Date) on Screen.
     */
    private void setupUI() {
        binding.btnSave.setOnClickListener(this);
        binding.etDate.setOnClickListener(this);

        /* Set default date in UI */
        binding.etDate.setText(dateUtils.convertDateToString(new Date()));
        binding.etScore.setText(Integer.toString(score));
        binding.etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.name.setError(Validator.validateName(s.toString()));
                validateInput();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.etDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.date.setError(Validator.validateDate(s.toString()));
                validateInput();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}