package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLSequenceItem
import org.jetbrains.yaml.psi.YAMLValue

class ArgoPsiDagTaskSpecification(
    override val psiElement: YAMLSequenceItem,
    override val parentElement: ArgoPsiDagTask
) : ArgoPsi<YAMLSequenceItem>, HasNameArgoElement, HasTemplateArgoElement {

    override val children: Sequence<ArgoPsi<*>>
        get() = sequenceOf(
            namePsiElement?.let { GenericArgoPsiWrapper(it, this) },
            templatePsiElement?.let { GenericArgoPsiWrapper(it, this) }
        ).filterNotNull()

    override val yamlChildren
        get() = psiElement.children[0] as YAMLValue
}
