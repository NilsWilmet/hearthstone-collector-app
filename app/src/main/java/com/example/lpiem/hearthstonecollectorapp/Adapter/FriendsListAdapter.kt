package com.example.lpiem.hearthstonecollectorapp.Adapter;

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lpiem.hearthstonecollectorapp.Models.Friendship
import com.example.lpiem.hearthstonecollectorapp.R
import com.example.lpiem.hearthstonecollectorapp.Models.User
import kotlinx.android.synthetic.main.friends_list_item.view.*

class FriendsListAdapter(val items: List<Friendship>, val context: Context, var listener: Listener) : RecyclerView.Adapter<ViewHolderFriends>() {

    companion object {
        lateinit var fragContext: Context
    }

    interface Listener {
        fun onItemClicked(item: Friendship)
        fun onDeleteClicked(item: Friendship)
    }

    // Gets the number of friends in the list
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFriends {
        fragContext = context
        return ViewHolderFriends(LayoutInflater.from(context).inflate(R.layout.friends_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolderFriends, position: Int) {
        holder?.bind(items[position]?.user2.pseudo)
        holder.itemView.setOnClickListener {
            listener.onItemClicked(items[position])
        }
        holder.imageView.setOnClickListener {
            listener.onDeleteClicked(items[position])
        }

    }

}

class ViewHolderFriends (view: View) : RecyclerView.ViewHolder(view) {
    //val tvCardName = view.cardsList_card_name
    val textView = view.friends_list_item_pseudo
    val imageView = view.btn_delete_friend

    fun bind(pseudo: String?) {
        textView?.text = pseudo
    }
}