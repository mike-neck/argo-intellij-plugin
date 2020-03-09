package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement

interface ArgoPsi<T: PsiElement> {
    val psiElement: T
}
