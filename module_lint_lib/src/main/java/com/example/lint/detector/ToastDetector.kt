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

class ToastDetector : Detector(), Detector.UastScanner {

    override fun getApplicableMethodNames(): List<String> {
        return listOf("makeText")
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (method.containingClass?.qualifiedName == "android.widget.Toast") {
            context.report(
                ISSUE,
                node,
                context.getLocation(node),
                "使用 `toast()` `toastShort()` `toastLong()`"
            )
        }
    }

    companion object {
        val ISSUE: Issue = Issue.create(
            id = "ToastUsage", // `lint.xml` 配置用的是这个 id
            briefDescription = "使用 `toast()` `toastShort()` `toastLong()`",
            explanation = "使用统一的方式，便于维护",
            category = Category.CORRECTNESS,
            severity = Severity.WARNING,
            implementation = Implementation(
                ToastDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

}