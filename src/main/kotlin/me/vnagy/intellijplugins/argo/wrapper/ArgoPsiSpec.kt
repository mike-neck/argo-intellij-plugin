package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLPsiElement

interface ArgoPsiSpec: ArgoPsi<YAMLPsiElement> {

    val templates: List<ArgoPsiTemplateSpec>

    fun getTemplateByName(templateName: String): ArgoPsiTemplateSpec? {
        return templates.filter { it.name == templateName }.uniqueOrNull()
    }
}
