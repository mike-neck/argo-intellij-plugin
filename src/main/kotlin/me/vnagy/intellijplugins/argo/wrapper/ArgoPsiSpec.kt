package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLPsiElement

class ArgoPsiSpec(
    override val psiElement: YAMLPsiElement,
    override val parentElement: ArgoPsiFileWrapper
) : ArgoPsi<YAMLPsiElement> {

    override val children: Sequence<ArgoPsi<*>>
        get() = templates.asSequence()

    val templates: List<ArgoPsiTemplateSpec>
        get() {
            return psiElement
                .children
                .asSequence()
                .map { it as? YAMLKeyValue }
                .filterNotNull()
                .filter { it.keyText == "templates" }
                .uniqueOrNull()
                ?.children[0]
                ?.children
                ?.map { it as YAMLPsiElement }
                ?.map { ArgoPsiTemplateSpec(it, this) }
                ?.toList() ?: listOf()
        }

    fun getTemplateByName(templateName: String): ArgoPsiTemplateSpec? {
        return templates.filter { it.name == templateName }.uniqueOrNull()
    }
}
