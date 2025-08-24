package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.psi.PsiElement
import me.vnagy.intellijplugins.argo.wrapper.parent
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.YAMLKeyValue

typealias ArgoWorkflowStepTemplateElement = YAMLKeyValue

fun argoWorkflowStepTemplateElementFrom(psiElement: PsiElement): ArgoWorkflowStepTemplateElement? = psiElement.asYAMLKeyValue()?.ifHasKey("template")

val PsiElement.asArgoWorkflowStepTemplateElement: ArgoWorkflowStepTemplateElement? get() =  this.asYAMLKeyValue()?.ifHasKey("template")

val ArgoWorkflowStepTemplateElement?.upToStep: ArgoWorkflowTemplateSingleStepElement? get() = this.parent<ArgoWorkflowTemplateSingleStepElement>()

fun ArgoWorkflowStepTemplateElement.findLocalTemplate(): Iterable<ArgoWorkflowTemplateElement> {
    if (!(this.isValid)) {
        return emptyList()
    }
    val templateName = this.valueText
    val file = this.containingFile as? YAMLFile ?: return emptyList()

}
