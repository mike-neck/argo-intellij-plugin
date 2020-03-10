package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLValue
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl

interface HasTemplateArgoElement {

    val template: String?
        get() = (yamlChildren["template"] as? YAMLPlainTextImpl)?.textValue

    val templatePsiElement: PsiElement?
        get() = (yamlChildren["template"] as? YAMLPlainTextImpl)

    val yamlChildren: YAMLValue

}
