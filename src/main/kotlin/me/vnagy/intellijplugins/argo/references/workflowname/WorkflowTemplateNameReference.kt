package me.vnagy.intellijplugins.argo.references.workflowname

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiFileWrapper
import me.vnagy.intellijplugins.argo.wrapper.uniqueOrNull
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalar

class WorkflowTemplateNameReference(element: PsiElement) : PsiReferenceBase<PsiElement>(element) {

    private val argoPsiFileWrapper: ArgoPsiFileWrapper?

    init {
        val containingFile = element.containingFile
        if (containingFile is YAMLFile) {
            argoPsiFileWrapper = ArgoPsiFileWrapper(containingFile)
        } else {
            argoPsiFileWrapper = null
        }
    }

    override fun resolve(): PsiElement? {
        if (isThisElementADagTemplateReference() || isThisElementAStepTemplateReference()) {
            val referencedTemplateName = (myElement as YAMLScalar).textValue
            val templates = argoPsiFileWrapper
                ?.spec
                ?.templates ?: listOf()


            return templates
                .asSequence()
                .filter { it.name == referencedTemplateName }
                .uniqueOrNull()
                ?.namePsiElement
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
