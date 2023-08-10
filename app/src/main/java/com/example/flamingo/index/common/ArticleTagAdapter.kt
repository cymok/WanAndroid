package com.example.flamingo.index.common

import android.view.View
import com.example.flamingo.R
import com.example.flamingo.data.Tag
import com.example.flamingo.databinding.ViewTagBinding
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import splitties.systemservices.layoutInflater

class ArticleTagAdapter(listTag: List<Tag>) : TagAdapter<Tag>(listTag) {

    override fun getView(parent: FlowLayout, position: Int, t: Tag): View {
        return ViewTagBinding.inflate(parent.layoutInflater, parent, false).root.apply {
            text = t.name
            when (t.name) {
                "本站发布" -> {
                    setBackgroundResource(R.drawable.selector_tag_bg_green)
                }

                "问答" -> {
                    setBackgroundResource(R.drawable.selector_tag_bg_red)
                }

                "项目" -> {
                    setBackgroundResource(R.drawable.selector_tag_bg_blue)
                }

                "公众号" -> {
                    setBackgroundResource(R.drawable.selector_tag_bg_yellow)
                }

                else -> {
                    setBackgroundResource(R.drawable.selector_tag_bg_green)
                }
            }
        }
    }

}
