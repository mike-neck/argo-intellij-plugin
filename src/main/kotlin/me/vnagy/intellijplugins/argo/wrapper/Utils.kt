package me.vnagy.intellijplugins.argo.wrapper

import me.vnagy.intellijplugins.argo.exceptions.NonUniqueResultException
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLValue
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.full.safeCast

operator fun YAMLValue?.get(key: String): YAMLKeyValue? {
    return this?.children
        ?.asSequence()
        ?.map { it as? YAMLKeyValue }
        ?.filterNotNull()
        ?.filter { it.keyText == key }
        ?.uniqueOrNull()
}

operator fun <T> Array<T>?.get(index: Int): T? {
    if (this != null && this.size > index) {
         return get(index)
    } else {
        return null
    }
}

fun <T> Sequence<T>.uniqueOrNull(): T? {
    if (count() > 1) {
        throw NonUniqueResultException("")
    }
    return firstOrNull()
}

fun <T> Collection<T>.uniqueOrNull(): T? {
    if (count() > 1) {
        throw NonUniqueResultException("")
    }
    return firstOrNull()
}
