package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLValue

interface HasArgumentArgoElement<T : PsiElement> : ArgoPsi<T> {

    val arguments: ArgoArgumentsPsiWrapper?
        get() = (yamlChildren["arguments"].value<YAMLMapping>())
            ?.let { ArgoArgumentsPsiWrapper(it, this) }

    val yamlChildren: YAMLValue?
}

inline fun <reified T : PsiElement, E : PsiElement> HasArgumentArgoElement<E>.yamlChildValue(key: String): T? =
    this.yamlChildren[key].value<T>()
