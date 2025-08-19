package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import kotlin.reflect.KClass


inline fun <reified T: Any> logger(): Logger = loggerMap.computeIfAbsent(T::class) {
    Logger.getInstance(T::class.simpleName ?: T::class.java.simpleName)
}

val loggerMap: MutableMap<KClass<*>, Logger> = mutableMapOf()

inline fun <reified T: Any> log(element: PsiElement, message: String) = logger<T>().info(
    "element: ${element.formatLogString()} - $message"
)

fun PsiElement.onelineString(): String = when(val index = this.text?.indexOf('\n') ?: -2) {
    -2 -> "<empty ${this.javaClass.simpleName}>"
    -1 -> this.text
    else -> "${this.text.substring(0, index)}..."
}
fun PsiElement.formatLogString() = "${this.javaClass.simpleName} -> ${this.parent?.javaClass?.simpleName ?: "[no-parent]"}(${this.containingFile?.name})'${this.onelineString()}' - ${this.children.size}"
