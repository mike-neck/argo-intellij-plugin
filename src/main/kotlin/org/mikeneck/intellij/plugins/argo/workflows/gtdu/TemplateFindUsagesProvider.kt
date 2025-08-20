package org.mikeneck.intellij.plugins.argo.workflows.gtdu

import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls
import org.jetbrains.yaml.YAMLWordsScanner
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalar
import org.mikeneck.intellij.plugins.argo.workflows.fromTemplateNameToTemplates

class TemplateFindUsagesProvider: FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner {
        return YAMLWordsScanner()
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        val yamlKeyValue = when (psiElement) {
            is YAMLKeyValue -> psiElement
            is YAMLScalar -> psiElement.parent as? YAMLKeyValue
            else -> null
        } ?: return false
        if (yamlKeyValue.keyText != "name") {
            return false
        }
        return yamlKeyValue.fromTemplateNameToTemplates() != null

        //TODO NOT inputs.{parameters,artifacts}.name usages in the same template
        //TODO NOT steps.name.outputs.{parameters,artifacts}.name usages in steps in the same template
        //TODO NOT outputs.{parameters,artifacts}.name usages in steps in the same template of the caller step
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