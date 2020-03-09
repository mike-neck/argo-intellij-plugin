package me.vnagy.intellijplugins.argo.references.workflowname

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import me.vnagy.intellijplugins.argo.util.isWorkflowFile
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalar
import org.jetbrains.yaml.psi.YAMLSequenceItem

class WorkflowTemplateNameReference(element: PsiElement) : PsiReferenceBase<PsiElement>(element) {


    init {
        if (isWorkflowFile(element.containingFile)) {
            // ???
        }
    }

    override fun resolve(): PsiElement? {
        if (isThisElementADagTemplateReference() || isThisElementAStepTemplateReference()) {
            val referencedTemplateName = (myElement as YAMLScalar).textValue
            val templates = myElement
                .parent
                .parent
                .parent
                .parent
                .parent
                .parent
                .parent
                .parent
                .parent
                .parent
                .children
                .filterIsInstance<YAMLSequenceItem>() // TODO do something with these many `.parent` calls

            return templates
                .flatMap { it.children.asList() }
                .flatMap { it.children.asList() }
                .filterIsInstance<YAMLKeyValue>()
                .filter { it.keyText == "name" }
                .filter { it.valueText == referencedTemplateName }
                .firstOrNull()
                ?.value
        }
        return null
    }

    private fun isThisElementADagTemplateReference(): Boolean {
        val currentElementParent = myElement.parent
        if (currentElementParent is YAMLKeyValue) {
            if (currentElementParent.keyText == "template") {
                if ((currentElementParent.parent.parent.parent.parent.parent.parent as? YAMLKeyValue)?.keyText == "dag")
                    return true
            }
        }
        return false
    }

    private fun isThisElementAStepTemplateReference(): Boolean {
        val currentElementParent = myElement.parent
        if (currentElementParent is YAMLKeyValue) {
            if (currentElementParent.keyText == "template") {
                if ((currentElementParent.parent.parent.parent.parent.parent.parent as? YAMLKeyValue)?.keyText == "steps")
                    return true
            }
        }
        return false
    }

}
