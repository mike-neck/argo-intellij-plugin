package me.vnagy.intellijplugins.argo.annotators.callsitedependson

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import me.vnagy.intellijplugins.argo.wrapper.*
import org.jetbrains.yaml.psi.YAMLFile

class CallsiteDependsOnTaskMissingFromDagTaskAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val containingFile = element.containingFile
        if (containingFile is YAMLFile) {
            val argoPsiFileWrapper = ArgoPsiFileWrapper(containingFile)
            when (val parentElement = argoPsiFileWrapper.findChildrenForPsiElement(element)?.parentElement) {
                is ArgoPsiDagDependency -> annotateNonExistingDependencyElement(parentElement, element, holder)
            }
        }
    }

    private fun annotateNonExistingDependencyElement(
        dagDependency: ArgoPsiDagDependency,
        dependsOnTextElement: PsiElement,
        holder: AnnotationHolder
    ) {
        if (dependsOnTextElement.text.isNotBlank()) {
            val dagTask = dagDependency.parentElement.parentElement
            val dagTaskWithName = dagTask.getTaskByName(dependsOnTextElement.text)
            if (dagTaskWithName == null) {
                val annotation = holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    "The dag task `${dependsOnTextElement.text}` don't exist in the parent DAG template."
                )

                annotation.create()
            }
        }
    }

}
