package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLSequenceItem

class ArgoPsiDagDependency(
    override val psiElement: YAMLSequenceItem,
    override val parentElement: ArgoPsiDagTaskSpecification
) : ArgoPsi<YAMLSequenceItem> {

    override val children: Sequence<ArgoPsi<*>>
        get() = sequenceOf(
            valuePsiElement?.let { GenericArgoPsiWrapper(it, this) }
        ).filterNotNull()

    val value: String?
        get() = this.valuePsiElement?.text

    val valuePsiElement: PsiElement?
        get() = this.psiElement.children.firstOrNull()
}
