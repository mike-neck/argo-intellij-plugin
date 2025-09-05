package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.psi.PsiElement
import me.vnagy.intellijplugins.argo.wrapper.parent
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLSequence
import org.jetbrains.yaml.psi.YAMLSequenceItem

typealias ArgoWorkflowTemplateNameElement = YAMLKeyValue

fun ArgoWorkflowTemplateNameElement?.fromTemplateNameToTemplates(): ArgoWorkflowTemplatesElement? {
    return this.parent<YAMLMapping>()
        .parent<YAMLSequenceItem>()
        .parent<YAMLSequence>()
        .parent<YAMLKeyValue>()
        .ifHasKey("templates")
}

val ArgoWorkflowTemplateNameElement?.template: ArgoWorkflowTemplateElement? get() = this.parent<ArgoWorkflowTemplateElement>()

val ArgoWorkflowTemplateNameElement?.templateCollection: ArgoWorkflowTemplateElementCollection get() {
    val seq = this.parent<YAMLMapping>()
        .parent<YAMLSequenceItem>()
        .parent<YAMLSequence>()
    return seq.getItems<ArgoWorkflowTemplateElement>()
}

fun YAMLKeyValue?.ifHasKey(key: String): YAMLKeyValue? {
    return when(this?.keyText) {
        key -> this
        else -> null
    }
}

fun YAMLKeyValue?.hasKey(key: String): Boolean = this?.keyText == key

fun PsiElement.asArgoWorkflowTemplateNameElement(): ArgoWorkflowTemplateNameElement? {
    val element = this.asYAMLKeyValue() ?: return null
    val templates = element
        .ifHasKey("name")
        .fromTemplateNameToTemplates() ?: return null
    return if (templates.hasKey("templates")) element else null
}
