package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLScalar
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl

class ArgoParameterPsi(
    override val psiElement: YAMLMapping,
    override val parentElement: ArgoPsi<*>
) : ArgoPsi<YAMLMapping>, HasNameArgoElement {

    override val children: Sequence<ArgoPsi<*>>
        get() = emptySequence() // TODO

    val value: String?
        get() = ((psiElement["value"]?.value) as YAMLScalar?)?.textValue

    override val yamlChildren
        get() = psiElement

}
