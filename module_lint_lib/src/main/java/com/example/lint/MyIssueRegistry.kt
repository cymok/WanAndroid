package com.example.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue
import com.example.lint.detector.GlideDetector
import com.example.lint.detector.LogDetector
import com.example.lint.detector.ToastDetector

class MyIssueRegistry : IssueRegistry() {

    override val issues: List<Issue> = listOf(
        GlideDetector.ISSUE,
        LogDetector.ISSUE,
        ToastDetector.ISSUE,
    ) // 将所有自定义 Lint 的 ISSUE 列出

}