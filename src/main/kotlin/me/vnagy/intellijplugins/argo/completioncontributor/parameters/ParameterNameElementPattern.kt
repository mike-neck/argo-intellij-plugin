package me.vnagy.intellijplugins.argo.completioncontributor.parameters

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.ElementPatternCondition
import com.intellij.patterns.InitialPatternCondition
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import me.vnagy.intellijplugins.argo.wrapper.ArgoParameterPsi
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiFileWrapper
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.impl.YAMLBlockMappingImpl

class ParameterNameElementPattern :
    InitialPatternCondition<PsiElement>(PsiElement::class.java),
    ElementPattern<PsiElement> {

    override fun accepts(o: Any?, context: ProcessingContext): Boolean {
        if (o is PsiElement) {
            val containingFile = o.containingFile
            if (containingFile is YAMLFile) {
                val argoPsiWrapper = ArgoPsiFileWrapper(containingFile)
                val parameterParent = argoPsiWrapper.findChildrenForPsiElement(o.parentOfType<YAMLBlockMappingImpl>())
                if (parameterParent is ArgoParameterPsi && parameterParent.namePsiElement == o.parent) {
                    context.put(PARAMETERS_ELEMENT, parameterParent)
                    return true
                }
            }
        }
        return false
    }

    override fun accepts(o: Any?): Boolean {
        return false
    }

    override fun getCondition(): ElementPatternCondition<PsiElement> {
        return ElementPatternCondition(this)
    }
}
