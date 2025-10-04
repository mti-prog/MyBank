package com.geeks.mybank.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geeks.mybank.data.model.Account
import com.geeks.mybank.databinding.ItemAccountBinding
import com.geeks.mybank.ui.adapter.AccountAdapter.AccountViewHolder

class AccountAdapter() : RecyclerView.Adapter<AccountViewHolder>() {

    private val items = mutableListOf<Account>()

    fun submitList(data : List<Account>){
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountViewHolder {
        return AccountViewHolder(ItemAccountBinding.
        inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: AccountViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class AccountViewHolder(private val binding : ItemAccountBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(account: Account) = with(binding){
            tvName.text = account.name
            val text = "${account.balance} ${account.currency}"
            tvBalance.text = text
        }
    }
}