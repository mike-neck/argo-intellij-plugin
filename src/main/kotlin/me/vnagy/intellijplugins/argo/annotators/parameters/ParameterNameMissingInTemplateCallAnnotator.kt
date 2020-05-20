package me.vnagy.intellijplugins.argo.annotators.parameters

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import me.vnagy.intellijplugins.argo.wrapper.*
import org.jetbrains.yaml.psi.YAMLFile

class ParameterNameMissingInTemplateCallAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val containingFile = element.containingFile
        if (containingFile is YAMLFile) {
            val argoPsiFileWrapper = ArgoPsiFileWrapper(containingFile)
            when (val argoElement = argoPsiFileWrapper.findChildrenForPsiElement(element)?.parentElement) {
                is ArgoParametersPsi -> annotateParametersElement(argoElement, element, holder)
                is HasArgumentArgoElement -> annotateTemplateElementWithMissingParameter(argoElement, holder)
            }
        }
    }

    private fun annotateTemplateElementWithMissingParameter(
        stepSpecification: HasArgumentArgoElement<*>,
        holder: AnnotationHolder
    ) {
        val parametersElement = stepSpecification
            .arguments
            ?.parameters
            ?.keyPsiElement
        if (parametersElement == null) {
            createAnnotation(stepSpecification, holder)
        }
    }

    private fun annotateParametersElement(parametersElement: ArgoParametersPsi, element: PsiElement, holder: AnnotationHolder) {
        if (element != parametersElement.keyPsiElement) {
            return
        }
        createAnnotation(parametersElement, holder)
    }

    private fun createAnnotation(
        parametersElement: ArgoPsi<*>,
        holder: AnnotationHolder
    ) {
        val missingParameterNames = getMissingParameterNames(parametersElement)
        if (missingParameterNames.isNotEmpty()) {
            val annotation = holder.newAnnotation(
                HighlightSeverity.ERROR,
                "The parameter(s) ${missingParameterNames.joinToString(
                    ",",
                    "[",
                    "]"
                )} are missing from the template."
            )

            annotation.create()
    //                annotation.registerFix(ParameterNameMissingInTemplateCallQuickFix(parametersElement, missingParameterNames))
        }
    }

    private fun getMissingParameterNames(argoElement: ArgoPsi<*>): List<String> {
        val templateCallDefinition = argoElement.findParentOfType(HasTemplateArgoElement::class)
        val templateName = templateCallDefinition?.template
        if (templateName != null) {

            val templateDefinition = argoElement
                .findParentOfType(ArgoPsiWorkflowSpec::class)
                ?.getTemplateByName(templateName)

            val parametersInTemplateWithoutValue = templateDefinition
                ?.inputs
                ?.parameters
                ?.parameters
                ?.filter { it.value == null } ?: emptySequence()

            val parameterNamesAtCallSize = templateCallDefinition
                .arguments
                ?.parameters
                ?.parameters
                ?.map { it.name }
                ?.toList() ?: emptyList()

            return parametersInTemplateWithoutValue
                .map { it.name }
                .filterNotNull()
                .filterNot { parameterNamesAtCallSize.contains(it) }
                .toList()
        }
        return emptyList()
    }

}
