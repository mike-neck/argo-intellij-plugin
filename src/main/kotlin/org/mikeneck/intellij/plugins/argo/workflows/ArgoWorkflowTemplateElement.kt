package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.kubernetes.get
import me.vnagy.intellijplugins.argo.wrapper.parent
import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLScalar
import org.jetbrains.yaml.psi.YAMLSequence
import org.jetbrains.yaml.psi.YAMLSequenceItem

typealias ArgoWorkflowTemplateElement = YAMLMapping

val ArgoWorkflowTemplateElement?.resourceRoot: ArgoWorkflowElement? get() =
    this.parentNamed("spec")
        .parent<YAMLMapping>()
        .asWorkflowOrWorkflowTemplate

val ArgoWorkflowTemplateElement.steps: ArgoWorkflowTemplateStepElement? get() {
    val steps = this["steps"] as? YAMLSequence ?: return null
    return steps.items
        .mapNotNull { it.value as? YAMLSequence }
        .map { it.items.mapNotNull { item -> item.value as? YAMLMapping } }
}

val ArgoWorkflowTemplateElement?.collection: ArgoWorkflowTemplateElementCollection? get() {
    val sequence = this.parent<YAMLSequenceItem>()
        .parent<YAMLSequence>()
        ?: return null
    return sequence.items.mapNotNull { it.value as? ArgoWorkflowTemplateElement }
}

val ArgoWorkflowTemplateElement?.templateName: String? get() = this?.getValue<YAMLScalar>("name")?.textValue

val ArgoWorkflowTemplateElement?.templateNameElement: YAMLScalar? get() = this?.getValue<YAMLScalar>("name")
