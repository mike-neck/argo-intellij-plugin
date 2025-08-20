package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.kubernetes.get
import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLSequence

typealias ArgoWorkflowTemplateElement = YAMLMapping

val ArgoWorkflowTemplateElement.steps: ArgoWorkflowTemplateStepElement? get() {
    val steps = this["steps"] as? YAMLSequence ?: return null
    return steps.items
        .mapNotNull { it.value as? YAMLSequence }
        .map { it.items.mapNotNull { item -> item.value as? YAMLMapping } }
}
