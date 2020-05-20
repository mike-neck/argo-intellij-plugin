package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLPsiElement

class ArgoPsiCronWorkflowSpec(
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
            val workflowSpecChildren = keyValuePairs
                .filter { "workflowSpec" == it.keyText }
                .uniqueOrNull()
                ?.children[0]
                ?.children
                ?.asSequence()
                ?.map { it as? YAMLKeyValue }
                ?.filterNotNull()
            val templatesKeyValue = workflowSpecChildren
                ?.filter { "templates" == it.keyText }
                ?.uniqueOrNull()
            return templatesKeyValue
                ?.children[0]
                ?.children
                ?.map { it as YAMLPsiElement }
                ?.map { ArgoPsiTemplateSpec(it, this) }
                ?.toList() ?: listOf()
        }
}
