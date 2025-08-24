package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.kubernetes.get
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.*

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

fun YAMLScalar?.haValue(text: String): Boolean = if (this == null) false else this.textValue == text

fun Iterable<YAMLDocument>.ofWorkflowTemplate(): Iterable<YAMLDocument> {
    return this.filter { document ->
        document.resource.of(ArgoWorkflowType.WorkflowTemplate) != null
    }
}

val YAMLDocument.resource: YAMLMapping? get() = this.topLevelValue as? YAMLMapping

fun YAMLMapping?.of(type: ArgoWorkflowType): YAMLMapping? = this
    .ifHasKeyValue("apiVersion", "argoproj.io/v1alpha1")
    .ifHasKeyValue("kind", type.name)

enum class ArgoWorkflowType {
    Workflow,
    WorkflowTemplate,
    CronWorkflow,
}
