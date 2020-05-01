package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLScalar

class ArgoParameterPsi(
    override val psiElement: YAMLMapping,
    override val parentElement: ArgoParametersPsi
) : ArgoPsi<YAMLMapping>, HasNameArgoElement {

    override val children: Sequence<ArgoPsi<*>>
        get() = emptySequence() // TODO

    val value: String?
        get() = ((psiElement["value"]?.value) as YAMLScalar?)?.textValue

    override val yamlChildren
        get() = psiElement

}
