package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl
import org.jetbrains.yaml.psi.impl.YAMLScalarImpl

interface HasTemplateArgoElement<T : PsiElement> : ArgoPsi<T>, HasArgumentArgoElement<T> {

    private val childTemplate: YAMLKeyValue? get() = yamlChildren["template"]

    val template: String?
        get() = childTemplate.value<YAMLPlainTextImpl>()?.textValue

    val templatePsiElement: YAMLScalarImpl?
        get() = childTemplate.value<YAMLScalarImpl>()
}
