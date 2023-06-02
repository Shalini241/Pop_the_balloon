package com.pop_up_balloon.utils;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pop_up_balloon.databinding.ItemPlayerBinding;
import com.pop_up_balloon.objects.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle player list (View, Addition, Modification)
 */
public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private final List<Player> players = new ArrayList<>();

    /**
     * creates view holder
     */
    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlayerViewHolder(ItemPlayerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    /**
     * Methods to fill player's data and bind it to the view
     */
    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = players.get(position);

        holder.tvName.setText(player.getName());
        holder.tvScore.setText(String.valueOf(player.getScore()));
        holder.tvNo.setText(position + 1 + ".");
        holder.tvDate.setText(new DateUtils().convertDateToString(player.getDate()));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    /**
     * updates player's list with given players
     * @param updatedList
     */
    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Player> updatedList) {
        players.clear();
        players.addAll(updatedList);
        notifyDataSetChanged();
    }
    /**
     * Player View Holder to hold recycler view of player's list
     */
    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvScore, tvDate, tvNo;

        public PlayerViewHolder(ItemPlayerBinding binding) {
            super(binding.getRoot());

            /* Set data to UI */
            tvNo = binding.tvNo;
            tvName = binding.tvName;
            tvScore = binding.tvScore;
            tvDate = binding.tvDate;
        }
    }
}
