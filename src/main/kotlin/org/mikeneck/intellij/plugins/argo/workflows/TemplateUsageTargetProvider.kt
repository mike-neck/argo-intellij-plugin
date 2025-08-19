package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.usages.UsageTarget
import com.intellij.usages.UsageTargetProvider
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

    fun getTargets(file: YAMLFile, element: YAMLKeyValue): Array<out UsageTarget?>? {
        TODO("Not yet implemented")
    }
}

val PsiElement.isYamlKeyValue: Boolean
    get() {
        if (this is YAMLKeyValue) {
            return true
        }
        return this.parent is YAMLKeyValue
    }

fun PsiElement.asArgoWorkflowTemplateNameKeyValue(): YAMLKeyValue? {
    if (this !is YAMLScalar && !(this.isYamlKeyValue) && this !is YAMLKeyValue) {
        return null
    }
    var element = this
    while (element !is YAMLKeyValue) {
        element = element.parent ?: return null
    }
    if (element.keyText != "name") {
        return null
    }
    val templates = (element.parent<YAMLMapping>()
        .parent<YAMLSequenceItem>()
        .parent<YAMLSequence>()
        .parent<YAMLKeyValue>()
        ?: return null)
    return if (templates.keyText == "templates") element else null
}
