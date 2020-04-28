package me.vnagy.intellijplugins.argo.references.callsitetemplate

import com.intellij.psi.PsiElement
import me.vnagy.intellijplugins.argo.references.ArgoCallsitePsiReferenceBase
import org.jetbrains.yaml.psi.YAMLScalar

class CallsiteTemplateNameReference(element: PsiElement) : ArgoCallsitePsiReferenceBase(element) {

    override fun resolve(): PsiElement? {
        if (isThisATemplateReference()) {
            val referencedTemplateName = (myElement as YAMLScalar).textValue
            return argoPsiFileWrapper
                ?.spec
                ?.getTemplateByName(referencedTemplateName)
                ?.namePsiElement
        }
        return null
    }

    private fun isThisATemplateReference(): Boolean {
        if (isThisADagWorkFlowChild() || isThisAStepWorkFlowChild()) {
            return parentElementKeyIs("template")
        }
        return false
    }

}
