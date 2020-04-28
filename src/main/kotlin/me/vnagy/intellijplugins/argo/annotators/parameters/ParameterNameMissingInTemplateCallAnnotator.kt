package me.vnagy.intellijplugins.argo.annotators.parameters

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import me.vnagy.intellijplugins.argo.wrapper.ArgoParametersPsi
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiFileWrapper
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiSpec
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiStepSpecification
import org.jetbrains.yaml.psi.YAMLFile

class ParameterNameMissingInTemplateCallAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val containingFile = element.containingFile
        if (containingFile is YAMLFile) {
            val argoPsiFileWrapper = ArgoPsiFileWrapper(containingFile)
            when (val argoElement = argoPsiFileWrapper.findChildrenForPsiElement(element)) {
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

            val missingParameterNames = parametersInTemplateWithoutValue
                .map { it.name }
                .filterNotNull()
                .filterNot { parameterNamesAtCallSize.contains(it) }
                .toList()

            if (missingParameterNames.isNotEmpty()) {
                val annotation = holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    "The parameter(s) ${missingParameterNames.joinToString(
                        ",",
                        "[",
                        "]"
                    )} are missing from the template."
                )
//                annotation.registerFix(ParameterNameMissingInTemplateCallQuickFix(parametersElement, missingParameterNames))
            }
        }
    }

}
