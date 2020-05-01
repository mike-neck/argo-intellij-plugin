package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLValue
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl
import org.jetbrains.yaml.psi.impl.YAMLScalarImpl

interface HasTemplateArgoElement<T : PsiElement> : ArgoPsi<T>, HasArgumentArgoElement<T> {

    val template: String?
        get() = (yamlChildren["template"]?.value as YAMLPlainTextImpl?)?.textValue

    val templatePsiElement: YAMLScalarImpl?
        get() = (yamlChildren["template"]?.value as YAMLScalarImpl?)

}
