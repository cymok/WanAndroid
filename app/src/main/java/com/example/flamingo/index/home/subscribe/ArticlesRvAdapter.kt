package com.example.flamingo.index.home.subscribe

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flamingo.data.ArticlesTree
import com.example.flamingo.databinding.RvArticlesBinding
import com.example.flamingo.index.home.ArticlesPagingAdapter
import com.example.flamingo.utils.dp2px
import com.example.flamingo.utils.getViewModel
import com.grzegorzojdana.spacingitemdecoration.Spacing
import com.grzegorzojdana.spacingitemdecoration.SpacingItemDecoration

class ArticlesRvAdapter(private val fragment: SubscribeFragment) :
    RecyclerView.Adapter<ArticlesViewHolder>() {

    var list = ArticlesTree()
    private val adapter = ArticlesPagingAdapter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        return ArticlesViewHolder(
            RvArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ).apply {
            val recyclerView = this.binding.rv
            recyclerView.addItemDecoration(SpacingItemDecoration(
                Spacing(
                    vertical = 12.dp2px,
                    horizontal = 50.dp2px,
                    item = Rect(),
                    edges = Rect(16.dp2px, 20.dp2px, 16.dp2px, 20.dp2px),
                )
            ))
            recyclerView.adapter = adapter
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        val data = list[position]
        fragment.getViewModel<SubscribeViewModel>().getArticlesWithPager(id = data.id)
            .observe(fragment.viewLifecycleOwner) {
                adapter.submitData(fragment.lifecycle, it)
            }
    }
}

class ArticlesViewHolder(val binding: RvArticlesBinding) : RecyclerView.ViewHolder(binding.root)