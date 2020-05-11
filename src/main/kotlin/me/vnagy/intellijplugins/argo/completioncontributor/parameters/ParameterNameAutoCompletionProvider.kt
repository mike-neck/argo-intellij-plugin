package me.vnagy.intellijplugins.argo.completioncontributor.parameters

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.AutoCompletionPolicy
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import me.vnagy.intellijplugins.argo.completioncontributor.PARAMETERS_ELEMENT
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiSpec
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiStepSpecification
import me.vnagy.intellijplugins.argo.wrapper.HasTemplateArgoElement

class ParameterNameAutoCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val parameterPsi = context.get(PARAMETERS_ELEMENT)
        val hasArgumentElement = parameterPsi.parentElement.parentElement.parentElement
        if (hasArgumentElement is ArgoPsiStepSpecification) {
            val templateName = hasArgumentElement
                .findParentOfType(HasTemplateArgoElement::class)
                ?.template

            if (templateName != null) {
                val parameterElements = hasArgumentElement
                    .findParentOfType(ArgoPsiSpec::class)
                    ?.getTemplateByName(templateName)
                    ?.inputs
                    ?.parameters
                    ?.parameters ?: emptySequence()

                val parameterNames = parameterElements.map { it.name }.filterNotNull()
                parameterNames.forEach {
                    result.addElement(
                        LookupElementBuilder.create(it)
                            .withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE)
                    )
                }
            }
        }
    }
}
