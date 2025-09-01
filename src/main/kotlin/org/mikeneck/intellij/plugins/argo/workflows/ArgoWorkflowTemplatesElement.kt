package org.mikeneck.intellij.plugins.argo.workflows

import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLSequence

typealias ArgoWorkflowTemplatesElement = YAMLKeyValue

fun ArgoWorkflowTemplatesElement.templates(): ArgoWorkflowTemplateElementCollection {
    return this.value<YAMLSequence>()?.items?.mapNotNull { it.value as? ArgoWorkflowTemplateElement } ?: emptyList()
}

operator fun ArgoWorkflowTemplatesElement.iterator(): Iterator<ArgoWorkflowTemplateElement> = this.templates().iterator()

val YAMLDocument.templates: ArgoWorkflowTemplateElementCollection get() =
    this.resource.getValue<YAMLMapping>("spec")
        .getValue<YAMLSequence>("templates")
        .getItems<ArgoWorkflowTemplateElement>()
