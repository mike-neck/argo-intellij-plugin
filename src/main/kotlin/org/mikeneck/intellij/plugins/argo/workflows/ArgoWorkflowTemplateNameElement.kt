package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.psi.PsiElement
import me.vnagy.intellijplugins.argo.wrapper.parent
import org.jetbrains.yaml.psi.*

typealias ArgoWorkflowTemplateNameElement = YAMLKeyValue

fun ArgoWorkflowTemplateNameElement?.fromTemplateNameToTemplates(): ArgoWorkflowTemplatesElement? {
    return this.parent<YAMLMapping>()
        .parent<YAMLSequenceItem>()
        .parent<YAMLSequence>()
        .parent<YAMLKeyValue>()
        .ifHasKey("templates")
}

fun YAMLKeyValue?.ifHasKey(key: String): YAMLKeyValue? {
    return when(this?.keyText) {
        key -> this
        else -> null
    }
}

fun PsiElement.asArgoWorkflowTemplateNameElement(): ArgoWorkflowTemplateNameElement? {
    if (this !is YAMLScalar && !(this.isYamlKeyValue) && this !is YAMLKeyValue) {
        return null
    }
    var element = this
    while (element !is ArgoWorkflowTemplateNameElement) {
        element = element.parent ?: return null
    }
    if (element.keyText != "name") {
        return null
    }
    val templates = element.fromTemplateNameToTemplates() ?: return null
    return if (templates.keyText == "templates") element else null
}
