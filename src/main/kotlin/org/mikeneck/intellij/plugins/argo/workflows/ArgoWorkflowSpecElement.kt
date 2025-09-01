package org.mikeneck.intellij.plugins.argo.workflows

import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLSequence

typealias ArgoWorkflowSpecElement = YAMLMapping

val ArgoWorkflowSpecElement?.templates: ArgoWorkflowTemplateElementCollection get() =
    this.getValue<YAMLSequence>("templates")
        .getItems<ArgoWorkflowTemplateElement>()
