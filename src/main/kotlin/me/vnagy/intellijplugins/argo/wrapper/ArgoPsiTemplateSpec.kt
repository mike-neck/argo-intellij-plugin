package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLPsiElement
import org.jetbrains.yaml.psi.YAMLSequence
import org.jetbrains.yaml.psi.YAMLValue

class ArgoPsiTemplateSpec(
    override val psiElement: YAMLPsiElement,
    override val parentElement: ArgoPsiSpec
) : ArgoPsi<YAMLPsiElement>, HasNameArgoElement {

    override val children: Sequence<ArgoPsi<*>>
        get() = sequenceOf(
            namePsiElement?.let { GenericArgoPsiWrapper(it, this) },
            dag,
            steps
        ).filterNotNull()

    val dag: ArgoPsiDagTask?
        get() = (yamlChildren["dag"] as? YAMLMapping)?.let { ArgoPsiDagTask(it, this) }

    val steps: ArgoPsiSteps?
        get() = (yamlChildren["steps"] as? YAMLSequence)?.let { ArgoPsiSteps(it, this) }

    override val yamlChildren
        get() = psiElement.children[0] as YAMLValue
}
