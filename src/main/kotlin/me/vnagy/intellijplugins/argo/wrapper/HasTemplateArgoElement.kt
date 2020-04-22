package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLValue
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl
import org.jetbrains.yaml.psi.impl.YAMLScalarImpl

interface HasTemplateArgoElement {

    val template: String?
        get() = (yamlChildren["template"]?.value as YAMLPlainTextImpl?)?.textValue

    val templatePsiElement: YAMLScalarImpl?
        get() = (yamlChildren["template"]?.value as YAMLScalarImpl?)

    val yamlChildren: YAMLValue

}
