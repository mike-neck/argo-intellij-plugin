package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.*

class ArgoPsiTemplateSpec(
    override val psiElement: YAMLSequenceItem, // YAMLSequenceItem
    override val parentElement: ArgoPsiSpec
) : ArgoPsi<YAMLPsiElement>, HasNameArgoElement {

    override val children: Sequence<ArgoPsi<*>>
        get() = sequenceOf(
            namePsiElement?.let { GenericArgoPsiWrapper(it, this) },
            dag,
            steps,
            inputs
        ).filterNotNull()

    val inputs: ArgoInputsWrapper?
        get() = (yamlChildren["inputs"]?.value as? YAMLMapping)?.let { ArgoInputsWrapper(it, this) }

    val dag: ArgoPsiDagTask?
        get() = (yamlChildren["dag"]?.value as? YAMLMapping)?.let { ArgoPsiDagTask(it, this) }

    val steps: ArgoPsiSteps?
        get() = (yamlChildren["steps"]?.value as? YAMLSequence)?.let { ArgoPsiSteps(it, this) }

    override val yamlChildren: YAMLValue?
        get() = psiElement.children.firstOrNull() as? YAMLValue
}
