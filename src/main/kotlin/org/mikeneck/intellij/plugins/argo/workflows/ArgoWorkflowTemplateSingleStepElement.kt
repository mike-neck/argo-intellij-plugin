package org.mikeneck.intellij.plugins.argo.workflows

import me.vnagy.intellijplugins.argo.wrapper.parent
import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLSequence
import org.jetbrains.yaml.psi.YAMLSequenceItem

typealias ArgoWorkflowTemplateSingleStepElement = YAMLMapping

val ArgoWorkflowTemplateSingleStepElement?.upToSteps: ArgoWorkflowTemplateStepElement?
    get() {
        val rawSteps = this
            .parent<YAMLSequenceItem>()
            .parent<YAMLSequence>()
            .parent<YAMLSequenceItem>()
            .parent<YAMLSequence>() ?: return null
        return rawSteps.items.mapNotNull { sameSteps ->
            val steps = sameSteps.value as? YAMLSequence ?: return@mapNotNull null
            steps.items.mapNotNull { item -> item.value as? ArgoWorkflowTemplateSingleStepElement }
        }
    }

val ArgoWorkflowTemplateSingleStepElement?.upToTemplate: ArgoWorkflowTemplateElement? get() {
    val rawSteps = this
        .parent<YAMLSequenceItem>()
        .parent<YAMLSequence>()
        .parent<YAMLSequenceItem>()
        .parent<YAMLSequence>() ?: return null
    return rawSteps
        .parentNamed("steps")
        .parent<YAMLMapping>()
}
