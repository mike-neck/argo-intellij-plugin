package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLValue
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl

interface HasTemplateArgoElement {

    val template: String?
        get() = (yamlChildren["template"]?.value as YAMLPlainTextImpl?)?.textValue

    val templatePsiElement: YAMLPlainTextImpl?
        get() = (yamlChildren["template"]?.value as YAMLPlainTextImpl?)

    val yamlChildren: YAMLValue

}
