package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement

interface ArgoPsi<T : PsiElement> {
    val psiElement: T
    val parentElement: ArgoPsi<*>?
    val children: Sequence<ArgoPsi<*>>

    fun findChildrenForPsiElement(psiElement: PsiElement): ArgoPsi<*>? {
        return if (this.psiElement == psiElement) {
            this
        } else {
            children
                .map { it.findChildrenForPsiElement(psiElement) }
                .filterNotNull()
                .uniqueOrNull()
        }
    }
}
