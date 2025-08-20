package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.find.findUsages.PsiElement2UsageTargetAdapter
import com.intellij.kubernetes.get
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.usages.UsageTarget
import com.intellij.usages.UsageTargetProvider
import org.jetbrains.yaml.YAMLLanguage
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalar
import org.jetbrains.yaml.psi.YAMLValue

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
        val element = file.findElementAt(offset)?.asArgoWorkflowTemplateNameElement() ?: return null
        return getTargets(yamlFile, element)
    }

    override fun getTargets(psiElement: PsiElement): Array<out UsageTarget?>? {
        val psiFile = psiElement.containingFile ?: return null
        val yamlFile = psiFile as? YAMLFile ?: return null
        val element = psiElement.asArgoWorkflowTemplateNameElement() ?: return null
        return getTargets(yamlFile, element)
    }

    fun getTargets(file: YAMLFile, nameElement: ArgoWorkflowTemplateNameElement): Array<out UsageTarget?>? {
        val argoWorkflowTemplates = nameElement.fromTemplateNameToTemplates() ?: return null
        val list = mutableListOf<UsageTarget>()
        for (template in argoWorkflowTemplates) {
            val steps = template.steps ?: continue
            val templateName = (template["name"] as? YAMLScalar)?.textValue ?: continue
            val singleSteps = steps.findSingleStepCallingLocalTemplate(templateName)
            singleSteps.forEach { step ->
                list.add(PsiElement2UsageTargetAdapter(step, true))
            }
        }
        return list.toTypedArray()
    }
}


val PsiElement.isYamlKeyValue: Boolean
    get() {
        if (this is YAMLKeyValue) {
            return true
        }
        return this.parent is YAMLKeyValue
    }

inline fun <reified  T: YAMLValue> YAMLKeyValue.value(): T? = this.value as? T
