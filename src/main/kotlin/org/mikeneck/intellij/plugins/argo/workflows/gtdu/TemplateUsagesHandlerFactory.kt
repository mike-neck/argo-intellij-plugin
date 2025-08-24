package org.mikeneck.intellij.plugins.argo.workflows.gtdu

import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesHandlerFactory
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.lang.findUsages.LanguageFindUsages
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.usageView.UsageInfo
import com.intellij.util.Processor
import org.jetbrains.annotations.Unmodifiable
import org.mikeneck.intellij.plugins.argo.workflows.asArgoWorkflowTemplateNameElement

class TemplateUsagesHandlerFactory : FindUsagesHandlerFactory() {

    override fun canFindUsages(element: PsiElement): Boolean {
        return element.asArgoWorkflowTemplateNameElement() != null
    }

    override fun createFindUsagesHandler(
        element: PsiElement,
        forHighlightUsages: Boolean
    ): FindUsagesHandler? {
        if (!canFindUsages(element)) return null
        return FindTemplateUsagesHandler(element, forHighlightUsages)
    }
}

class FindTemplateUsagesHandler(
        element: PsiElement, private val forHighlightUsages: Boolean) : FindUsagesHandler(element) {
    override fun getHelpId(): String? {
        return LanguageFindUsages.getHelpId(psiElement)
    }

    override fun processElementUsages(
        element: PsiElement,
        processor: Processor<in UsageInfo>,
        options: FindUsagesOptions
    ): Boolean {
        return super.processElementUsages(element, processor, options)
    }

    override fun processUsagesInText(
        element: PsiElement,
        processor: Processor<in UsageInfo>,
        searchScope: GlobalSearchScope
    ): Boolean {
        return super.processUsagesInText(element, processor, searchScope)
    }

    override fun getPrimaryElements(): Array<out PsiElement?> {
        return super.getPrimaryElements()
    }

    override fun getSecondaryElements(): Array<out PsiElement?> {
        return super.getSecondaryElements()
    }

    override fun findReferencesToHighlight(
        target: PsiElement,
        searchScope: SearchScope
    ): @Unmodifiable Collection<PsiReference?> {
        return super.findReferencesToHighlight(target, searchScope)
    }
}
