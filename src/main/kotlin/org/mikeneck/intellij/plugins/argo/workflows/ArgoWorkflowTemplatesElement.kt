package org.mikeneck.intellij.plugins.argo.workflows

import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLSequence

typealias ArgoWorkflowTemplatesElement = YAMLKeyValue

fun ArgoWorkflowTemplatesElement.templates(): ArgoWorkflowTemplateElementCollection {
    return this.value<YAMLSequence>()?.items?.mapNotNull { it.value as? ArgoWorkflowTemplateElement } ?: emptyList()
}

operator fun ArgoWorkflowTemplatesElement.iterator(): Iterator<ArgoWorkflowTemplateElement> = this.templates().iterator()
