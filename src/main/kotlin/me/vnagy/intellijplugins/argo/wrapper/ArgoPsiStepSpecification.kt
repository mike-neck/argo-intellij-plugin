package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLSequenceItem
import org.jetbrains.yaml.psi.YAMLValue

class ArgoPsiStepSpecification(
    override val psiElement: YAMLSequenceItem,
    override val parentElement: ArgoPsiSteps
) : ArgoPsi<YAMLSequenceItem>,
    HasNameArgoElement,
    HasTemplateArgoElement<YAMLSequenceItem>,
    HasArgumentArgoElement<YAMLSequenceItem> {

    override val children: Sequence<ArgoPsi<*>>
        get() = sequenceOf(
            namePsiElement?.let { GenericArgoPsiWrapper(it, this) },
            templatePsiElement?.let { GenericArgoPsiWrapper(it, this) },
            arguments
        ).filterNotNull()

    override val yamlChildren: YAMLValue?
        get() = psiElement.children.firstOrNull()?.children?.firstOrNull()?.children?.firstOrNull() as? YAMLValue
}
