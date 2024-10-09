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

class LogDetector : Detector(), Detector.UastScanner {

    override fun getApplicableMethodNames(): List<String> {
        // 指定方法名
        return listOf("d", "e", "i", "v", "w", "wtf")
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        // 找到方法的类
        if (method.containingClass?.qualifiedName == "android.util.Log") {
            context.report(
                ISSUE,
                node,
                context.getLocation(node),
                "使用 `log()` `logd()` `loge()` `logi()` 等"
            )
        }
    }

    companion object {
        val ISSUE: Issue = Issue.create(
            id = "LogUsage", // `lint.xml` 配置用的是这个 id
            briefDescription = "使用 `log()` `logd()` `loge()` `logi()` 等",
            explanation = "使用统一的方式，便于维护",
            category = Category.CORRECTNESS,
            severity = Severity.WARNING,
            implementation = Implementation(
                LogDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

}