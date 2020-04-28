package me.vnagy.intellijplugins.argo.references.callsitedependson

import com.intellij.psi.PsiElement
import me.vnagy.intellijplugins.argo.references.ArgoCallsitePsiReferenceBase
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiDagTask
import org.jetbrains.yaml.psi.YAMLScalar

class CallsiteDependsOnReference(element: PsiElement) : ArgoCallsitePsiReferenceBase(element) {

    override fun resolve(): PsiElement? {
        if (isThisElementADependsOnText()) {
            val referencedTemplateName = (myElement as YAMLScalar).textValue
            val dagSpecification = argoPsiFileWrapper
                ?.findChildrenForPsiElement(myElement)
                ?.findParentOfType(ArgoPsiDagTask::class)

            return dagSpecification
                ?.getTaskByName(referencedTemplateName)
                ?.namePsiElement
        }
        return null
    }

    private fun isThisElementADependsOnText(): Boolean {
        if (isThisADagWorkFlowChild()) {
            return parentElementKeyIs("dependencies")
        }
        return false
    }
}
