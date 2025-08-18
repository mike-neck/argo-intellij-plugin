package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.psi.PsiElement
import com.intellij.usages.UsageToPsiElementProvider
import org.jetbrains.yaml.YAMLLanguage
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalar
import org.jetbrains.yaml.psi.YAMLValue

class TemplateUsagesToPsiElementProvider : UsageToPsiElementProvider() {

    override fun getAppropriateParentFrom(element: PsiElement?): PsiElement? {
        if (element == null || !(element.isValid) || element.language != YAMLLanguage.INSTANCE) {
            return null
        }
        return Util.tryFindKeyScalarValue(element)
    }

    object Util {
        tailrec fun tryFindKeyScalarValue(element: PsiElement): YAMLKeyValue? {
            return when (element) {
                is YAMLKeyValue -> when (element.value) {
                    is YAMLScalar -> element
                    else -> null
                }

                is YAMLValue -> tryFindKeyScalarValue(element.parent)
                else -> null
            }
        }
    }
}
