package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.kubernetes.get
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLSequence

typealias ArgoWorkflowTemplatesElement = YAMLKeyValue

fun ArgoWorkflowTemplatesElement.templates(): ArgoWorkflowTemplateElementCollection {
    return this.value<YAMLSequence>()?.items?.mapNotNull { it.value as? ArgoWorkflowTemplateElement } ?: emptyList()
}

operator fun ArgoWorkflowTemplatesElement.iterator(): Iterator<ArgoWorkflowTemplateElement> = this.templates().iterator()

val YAMLDocument.templates: Iterable<ArgoWorkflowTemplatesElement> get() = this.resource?.get("spec")?.value<YAMLMapping>()?.get("templates")?.children?.mapNotNull { it as? ArgoWorkflowTemplatesElement } ?: emptyList()
