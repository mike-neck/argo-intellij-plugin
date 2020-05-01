package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping

class ArgoParametersPsi(
    override val psiElement: YAMLKeyValue,
    override val parentElement: ArgoPsi<*>
) : ArgoPsi<YAMLKeyValue> {
    // TODO

    val parameters: Sequence<ArgoParameterPsi>
        get() {
            return psiElement
                .value
                ?.children
                ?.asSequence()
                ?.flatMap { it.children.asSequence() }
                ?.asSequence()
                ?.map { it as? YAMLMapping }
                ?.filterNotNull()
                ?.map { ArgoParameterPsi(it, this) } ?: emptySequence()
        }

    override val children: Sequence<ArgoPsi<*>>
        get() = sequenceOf(
            parameters,
            sequenceOf(
                keyPsiElement?.let { GenericArgoPsiWrapper(it, this) }
            ).filterNotNull()
        ).flatten()

    val keyPsiElement: PsiElement?
        get() = psiElement.key

    fun getParameterByName(parameterName: String): ArgoParameterPsi? {
        return parameters.find { it.name == parameterName }
    }


}
