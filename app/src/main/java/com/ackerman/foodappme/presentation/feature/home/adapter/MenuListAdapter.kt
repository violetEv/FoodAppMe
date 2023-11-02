package com.ackerman.foodappme.presentation.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ackerman.foodappme.core.ViewHolderBinder
import com.ackerman.foodappme.databinding.ItemGridListMenuBinding
import com.ackerman.foodappme.databinding.ItemLinearListMenuBinding
import com.ackerman.foodappme.model.Menu
import com.ackerman.foodappme.utils.toCurrencyFormat

class MenuListAdapter(
    var adapterLayoutMode: AdapterLayoutMode,
    private val onClickListener: (Menu) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataDiffer = AsyncListDiffer(
        this,
        object : DiffUtil.ItemCallback<Menu>() {
            override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            AdapterLayoutMode.GRID.ordinal -> {
                GridMenuListItemViewHolder(
                    binding = ItemGridListMenuBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    onClickListener
                )
            }

            else -> {
                LinearMenuListItemViewHolder(
                    binding = ItemLinearListMenuBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    onClickListener
                )
            }
        }
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolderBinder<Menu>).bind(dataDiffer.currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return adapterLayoutMode.ordinal
    }

    fun submitData(data: List<Menu>) {
        dataDiffer.submitList(data)
    }

    // MENU LIST VIEW HOLDER
    class LinearMenuListItemViewHolder(
        private val binding: ItemLinearListMenuBinding,
        private val onClicklistener: (Menu) -> Unit
    ) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Menu> {
        override fun bind(item: Menu) {
            binding.ivMenuList.load(item.imgMenuUrl) {
                crossfade(true)
            }
            binding.tvMenuName.text = item.name
            binding.tvMenuPrice.text = item.price.toCurrencyFormat()
            binding.root.setOnClickListener {
                onClicklistener.invoke(item)
            }
        }
    }

    class GridMenuListItemViewHolder(
        private val binding: ItemGridListMenuBinding,
        private val onClickListener: (Menu) -> Unit
    ) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Menu> {
        override fun bind(item: Menu) {
            binding.ivMenuList.load(item.imgMenuUrl) {
                crossfade(true)
            }
            binding.tvMenuName.text = item.name
            binding.tvMenuPrice.text = item.price.toCurrencyFormat()
            binding.root.setOnClickListener {
                onClickListener.invoke(item)
            }
        }
    }
}
