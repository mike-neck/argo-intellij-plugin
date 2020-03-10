package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement

class GenericArgoPsiWrapper(
    override val psiElement: PsiElement,
    override val parentElement: ArgoPsi<*>
) : ArgoPsi<PsiElement> {

    override val children: Sequence<ArgoPsi<*>>
        get() = emptySequence()

}
