package me.vnagy.intellijplugins.argo.references.callsitetemplate

import com.intellij.psi.PsiElement
import me.vnagy.intellijplugins.argo.references.ArgoCallsitePsiReferenceBase
import me.vnagy.intellijplugins.argo.references.callsitetemplate.CallsiteTemplateNameReferenceProvider.Companion.logger
import me.vnagy.intellijplugins.argo.wrapper.parent
import org.jetbrains.yaml.psi.*

class CallsiteTemplateNameReference(element: PsiElement) : ArgoCallsitePsiReferenceBase(element) {

    val log = logger<CallsiteTemplateNameReference>()

    override fun resolve(): PsiElement? {
        val templateName = elementOfCallTemplateName()?.textValue ?: return null
        val templates = argoPsiFileWrapper?.templates ?: return null
        return templates.find { it.name == templateName }?.psiElement?.value
    }

    private fun elementOfCallTemplateName(): YAMLScalar? {
        val self = element
        if (self !is YAMLScalar) {
            return null
        }
        val localTemplate = self.parent<YAMLKeyValue>() ?: return null
        if (localTemplate.keyText != "template") {
            return null
        }
        if (localTemplate.value != self) {
            return null
        }
        val mayStep = localTemplate.parentMapping ?: return null
        val templateStep = mayStep.getTemplateStep() ?: return null
        return if (templateStep.keyText == "steps") self else null
    }

    companion object {
        fun YAMLMapping.getTemplateStep(): YAMLKeyValue? {
            return this
                .parent<YAMLSequenceItem>()
                .parent<YAMLSequence>()
                .parent<YAMLSequenceItem>()
                .parent<YAMLSequence>()
                .parent<YAMLKeyValue>()
        }
    }
}
