package com.example.flamingo.index.home.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.data.Banner
import com.example.flamingo.data.BannerItem
import com.example.flamingo.databinding.ViewBannerBinding
import com.example.flamingo.index.web.WebActivity
import com.example.flamingo.utils.load
import com.youth.banner.adapter.BannerAdapter
import splitties.views.onClick

class HomeBannerAdapter(list: Banner) : BannerAdapter<BannerItem, HomeBannerViewHolder>(list) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): HomeBannerViewHolder {
        return HomeBannerViewHolder(
            ViewBannerBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindView(
        holder: HomeBannerViewHolder,
        data: BannerItem,
        position: Int,
        size: Int
    ) {
        holder.binding.run {
            // 注意，必须设置为match_parent，这个是viewpager2强制要求的
            root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            iv.load(data.imagePath)
            root.onClick {
                WebActivity.start(data, ArticlePage.HOME, 0) // banner 在外层列表 index == 0
            }
        }
    }

}

class HomeBannerViewHolder(val binding: ViewBannerBinding) : RecyclerView.ViewHolder(binding.root)