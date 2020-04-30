package me.vnagy.intellijplugins.argo.completioncontributor.parameters;

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.AutoCompletionPolicy
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.Key
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.ElementPatternCondition
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import me.vnagy.intellijplugins.argo.references.ArgoCallsitePsiReferenceBase
import me.vnagy.intellijplugins.argo.wrapper.*
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.impl.YAMLBlockMappingImpl

private val PARAMETERS_ELEMENT: Key<ArgoParameterPsi> = Key("PARAMETERS_ELEMENT")

class ParameterNameAutoCompletionContributor : CompletionContributor() {

    init {
        extend(
            CompletionType.BASIC,
            ParameterNameElementPattern(),
            ParameterNameAutoCompletionProvider()
        )
    }

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        super.fillCompletionVariants(parameters, result)
    }
}

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
                parameterNames.forEach { result.addElement(
                    LookupElementBuilder
                        .create(it)
                        .withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE)
                ) }
            }
        }
    }

}

class ParameterNameElementPattern : ElementPattern<PsiElement> {

    override fun accepts(o: Any?, context: ProcessingContext): Boolean {
        if (o is PsiElement) {
            val containingFile = o.containingFile
            if (containingFile is YAMLFile) {
                val argoPsiWrapper = ArgoPsiFileWrapper(containingFile)
                val parameterParent = argoPsiWrapper.findChildrenForPsiElement(o.parentOfType<YAMLBlockMappingImpl>())
                if (parameterParent is ArgoParameterPsi && parameterParent.namePsiElement == o.parent) {
                    context.put(PARAMETERS_ELEMENT, parameterParent)
                    return true
                }
            }
        }
        return false
    }

    override fun accepts(o: Any?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getCondition(): ElementPatternCondition<PsiElement> {
        TODO("Not yet implemented")
    }
}
