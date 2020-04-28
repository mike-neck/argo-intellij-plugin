package me.vnagy.intellijplugins.argo.references

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsi
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiDagTaskSpecification
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiFileWrapper
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiStepSpecification
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.YAMLKeyValue

abstract class ArgoCallsitePsiReferenceBase(element: PsiElement) : PsiReferenceBase<PsiElement>(element) {

    protected val argoPsiFileWrapper: ArgoPsiFileWrapper?

    init {
        val containingFile = element.containingFile
        if (containingFile is YAMLFile) {
            argoPsiFileWrapper = ArgoPsiFileWrapper(containingFile)
        } else {
            argoPsiFileWrapper = null
        }
    }

    protected fun isThisADagWorkFlowChild(): Boolean {
        val argoPsiElement = argoPsiFileWrapper?.findChildrenForPsiElement(myElement)
        if (argoPsiElement is ArgoPsi<*>) {
            return argoPsiElement.findParentOfType(ArgoPsiDagTaskSpecification::class) != null
        }
        return false
    }

    protected fun parentElementKeyIs(key: String): Boolean {
        val parentPsiElement = myElement.parent
        return parentElementKeyIs(parentPsiElement, key)
    }

    private tailrec fun parentElementKeyIs(parentPsiElement: PsiElement?, key: String): Boolean {
        if (parentPsiElement is YAMLKeyValue) {
            return parentPsiElement.keyText == key
        } else if (parentPsiElement != null) {
            return parentElementKeyIs(parentPsiElement.parent, key)
        } else {
            return false
        }
    }

    protected fun isThisAStepWorkFlowChild(): Boolean {
        val argoPsiElement = argoPsiFileWrapper?.findChildrenForPsiElement(myElement)
        if (argoPsiElement is ArgoPsi<*>) {
            return argoPsiElement.findParentOfType(ArgoPsiStepSpecification::class) != null
        }
        return false
    }
}
