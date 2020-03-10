package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLValue
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl

interface HasNameArgoElement {

    val name: String
        get() = (yamlChildren["name"] as YAMLPlainTextImpl).textValue

    val namePsiElement: PsiElement?
        get() = (yamlChildren["name"] as YAMLPlainTextImpl)

    val yamlChildren: YAMLValue
}
