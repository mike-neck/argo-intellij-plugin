package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping

class ArgoParametersPsi(
    override val psiElement: YAMLKeyValue,
    override val parentElement: ArgoPsi<*>
) : ArgoPsi<YAMLKeyValue> { // TODO

    val parameters: Sequence<ArgoParameterPsi>
        get() {
            return psiElement
                .value
                ?.children
                ?.asSequence()
                ?.flatMap { it.children.asSequence() }
                ?.asSequence()
                ?.map { it as YAMLMapping }
                ?.map { ArgoParameterPsi(it, this) } ?: emptySequence()
        }
    override val children: Sequence<ArgoPsi<*>>
        get() = parameters

}
