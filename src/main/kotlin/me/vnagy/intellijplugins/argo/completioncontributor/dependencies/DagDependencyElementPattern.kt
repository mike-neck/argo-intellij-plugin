package me.vnagy.intellijplugins.argo.completioncontributor.dependencies

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.ElementPatternCondition
import com.intellij.patterns.InitialPatternCondition
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import me.vnagy.intellijplugins.argo.completioncontributor.DEPENDENCIES_ELEMENT
import me.vnagy.intellijplugins.argo.completioncontributor.PARAMETERS_ELEMENT
import me.vnagy.intellijplugins.argo.wrapper.ArgoParameterPsi
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiDagDependency
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiFileWrapper
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLSequenceItem
import org.jetbrains.yaml.psi.impl.YAMLBlockMappingImpl

class DagDependencyElementPattern :
    InitialPatternCondition<PsiElement>(PsiElement::class.java),
    ElementPattern<PsiElement> {

    override fun accepts(o: Any?, context: ProcessingContext): Boolean {
        if (o is PsiElement) {
            val containingFile = o.containingFile
            if (containingFile is YAMLFile) {
                val argoPsiWrapper = ArgoPsiFileWrapper(containingFile)
                val dependenciesParent = argoPsiWrapper.findChildrenForPsiElement(o.parentOfType<YAMLSequenceItem>())
                if (dependenciesParent is ArgoPsiDagDependency && dependenciesParent.psiElement == o.parent.parent) {
                    context.put(DEPENDENCIES_ELEMENT, dependenciesParent)
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
