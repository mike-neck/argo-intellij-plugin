package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalar
import org.jetbrains.yaml.psi.YAMLValue

fun PsiElement.asYAMLKeyValue(): YAMLKeyValue? = when (this) {
    is YAMLKeyValue -> this
    is YAMLScalar -> this.parent as? YAMLKeyValue
    else -> this.parent as? YAMLKeyValue
}

inline fun <reified  T: YAMLValue> YAMLKeyValue.value(): T? = this.value as? T
