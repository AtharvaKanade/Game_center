package com.example.tictactoe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tictactoe.databinding.ItemGameBinding

class GamesAdapter(
    private val onGameClick: (Game) -> Unit
) : ListAdapter<Game, GamesAdapter.GameViewHolder>(GameDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = ItemGameBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GameViewHolder(binding, onGameClick)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class GameViewHolder(
        private val binding: ItemGameBinding,
        private val onGameClick: (Game) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game) {
            binding.gameTitle.text = game.title
            binding.gameDescription.text = game.description
            binding.gameIcon.setImageResource(game.iconResId)
            
            binding.root.setOnClickListener {
                onGameClick(game)
            }
        }
    }

    private class GameDiffCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem == newItem
        }
    }
}
