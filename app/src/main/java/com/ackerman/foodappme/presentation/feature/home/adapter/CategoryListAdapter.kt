package com.ackerman.foodappme.presentation.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ackerman.foodappme.databinding.ItemCategoryMenuBinding
import com.ackerman.foodappme.model.Category

class CategoryListAdapter(
    private val itemClick: (Category) -> Unit
) : RecyclerView.Adapter<ItemCategoryViewHolder>() {

    private val dataDiffer = AsyncListDiffer(
        this,
        object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    )

    fun submitData(data: List<Category>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCategoryViewHolder {
        return ItemCategoryViewHolder(
            binding = ItemCategoryMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            itemClick
        )
    }

    override fun onBindViewHolder(holder: ItemCategoryViewHolder, position: Int) {
        holder.bindView(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size
}

// CATEGORYVIEWHOLDER
class ItemCategoryViewHolder(
    private val binding: ItemCategoryMenuBinding,
    val itemClick: (Category) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bindView(item: Category) {
        with(item) {
            binding.ivCategoryItem.load(item.imgCategoryUrl) {
                crossfade(true)
            }
            binding.tvCategoryName.text = item.name
            itemView.setOnClickListener { itemClick(this) }
        }
    }
}
