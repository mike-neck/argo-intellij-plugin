package org.mikeneck.intellij.plugins.argo.workflows

import me.vnagy.intellijplugins.argo.wrapper.parent
import org.jetbrains.yaml.psi.*

typealias ArgoWorkflowElement = YAMLMapping

val ArgoWorkflowTemplateElement?.upToWorkflowOrWorkflowTemplate: ArgoWorkflowElement? get() {
    return this
        .parent<YAMLSequenceItem>()
        .parent<YAMLSequence>()
        .parent<YAMLKeyValue>()
        .ifHasKey("templates")
        .parent<YAMLMapping>()
        .parent<YAMLKeyValue>()
        .ifHasKey("spec")
        .parent<ArgoWorkflowElement>()
        .asWorkflowOrWorkflowTemplate
}

val YAMLMapping?.asWorkflowOrWorkflowTemplate: ArgoWorkflowElement? get() = this.asArgoWorkflowTemplate ?: this.asArgoWorkflow

val YAMLMapping?.asArgoWorkflow: ArgoWorkflowElement? get() = this.of(ArgoWorkflowType.Workflow)

val YAMLMapping?.asArgoWorkflowTemplate: ArgoWorkflowElement? get() = this.of(ArgoWorkflowType.WorkflowTemplate)

fun ArgoWorkflowElement?.of(type: ArgoWorkflowType): YAMLMapping? = this
    .ifHasKeyValue("apiVersion", "argoproj.io/v1alpha1")
    .ifHasKeyValue("kind", type.name)

val ArgoWorkflowElement?.workflowName: String get() {
    return this
        .getValue<YAMLMapping>("metadata")
        .getValue<YAMLScalar>("name")
        ?.textValue ?: ""
}

val ArgoWorkflowElement?.spec: ArgoWorkflowSpecElement? get() = this?.getValue("spec")
