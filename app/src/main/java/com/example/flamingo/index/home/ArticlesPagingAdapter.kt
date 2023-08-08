package com.example.flamingo.index.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.flamingo.index.web.WebActivity
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.data.DataX
import com.example.flamingo.databinding.RvItemArticleBinding
import com.example.flamingo.utils.load
import com.example.flamingo.utils.visible

class ArticlesPagingAdapter(
    @ArticlePage private val whichPage: Int
) :
    PagingDataAdapter<DataX, ArticlesPagingViewHolder>(object : DiffUtil.ItemCallback<DataX>() {
        override fun areItemsTheSame(oldItem: DataX, newItem: DataX): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataX, newItem: DataX): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun getItemViewType(position: Int): Int {
        return whichPage
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ArticlesPagingViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.run {
            item?.let {

                when (whichPage) {
                    ArticlePage.HOME,
                    ArticlePage.SQUARE,
                    -> {

                    }

                    ArticlePage.PROJECT,
                    ArticlePage.SUBSCRIBE,
                    -> {

                    }

                    else -> {

                    }
                }

                tvTime.text = item.niceDate
                tvAuthor.text = item.author

                tvTitle.text = item.title

                tvSubtitle.text = item.desc
                tvSubtitle.visible(item.desc.isNotBlank())

                tvClassification.text = "${item.superChapterName}·${item.chapterName}"
                tvPosition.text = "[ ${position + 1} ]"

                ivImg.load(item.envelopePic)
                ivImg.visible(item.envelopePic.isNotBlank())

                root.setOnClickListener {
                    item.let {
                        WebActivity.start(item)
                    }
                }
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