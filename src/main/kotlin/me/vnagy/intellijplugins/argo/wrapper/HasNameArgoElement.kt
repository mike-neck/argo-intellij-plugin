package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLScalar
import org.jetbrains.yaml.psi.YAMLValue

interface HasNameArgoElement {

    val name: String?
        get() = (yamlChildren["name"]?.value as YAMLScalar?)?.textValue

    val namePsiElement: PsiElement?
        get() = (yamlChildren["name"]?.value as YAMLScalar?)

    val yamlChildren: YAMLValue
}
