package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.openapi.diagnostic.logger
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLPsiElement
import org.jetbrains.yaml.psi.YAMLValue

operator fun YAMLValue?.get(key: String): YAMLKeyValue? {
    if (this !is YAMLMapping) {
        return null
    }
    return this.children
        .asSequence()
        .mapNotNull { it as? YAMLKeyValue }
        .filter { it.keyText == key }
        .uniqueOrNull()
}

inline fun <reified P: YAMLPsiElement> YAMLPsiElement?.parent(): P? =
    when (this) {
        null -> null
        else -> this.parent as? P
    }

inline fun <reified T: PsiElement> YAMLKeyValue?.value(): T? {
    return this?.value as? T
}

operator fun <T> Array<T>?.get(index: Int): T? {
    return if (this != null && this.size > index) {
        get(index)
    } else {
        null
    }
}

inline fun <reified T: Any> Sequence<T>.uniqueOrNull(): T? {
    val count = count()
    if (count > 1) {
        logger<T>().warn("Sequence contains $count ${T::class.simpleName}")
    }
    return firstOrNull()
}

inline fun <reified T: Any> Collection<T>.uniqueOrNull(): T? {
    if (size > 1) {
        logger<T>().warn("Collection contains $size ${T::class.simpleName}")
    }
    return firstOrNull()
}
