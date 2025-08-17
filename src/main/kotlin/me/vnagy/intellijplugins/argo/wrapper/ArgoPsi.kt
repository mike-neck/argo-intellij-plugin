package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

interface ArgoPsi<T : PsiElement> {
    val psiElement: T
    val parentElement: ArgoPsi<*>?
    val children: Sequence<ArgoPsi<*>>

    fun findChildrenForPsiElement(psiElement: PsiElement?): ArgoPsi<*>? {
        return when(psiElement) {
            null -> null
            this.psiElement -> this
            else -> findChildRecursive(psiElement, children.iterator())
        }
    }

    fun <T : ArgoPsi<*>> findParentOfType(klass: KClass<T>): T? {
        return if (klass.isInstance(this)) {
            klass.cast(this)
        } else {
            parentElement?.findParentOfType(klass)
        }
    }

    companion object {
        fun <T: Any> Iterator<T>.tryNext(): T? = if (!hasNext()) null
        else try { next() } catch (_: NoSuchElementException) { null }

        tailrec fun findChildRecursive(target: PsiElement, iterator: Iterator<ArgoPsi<*>>): ArgoPsi<*>? {
            val next = iterator.tryNext() ?: return null
            return if (next.psiElement == target) next else findChildRecursive(target, iterator)
        }

    }
}
