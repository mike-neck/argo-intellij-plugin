package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLValue


val PsiElement.isYamlKeyValue: Boolean
    get() {
        if (this is YAMLKeyValue) {
            return true
        }
        return this.parent is YAMLKeyValue
    }

inline fun <reified  T: YAMLValue> YAMLKeyValue.value(): T? = this.value as? T
