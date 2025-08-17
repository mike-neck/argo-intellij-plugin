package me.vnagy.intellijplugins.argo.references.callsitetemplate

import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar

class CallsiteTemplateNameReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            psiElement(),
            CallsiteTemplateNameReferenceProvider()
        )
    }
}
