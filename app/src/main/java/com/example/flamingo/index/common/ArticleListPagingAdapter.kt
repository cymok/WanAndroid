package com.example.flamingo.index.common

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.flamingo.R
import com.example.flamingo.data.DataX
import com.example.flamingo.data.LikeData
import com.example.flamingo.databinding.RvItemArticleBinding
import com.example.flamingo.utils.load
import com.example.flamingo.utils.loadRes
import com.example.flamingo.utils.visible
import com.lxj.xpopup.XPopup
import splitties.views.onClick

class ArticleListPagingAdapter :
    PagingDataAdapter<DataX, ArticleListPagingViewHolder>(object : DiffUtil.ItemCallback<DataX>() {
        override fun areItemsTheSame(oldItem: DataX, newItem: DataX): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataX, newItem: DataX): Boolean {
            return oldItem == newItem
        }
    }) {

    var likeClickListener: ((LikeData) -> Unit)? = null

    fun onLikeClick(listener: (LikeData) -> Unit) {
        likeClickListener = listener
    }

    fun notifyLikeChanged(likeData: LikeData) {
        val item = getItem(likeData.position)!!
        item.collect = likeData.like
        notifyItemChanged(likeData.position)
    }

    var itemClickListener: ((Int, DataX) -> Unit)? = null

    fun onItemClick(listener: (Int, DataX) -> Unit) {
        itemClickListener = listener
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ArticleListPagingViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.run {
            item?.let {

                tvTime.text = item.niceDate

                tvAuthor.text = if (item.superChapterName == "广场Tab") {
                    "分享人: ${item.shareUser}"
                } else {
                    "作者: ${item.author}"
                }

                tvTitle.text = item.title

                tvSubtitle.text = item.desc
                tvSubtitle.visible(item.desc.isNotBlank())

                tvClassification.text = listOf(
                    item.superChapterName,
                    item.chapterName,
                ).filter {
                    // 解析为 null 了
                    @Suppress("UselessCallOnNotNull")
                    it.isNullOrBlank().not()
                }.joinToString("/")

                tvPosition.text = "[ ${position + 1} ]"

                ivImg.load(item.envelopePic)
                ivImg.visible(item.envelopePic.isNotBlank())

                root.setOnClickListener {
                    itemClickListener?.invoke(position, item)
                }

                ivTop.visible(item.type == 1)
                ivNew.visible(item.fresh)

                tag.adapter = ArticleTagAdapter(item.tags)

                // 收藏
                if (item.collect) {
                    ivStar.loadRes(R.drawable.icon_like_selected)
                } else {
                    ivStar.loadRes(R.drawable.icon_like)
                }
                ivStar.onClick {
                    if (item.collect) {
                        XPopup.Builder(holder.binding.root.context)
                            .asConfirm("移除收藏", "《${item.title}》") {
                                likeClickListener?.invoke(
                                    LikeData(
                                        id = item.id,
                                        originId = item.originId,
                                        like = false,
                                        position = position,
                                    )
                                )
                            }.show()
                    } else {
                        likeClickListener?.invoke(
                            LikeData(
                                id = item.id,
                                originId = item.originId,
                                like = true,
                                position = position,
                            )
                        )
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleListPagingViewHolder {
        return ArticleListPagingViewHolder(
            RvItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}

class ArticleListPagingViewHolder(val binding: RvItemArticleBinding) : ViewHolder(binding.root)