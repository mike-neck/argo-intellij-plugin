package me.vnagy.intellijplugins.argo.completioncontributor.dependencies

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType

class DagDependenciesAutoCompletionContributor : CompletionContributor() {

    init {
        extend(
            CompletionType.BASIC,
            DagDependencyElementPattern(),
            DagDependencyAutoCompletionProvider()
        )
    }
}
