package org.mikeneck.intellij.plugins.argo.workflows.gtdu

import com.intellij.patterns.StandardPatterns.or
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalar

class TemplateUsagesContributor: PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            or(psiElement<YAMLScalar>(), psiElement<YAMLKeyValue>()),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<out PsiReference?> = TemplateUsagesPsiReferenceProvider.findReferences(element, context)
            }
        )
    }

    object TemplateUsagesPsiReferenceProvider {
        fun findReferences(
            element: PsiElement,
            context: ProcessingContext
        ): Array<PsiReference> {
            TODO("Not yet implemented")
        }
    }
}
