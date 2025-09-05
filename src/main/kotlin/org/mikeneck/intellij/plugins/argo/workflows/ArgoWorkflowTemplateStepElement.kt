package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.kubernetes.get
import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLScalar

typealias ArgoWorkflowTemplateStepElement = Collection<Collection<ArgoWorkflowTemplateSingleStepElement>>

fun ArgoWorkflowTemplateStepElement.findSingleStepCallingLocalTemplate(templateName: String): Collection<YAMLMapping> {
    return this.flatten().filter { (it["template"] as? YAMLScalar)?.textValue == templateName }
}

val ArgoWorkflowTemplateStepElement?.all: Collection<ArgoWorkflowTemplateSingleStepElement> get() = when (this) {
    null -> emptyList()
    else -> this.flatten()
}
