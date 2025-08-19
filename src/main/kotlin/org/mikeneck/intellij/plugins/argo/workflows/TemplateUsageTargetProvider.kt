package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.find.findUsages.PsiElement2UsageTargetAdapter
import com.intellij.kubernetes.findKeyValue
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.usages.UsageTarget
import com.intellij.usages.UsageTargetProvider
import me.vnagy.intellijplugins.argo.references.callsitetemplate.CallsiteTemplateNameReference.Companion.getTemplateStep
import me.vnagy.intellijplugins.argo.wrapper.parent
import org.jetbrains.yaml.YAMLLanguage
import org.jetbrains.yaml.psi.*

class TemplateUsageTargetProvider : UsageTargetProvider {

    override fun getTargets(
        editor: Editor,
        file: PsiFile
    ): Array<out UsageTarget?>? {
        if (file.language != YAMLLanguage.INSTANCE) {
            return null
        }
        val yamlFile = file as? YAMLFile ?: return null
        val caretModel = editor.caretModel
        val offset = caretModel.offset
        val element = file.findElementAt(offset)?.asArgoWorkflowTemplateNameKeyValue() ?: return null
        return getTargets(yamlFile, element)
    }

    override fun getTargets(psiElement: PsiElement): Array<out UsageTarget?>? {
        val psiFile = psiElement.containingFile ?: return null
        val yamlFile = psiFile as? YAMLFile ?: return null
        val element = psiElement.asArgoWorkflowTemplateNameKeyValue() ?: return null
        return getTargets(yamlFile, element)
    }

    fun getTargets(file: YAMLFile, nameElement: ArgoWorkflowTemplateNameElement): Array<out UsageTarget?>? {
        val argoWorkflowTemplates = nameElement.fromTemplateNameToTemplates() ?: return null
        val list = mutableListOf<UsageTarget>()
        for (template in argoWorkflowTemplates) {
            val steps: YAMLSequence = template.steps ?: continue
            // TODO val matchingSteps = steps filter { it.template == template.name }
            // list addAll << matchingSteps.map { PsiElement2UsageTargetAdapter(it, true) }
            val templateName = steps.findKeyValue("template") ?: continue
            if (nameElement.valueText == templateName.valueText) {
                list.add(PsiElement2UsageTargetAdapter())
            }
        }
    }
}

typealias ArgoWorkflowTemplateElement = YAMLMapping
typealias ArgoWorkflowTemplateNameElement = YAMLKeyValue
typealias ArgoWorkflowTemplatesElement = YAMLKeyValue
typealias ArgoWorkflowTemplateElementCollection = Iterable<ArgoWorkflowTemplateElement>

val PsiElement.isYamlKeyValue: Boolean
    get() {
        if (this is YAMLKeyValue) {
            return true
        }
        return this.parent is YAMLKeyValue
    }

fun PsiElement.asArgoWorkflowTemplateNameKeyValue(): ArgoWorkflowTemplateNameElement? {
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

fun ArgoWorkflowTemplateNameElement?.fromTemplateNameToTemplates(): ArgoWorkflowTemplatesElement? {
    return this.parent<YAMLMapping>()
        .parent<YAMLSequenceItem>()
        .parent<YAMLSequence>()
        .parent<YAMLKeyValue>()
}

fun ArgoWorkflowTemplatesElement.templates(): ArgoWorkflowTemplateElementCollection {
    return this.value<YAMLSequence>()?.items?.mapNotNull { it.value as? ArgoWorkflowTemplateElement } ?: emptyList()
}

operator fun ArgoWorkflowTemplatesElement.iterator(): Iterator<ArgoWorkflowTemplateElement> = this.templates().iterator()

inline fun <reified  T: YAMLValue> YAMLKeyValue.value(): T? = this.value as? T

