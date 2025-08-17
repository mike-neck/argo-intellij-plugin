package me.vnagy.intellijplugins.argo.references.callsitetemplate

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext
import kotlin.reflect.KClass

class CallsiteTemplateNameReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        log(element)
        return arrayOf(CallsiteTemplateNameReference(element))
    }

    companion object {
        val logger = Logger.getInstance(CallsiteTemplateNameReferenceProvider::class.simpleName ?: "CallsiteTemplateNameReferenceProvider")

        fun log(element: PsiElement) = logger.info(
            "element: ${element.formatLogString()}"
        )

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
    }
}
