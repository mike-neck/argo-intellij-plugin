package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLMapping

class ArgoInputsWrapper(
    override val psiElement: YAMLMapping,
    override val parentElement: ArgoPsiTemplateSpec
) : ArgoPsi<YAMLMapping> {

    val parameters: ArgoParametersPsi?
        get() = psiElement["parameters"]?.let { ArgoParametersPsi(it, this) }

    override val children: Sequence<ArgoPsi<*>>
        get() = sequenceOf(parameters)
            .filterNotNull()

}
