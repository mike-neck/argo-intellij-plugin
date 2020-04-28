package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLSequenceItem

class ArgoPsiDagDependency(
    override val psiElement: YAMLSequenceItem,
    override val parentElement: ArgoPsiDagTaskSpecification
): ArgoPsi<YAMLSequenceItem> {

    override val children: Sequence<ArgoPsi<*>>
        get() = psiElement.children.asSequence().map { GenericArgoPsiWrapper(it, this) }

}
