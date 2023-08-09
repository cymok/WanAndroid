package com.example.flamingo.index.home.project.fragment.paging

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.flamingo.R
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.data.DataX
import com.example.flamingo.data.WebData
import com.example.flamingo.databinding.RvItemArticleBinding
import com.example.flamingo.index.web.WebActivity
import com.example.flamingo.utils.load
import com.example.flamingo.utils.loadRes
import com.example.flamingo.utils.visible
import com.lxj.xpopup.XPopup
import splitties.views.onClick

class ProjectTabPagingAdapter :
    PagingDataAdapter<DataX, ProjectTabPagingViewHolder>(object : DiffUtil.ItemCallback<DataX>() {
        override fun areItemsTheSame(oldItem: DataX, newItem: DataX): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataX, newItem: DataX): Boolean {
            return oldItem == newItem
        }
    }) {

    var listener: ((Int, Boolean) -> Unit)? = null

    fun setLickListener(listener: (Int, Boolean) -> Unit) {
        this.listener = listener
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProjectTabPagingViewHolder, position: Int) {
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
                    WebActivity.start(item, ArticlePage.PROJECT, position)
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
                    val like = item.collect.not()
                    if (item.collect) {
                        XPopup.Builder(holder.binding.root.context)
                            .asConfirm("提示", "您已收藏, 您要取消收藏吗?") {
                                listener?.invoke(item.id, like)
                                item.collect = item.collect.not()
                                // 本地处理
                                if (like) {
                                    ivStar.loadRes(R.drawable.icon_star_selected)
                                } else {
                                    ivStar.loadRes(R.drawable.icon_star)
                                }
                            }.show()
                    } else {
                        listener?.invoke(item.id, like)
                        item.collect = item.collect.not()
                        // 本地处理
                        if (like) {
                            ivStar.loadRes(R.drawable.icon_star_selected)
                        } else {
                            ivStar.loadRes(R.drawable.icon_star)
                        }
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectTabPagingViewHolder {
        return ProjectTabPagingViewHolder(
            RvItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun updateLikeItem(it: WebData) {
        val item = getItem(it.listPosition!!)!!
        item.collect = it.like!!
        notifyItemChanged(it.listPosition)
    }

}

class ProjectTabPagingViewHolder(val binding: RvItemArticleBinding) : ViewHolder(binding.root)