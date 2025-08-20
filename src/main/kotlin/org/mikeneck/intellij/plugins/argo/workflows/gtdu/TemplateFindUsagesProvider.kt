package org.mikeneck.intellij.plugins.argo.workflows.gtdu

import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls
import org.jetbrains.yaml.YAMLWordsScanner

class TemplateFindUsagesProvider: FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner {
        return YAMLWordsScanner()
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        TODO("Not yet implemented")
    }

    override fun getHelpId(psiElement: PsiElement): @NonNls String? {
        TODO("Not yet implemented")
    }

    override fun getType(element: PsiElement): @Nls String {
        TODO("Not yet implemented")
    }

    override fun getDescriptiveName(element: PsiElement): @Nls String {
        TODO("Not yet implemented")
    }

    override fun getNodeText(
        element: PsiElement,
        useFullName: Boolean
    ): @Nls String {
        TODO("Not yet implemented")
    }
}