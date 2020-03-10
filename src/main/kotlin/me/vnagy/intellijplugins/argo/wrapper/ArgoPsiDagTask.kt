package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLSequenceItem

class ArgoPsiDagTask(
    override val psiElement: YAMLMapping,
    override val parentElement: ArgoPsiTemplateSpec
) : ArgoPsi<YAMLMapping> {

    override val children: Sequence<ArgoPsi<*>>
        get() = tasks.asSequence()

    val tasks: Sequence<ArgoPsiDagTaskSpecification>
        get() = psiElement
            .children
            .filterIsInstance<YAMLKeyValue>()
            .filter { it.keyText == "tasks" }
            .uniqueOrNull()
            ?.value
            ?.children
            ?.filterIsInstance<YAMLSequenceItem>()
            ?.map { ArgoPsiDagTaskSpecification(it, this) }
            ?.asSequence() ?: emptySequence()

    fun getTaskByName(name: String): ArgoPsiDagTaskSpecification? {
        return tasks.filter { it.name == name }.uniqueOrNull()
    }
}
