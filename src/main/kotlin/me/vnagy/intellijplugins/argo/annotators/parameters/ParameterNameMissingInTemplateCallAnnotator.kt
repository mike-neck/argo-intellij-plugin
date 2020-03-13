package me.vnagy.intellijplugins.argo.annotators.parameters

import com.intellij.lang.annotation.Annotation
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import me.vnagy.intellijplugins.argo.wrapper.*
import org.jetbrains.yaml.psi.YAMLFile

class ParameterNameMissingInTemplateCallAnnotator: Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val containingFile = element.containingFile
        if (containingFile is YAMLFile) {
            val argoPsiFileWrapper = ArgoPsiFileWrapper(containingFile)
            when (val argoElement =  argoPsiFileWrapper.findChildrenForPsiElement(element)) {
                is ArgoParametersPsi -> annotateParametersElement(argoElement, holder)
            }
        }
    }

    private fun annotateParametersElement(parametersElement: ArgoParametersPsi, holder: AnnotationHolder) {
        val templateCallDefinition = parametersElement.findParentOfType(ArgoPsiStepSpecification::class)
        val templateName = templateCallDefinition?.template
        if (templateName != null) {

            val templateDefinition = parametersElement
                .findParentOfType(ArgoPsiSpec::class)
                ?.getTemplateByName(templateName)

            val parametersInTemplate = templateDefinition
                ?.inputs
                ?.parameters
                ?.parameters ?: emptySequence()

            val parametersAtCallSite = templateCallDefinition
                .arguments
                ?.parameters
                ?.parameters ?: emptySequence()

            val parameterNamesAtCallSize = parametersAtCallSite
                .map { it.name }
                .toList()

            val missingParameterNames = parametersInTemplate
                .map { it.name }
                .filterNotNull()
                .filterNot { parameterNamesAtCallSize.contains(it) }
                .toList()

            if (missingParameterNames.isNotEmpty()) {
                val annotation = holder.createAnnotation(
                    HighlightSeverity.ERROR,
                    parametersElement.psiElement.textRange,
                    "The parameter(s) ${missingParameterNames.joinToString(",", "[", "]")} are missing from the template."
                )
//                annotation.registerFix(ParameterNameMissingInTemplateCallQuickFix(parametersElement, missingParameterNames))
            }
        }
    }

}
