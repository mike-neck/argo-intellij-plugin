package me.vnagy.intellijplugins.argo.references.workflowname

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import me.vnagy.intellijplugins.argo.wrapper.*
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
        val argoPsiElement = argoPsiFileWrapper?.findChildrenForPsiElement(myElement)
        if (argoPsiElement is ArgoPsi<*>) {
            return parentElementKeyIs("template") && argoPsiElement.findParentOfType(ArgoPsiDagTaskSpecification::class) != null
        }
        return false
    }

    private fun parentElementKeyIs(key: String): Boolean {
        val parentPsiElement = myElement.parent
        if (parentPsiElement is YAMLKeyValue) {
            return parentPsiElement.keyText == key
        }
        return false
    }

    private fun isThisElementAStepTemplateReference(): Boolean {
        val argoPsiElement = argoPsiFileWrapper?.findChildrenForPsiElement(myElement)
        if (argoPsiElement is ArgoPsi<*>) {
            return parentElementKeyIs("template") && argoPsiElement.findParentOfType(ArgoPsiStepSpecification::class) != null
        }
        return false
    }

}
