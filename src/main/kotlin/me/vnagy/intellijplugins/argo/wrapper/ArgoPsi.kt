package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

interface ArgoPsi<T : PsiElement> {
    val psiElement: T
    val parentElement: ArgoPsi<*>?
    val children: Sequence<ArgoPsi<*>>

    fun findChildrenForPsiElement(psiElement: PsiElement?): ArgoPsi<*>? {
        return when {
            psiElement == null -> {
                null
            }
            this.psiElement == psiElement -> {
                this
            }
            else -> {
                children
                    .map { it.findChildrenForPsiElement(psiElement) }
                    .filterNotNull()
                    .uniqueOrNull()
            }
        }
    }

    fun <T : ArgoPsi<*>> findParentOfType(klass: KClass<T>): T? {
        return if (klass.isInstance(this)) {
            klass.cast(this)
        } else {
            parentElement?.findParentOfType(klass)
        }
    }
}
