package com.example.lint.detector

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

class GlideDetector : Detector(), Detector.UastScanner {

    override fun getApplicableMethodNames(): List<String> {
        // 指定方法名
        return listOf("with")
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        // 找到指定方法的类
        if (method.containingClass?.qualifiedName == "com.bumptech.glide.Glide") {
            context.report(
                ISSUE,
                node,
                context.getLocation(node),
                "不建议直接使用 Glide"
            )
        }
    }

    companion object {
        val ISSUE = Issue.create(
            id = "GlideUsage", // `lint.xml` 配置用的是这个 id
            briefDescription = "建议使用 ImageViewExt 里面的扩展函数",
            explanation = "使用统一的方式，便于维护",
            category = Category.SECURITY,
            severity = Severity.WARNING,
            implementation = Implementation(GlideDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

}
