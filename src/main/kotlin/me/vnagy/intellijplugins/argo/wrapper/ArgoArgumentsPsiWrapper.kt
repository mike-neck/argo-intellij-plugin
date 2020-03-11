package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLSequence

class ArgoArgumentsPsiWrapper(
    override val psiElement: YAMLMapping,
    override val parentElement: HasArgumentArgoElement<*>
) : ArgoPsi<YAMLMapping> {

    val parameters: ArgoParametersPsi?
        get() = psiElement["parameters"]?.let { ArgoParametersPsi(it, this) }

    override val children: Sequence<ArgoPsi<*>>
        get() = sequenceOf(parameters)
            .filterNotNull()

}
