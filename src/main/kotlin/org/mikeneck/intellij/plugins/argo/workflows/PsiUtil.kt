package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.kubernetes.get
import com.intellij.psi.PsiElement
import me.vnagy.intellijplugins.argo.wrapper.parent
import org.jetbrains.yaml.psi.*

fun YAMLValue?.parentNamed(key: String): YAMLKeyValue? = when (this) {
    null -> null
    else -> this.parent<YAMLKeyValue>().inCase { name == key }
}

fun PsiElement.asYAMLKeyValue(): YAMLKeyValue? = when (this) {
    is YAMLKeyValue -> this
    is YAMLScalar -> this.parent as? YAMLKeyValue
    else -> this.parent as? YAMLKeyValue
}

inline fun <reified T : YAMLValue> YAMLKeyValue.value(): T? = this.value as? T

inline fun <reified T : YAMLValue> YAMLValue?.value(): T? = this as? T

fun <T: Any> T?.inCase(condition: T.() -> Boolean): T? = if (this != null && condition()) this else null

fun YAMLMapping?.ifHasKeyValue(key: String, value: String): YAMLMapping? =
    this.inCase { this[key].value<YAMLScalar>().haValue(value) }

inline fun <reified T: YAMLValue> YAMLMapping?.getValue(key: String): T? = if (this == null) null else this[key] as? T

fun YAMLScalar?.haValue(text: String): Boolean = if (this == null) false else this.textValue == text

inline fun <reified T: YAMLValue> YAMLSequence?.getItems(): Iterable<T> = this?.items?.mapNotNull { it.value as? T } ?: emptyList()

fun Iterable<YAMLDocument>.ofWorkflowOrWorkflowTemplate(): Iterable<YAMLDocument> {
    return this.filter { document ->
        document.resource.asWorkflowOrWorkflowTemplate != null
    }
}

val YAMLDocument.resource: ArgoWorkflowElement? get() = this.topLevelValue as? ArgoWorkflowElement

enum class ArgoWorkflowType {
    Workflow,
    WorkflowTemplate,
    CronWorkflow,
}
