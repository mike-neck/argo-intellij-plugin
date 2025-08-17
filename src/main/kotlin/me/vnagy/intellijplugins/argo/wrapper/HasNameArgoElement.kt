package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLScalar
import org.jetbrains.yaml.psi.YAMLValue

interface HasNameArgoElement {

    val name: String?
        get() = namePsiElement?.textValue

    val namePsiElement: YAMLScalar?
        get() = (yamlChildren["name"]?.value as? YAMLScalar)

    val yamlChildren: YAMLValue?
}
