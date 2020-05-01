package me.vnagy.intellijplugins.argo.completioncontributor.parameters;

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType

class ParameterNameAutoCompletionContributor : CompletionContributor() {

    init {
        extend(
            CompletionType.BASIC,
            ParameterNameElementPattern(),
            ParameterNameAutoCompletionProvider()
        )
    }
}

