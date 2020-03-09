package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLPsiElement
import org.jetbrains.yaml.psi.YAMLValue
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl

class ArgoPsiTemplateSpec(
    override val psiElement: YAMLPsiElement,
    val parent: ArgoPsiSpec
) : ArgoPsi<YAMLPsiElement> {

    val namePsiElement: PsiElement?
        get() = ((psiElement.children[0] as YAMLValue)["name"] as YAMLPlainTextImpl)

    val name: String
        get() = ((psiElement.children[0] as YAMLValue)["name"] as YAMLPlainTextImpl).textValue
}
