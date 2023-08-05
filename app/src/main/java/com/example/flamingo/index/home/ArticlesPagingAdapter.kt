package com.example.flamingo.index.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.flamingo.base.activity.BaseWebActivity
import com.example.flamingo.data.DataX
import com.example.flamingo.databinding.RvItemArticleBinding

class ArticlesPagingAdapter :
    PagingDataAdapter<DataX, ArticlesPagingViewHolder>(object : DiffUtil.ItemCallback<DataX>() {
        override fun areItemsTheSame(oldItem: DataX, newItem: DataX): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataX, newItem: DataX): Boolean {
            return oldItem == newItem
        }
    }) {

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ArticlesPagingViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.tvTitle.text = "${position}. ${item?.title}"

        holder.binding.root.setOnClickListener {
            item?.let {
                BaseWebActivity.start(item.link, item.title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesPagingViewHolder {
        return ArticlesPagingViewHolder(
            RvItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}

class ArticlesPagingViewHolder(val binding: RvItemArticleBinding) : ViewHolder(binding.root)