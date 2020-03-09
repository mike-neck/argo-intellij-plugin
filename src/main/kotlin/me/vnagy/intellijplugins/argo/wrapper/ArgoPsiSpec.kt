package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLPsiElement

class ArgoPsiSpec(
    override val psiElement: YAMLPsiElement,
    val parent: ArgoPsiFileWrapper
) : ArgoPsi<YAMLPsiElement> { // TODO

    val templates: List<ArgoPsiTemplateSpec>
        get() {
            return psiElement
                .children
                .asSequence()
                .map { it as YAMLKeyValue }
                .filter { it.keyText == "templates" }
                .uniqueOrNull()
                ?.children[0]
                ?.children
                ?.map { it as YAMLPsiElement }
                ?.map { ArgoPsiTemplateSpec(it, this) }
                ?.toList() ?: listOf()
        }
}
