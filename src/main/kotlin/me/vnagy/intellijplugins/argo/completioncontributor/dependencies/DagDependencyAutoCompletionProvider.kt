package me.vnagy.intellijplugins.argo.completioncontributor.dependencies

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.AutoCompletionPolicy
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import me.vnagy.intellijplugins.argo.completioncontributor.DEPENDENCIES_ELEMENT

class DagDependencyAutoCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val dependenciesElement = context.get(DEPENDENCIES_ELEMENT)
        val dagTask = dependenciesElement.parentElement.parentElement
        return dagTask
            .tasks
            .map { it.name }
            .filterNotNull()
            .filter { it != dependenciesElement.parentElement.name }
            .forEach {
                result.addElement(
                    LookupElementBuilder.create(it)
                        .withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE)
                )
            }
    }
}
