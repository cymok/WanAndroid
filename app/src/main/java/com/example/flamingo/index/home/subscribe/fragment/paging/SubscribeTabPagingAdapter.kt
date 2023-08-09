package com.example.flamingo.index.home.subscribe.fragment.paging

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.flamingo.R
import com.example.flamingo.data.DataX
import com.example.flamingo.data.LikeData
import com.example.flamingo.data.WebData
import com.example.flamingo.databinding.RvItemArticleBinding
import com.example.flamingo.utils.load
import com.example.flamingo.utils.loadRes
import com.example.flamingo.utils.visible
import com.lxj.xpopup.XPopup
import splitties.views.onClick

class SubscribePagingAdapter :
    PagingDataAdapter<DataX, SubscribeTabPagingViewHolder>(object : DiffUtil.ItemCallback<DataX>() {
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
    override fun onBindViewHolder(holder: SubscribeTabPagingViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.run {
            item?.let {

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
                    itemClickListener?.invoke(position, item)
                }

                ivTop.visible(item.type == 1)
                ivNew.visible(item.fresh)

                // 收藏
                if (item.collect) {
                    ivStar.loadRes(R.drawable.icon_star_selected)
                } else {
                    ivStar.loadRes(R.drawable.icon_star)
                }
                ivStar.onClick {
                    if (item.collect) {
                        XPopup.Builder(holder.binding.root.context)
                            .asConfirm("移除收藏", "《${item.title}》") {
                                likeClickListener?.invoke(
                                    LikeData(
                                        id = item.id,
                                        like = false,
                                        position = position,
                                    )
                                )
                            }.show()
                    } else {
                        likeClickListener?.invoke(
                            LikeData(
                                id = item.id,
                                like = true,
                                position = position,
                            )
                        )
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubscribeTabPagingViewHolder {
        return SubscribeTabPagingViewHolder(
            RvItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun updateLikeItem(it: WebData) {
        val item = getItem(it.position!!)!!
        item.collect = it.like!!
        notifyItemChanged(it.position)
    }

}

class SubscribeTabPagingViewHolder(val binding: RvItemArticleBinding) : ViewHolder(binding.root)