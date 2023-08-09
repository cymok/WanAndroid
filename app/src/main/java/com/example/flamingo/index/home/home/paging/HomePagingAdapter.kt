package com.example.flamingo.index.home.home.paging

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.flamingo.R
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.data.Banner
import com.example.flamingo.data.DataX
import com.example.flamingo.data.WebData
import com.example.flamingo.databinding.RvItemArticleBinding
import com.example.flamingo.databinding.RvItemBannerBinding
import com.example.flamingo.index.home.home.HomeBannerAdapter
import com.example.flamingo.index.home.home.HomeFragment
import com.example.flamingo.index.web.WebActivity
import com.example.flamingo.utils.load
import com.example.flamingo.utils.loadRes
import com.example.flamingo.utils.visible
import com.lxj.xpopup.XPopup
import com.youth.banner.indicator.CircleIndicator
import splitties.views.onClick

class HomePagingAdapter(val fragment: HomeFragment, private var bannerData: Banner) :
    PagingDataAdapter<DataX, ViewHolder>(object : DiffUtil.ItemCallback<DataX>() {
        override fun areItemsTheSame(oldItem: DataX, newItem: DataX): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataX, newItem: DataX): Boolean {
            return oldItem == newItem
        }
    }) {

    fun setBanner(banner: Banner) {
        bannerData = banner
        notifyItemChanged(0)
    }

    var listener: ((Int, Boolean) -> Unit)? = null

    fun setLickListener(listener: (Int, Boolean) -> Unit) {
        this.listener = listener
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == 0) {

            // banner
            val homeBannerAdapter = HomeBannerAdapter(bannerData)
            (holder.itemView as com.youth.banner.Banner<*, *>).addBannerLifecycleObserver(fragment.viewLifecycleOwner)
                .setAdapter(homeBannerAdapter)
                .indicator = CircleIndicator(fragment.requireContext())

        } else {

            val item = getItem(position - 1) // index == 0 是 banner

            // list
            holder as HomePagingViewHolder
            holder.binding.run {
                item?.let {

                    tvTime.text = item.niceDate
                    tvAuthor.text = "分享人: ${item.author}"

                    tvTitle.text = item.title

                    tvSubtitle.text = Html.fromHtml(item.desc)
                    tvSubtitle.visible(item.desc.isNotBlank())

                    tvClassification.text = "${item.superChapterName}·${item.chapterName}"
                    tvPosition.text = "[ $position ]"

                    ivImg.load(item.envelopePic)
                    ivImg.visible(item.envelopePic.isNotBlank())

                    root.setOnClickListener {
                        WebActivity.start(item, ArticlePage.HOME, position)
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
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return if (viewType == 0) {
            HomeBannerViewHolder(
                RvItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            HomePagingViewHolder(
                RvItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    fun updateLikeItem(it: WebData) {
        val item = getItem(it.listPosition!! - 1)!! // index == 0 是 banner
        item.collect = it.like!!
        notifyItemChanged(it.listPosition)
    }

}

class HomeBannerViewHolder(val binding: RvItemBannerBinding) : ViewHolder(binding.root)
class HomePagingViewHolder(val binding: RvItemArticleBinding) : ViewHolder(binding.root)
