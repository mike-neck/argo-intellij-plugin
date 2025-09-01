package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.psi.PsiElement
import me.vnagy.intellijplugins.argo.wrapper.parent
import org.jetbrains.yaml.psi.YAMLKeyValue

typealias ArgoWorkflowStepLocalTemplateNameElement = YAMLKeyValue

val PsiElement.asArgoWorkflowStepLocalTemplateNameElement: ArgoWorkflowStepLocalTemplateNameElement? get() =  this.asYAMLKeyValue()?.ifHasKey("template")

val ArgoWorkflowStepLocalTemplateNameElement?.upToStep: ArgoWorkflowTemplateSingleStepElement? get() = this.parent<ArgoWorkflowTemplateSingleStepElement>()
