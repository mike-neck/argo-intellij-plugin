package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLMapping

class ArgoParameterPsi(
    override val psiElement: YAMLMapping,
    override val parentElement: ArgoPsi<*>
) : ArgoPsi<YAMLMapping> {

    val name: String?
        get() = psiElement["name"]?.valueText

    override val children: Sequence<ArgoPsi<*>>
        get() = emptySequence() // TODO

}
