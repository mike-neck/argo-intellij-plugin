package me.vnagy.intellijplugins.argo.references.workflowname

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import me.vnagy.intellijplugins.argo.wrapper.*
import org.jetbrains.yaml.psi.YAMLFile
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
        return argoPsiFileWrapper
            ?.findChildrenForPsiElement(myElement)
            ?.let { findParentOfType(ArgoPsiDagTaskSpecification::class, it) } != null
    }

    private fun isThisElementAStepTemplateReference(): Boolean {
        return argoPsiFileWrapper
            ?.findChildrenForPsiElement(myElement)
            ?.let { findParentOfType(ArgoPsiStepSpecification::class, it) } != null
    }

}
