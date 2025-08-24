package org.mikeneck.intellij.plugins.argo.workflows.gtdu

import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls
import org.jetbrains.yaml.YAMLWordsScanner
import org.mikeneck.intellij.plugins.argo.workflows.Messages
import org.mikeneck.intellij.plugins.argo.workflows.asArgoWorkflowTemplateNameElement
import org.mikeneck.intellij.plugins.argo.workflows.asYAMLKeyValue

class TemplateFindUsagesProvider: FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner {
        return YAMLWordsScanner()
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement.asArgoWorkflowTemplateNameElement() != null

        //TODO NOT inputs.{parameters,artifacts}.name usages in the same template
        //TODO NOT steps.name.outputs.{parameters,artifacts}.name usages in steps in the same template
        //TODO NOT outputs.{parameters,artifacts}.name usages in steps in the same template of the caller step
    }

    override fun getHelpId(psiElement: PsiElement): @NonNls String = "reference.dialogs.findUsages.other"

    override fun getType(element: PsiElement): @Nls String {
        return Messages.message("find.usages.type.template")
    }

    override fun getDescriptiveName(element: PsiElement): @Nls String {
        val keyValue = element.asYAMLKeyValue() ?: return Messages.message("find.usages.unknown.element")
        return keyValue.keyText
    }

    override fun getNodeText(
        element: PsiElement,
        useFullName: Boolean
    ): @Nls String {
        if(!useFullName) return getDescriptiveName(element)
        else {
            val keyValue = element.asYAMLKeyValue() ?: return Messages.message("find.usages.unknown.element")
            val key = keyValue.keyText
            val value = keyValue.valueText
            return "$key: $value"
        }
    }
}