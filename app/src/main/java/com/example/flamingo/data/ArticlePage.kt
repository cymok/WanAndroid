package com.example.flamingo.data

import androidx.annotation.IntDef

@IntDef(
    *[
        ArticlePage.HOME,
        ArticlePage.STUDY,
        ArticlePage.SQUARE,
        ArticlePage.NAV,
        ArticlePage.TUTORIALS,
        ArticlePage.QA,
        ArticlePage.PROJECT_HOT,
        ArticlePage.SUBSCRIBE,
        ArticlePage.PROJECT,
        ArticlePage.TOOLS,
    ]
)
@Retention(AnnotationRetention.RUNTIME)
annotation class ArticlePage {

    companion object {

        const val HOME = 0
        const val STUDY = 1
        const val SQUARE = 2
        const val NAV = 3
        const val TUTORIALS = 4
        const val QA = 5
        const val PROJECT_HOT = 6
        const val SUBSCRIBE = 7
        const val PROJECT = 8
        const val TOOLS = 9

    }

}
