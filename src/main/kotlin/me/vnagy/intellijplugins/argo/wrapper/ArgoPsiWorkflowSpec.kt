package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLPsiElement
import org.jetbrains.yaml.psi.YAMLSequence
import org.jetbrains.yaml.psi.YAMLSequenceItem

class ArgoPsiWorkflowSpec(
    override val psiElement: YAMLPsiElement,
    override val parentElement: ArgoPsiFileWrapper
) : ArgoPsiSpec {

    override val children: Sequence<ArgoPsi<*>>
        get() = templates.asSequence()

    override val templates: List<ArgoPsiTemplateSpec>
        get() {
            val keyValuePairs = psiElement
                .children
                .asSequence()
                .map { it as? YAMLKeyValue }
                .filterNotNull()
            val templatesKeyValue = keyValuePairs
                .filter { "templates" == it.keyText }
                .uniqueOrNull()
            return templatesKeyValue
                ?.children
                ?.firstNotNullOf { it as? YAMLSequence }
                ?.children
                ?.mapNotNull { it as? YAMLSequenceItem }
                ?.map { ArgoPsiTemplateSpec(it, this) }
                ?.toList() ?: listOf()
        }
}
