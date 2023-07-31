package com.example.lint

import com.android.tools.lint.detector.api.*
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression

class Test{

}

class GlideLint : Detector(), Detector.UastScanner {

    override fun visitMethod(
        context: JavaContext,
        visitor: JavaElementVisitor?,
        call: PsiMethodCallExpression,
        method: PsiMethod
    ) {
        super.visitMethod(context, visitor, call, method)
        if (context.evaluator.isMemberInClass(null, "com.bumptech.glide.Glide")) {
            context.report(ISSUE, call, context.getLocation(call), "")
        }
    }
}

val ISSUE = Issue.create(
    "Glide-Usage",
    "使用项目的 GlideApp 代替 Glide",
    "请勿直接调用 [com.bumptech.glide.Glide], 使用项目的 GlideApp 代替 Glide",
    Category.SECURITY, 5, Severity.ERROR,
    Implementation(GlideLint::class.java, Scope.JAVA_FILE_SCOPE)
)
