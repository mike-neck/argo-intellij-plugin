package org.mikeneck.intellij.plugins.argo.workflows

import org.jetbrains.yaml.psi.YAMLKeyValue

typealias ArgoWorkflowStepNameElement = YAMLKeyValue

fun ArgoWorkflowStepNameElement.stepName(): String = this.keyText.trim()
