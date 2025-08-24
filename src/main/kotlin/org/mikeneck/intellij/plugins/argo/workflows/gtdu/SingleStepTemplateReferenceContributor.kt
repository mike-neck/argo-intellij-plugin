package org.mikeneck.intellij.plugins.argo.workflows.gtdu

import com.intellij.patterns.StandardPatterns.or
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalar
import org.mikeneck.intellij.plugins.argo.workflows.asArgoWorkflowStepTemplateElement
import org.mikeneck.intellij.plugins.argo.workflows.collection
import org.mikeneck.intellij.plugins.argo.workflows.ofWorkflowTemplate
import org.mikeneck.intellij.plugins.argo.workflows.upToStep
import org.mikeneck.intellij.plugins.argo.workflows.upToTemplate

class SingleStepTemplateReferenceContributor: PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            or(psiElement<YAMLScalar>(), psiElement<YAMLKeyValue>()),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<out PsiReference?> = SingleStepTemplateReferenceProvider.findReferences(element, context)
            }
        )
    }

    object SingleStepTemplateReferenceProvider {
        fun findReferences(
            element: PsiElement,
            context: ProcessingContext
        ): Array<PsiReference> {
            val stepTemplate = element.asArgoWorkflowStepTemplateElement ?: return emptyArray()
            val currentFile = stepTemplate.containingFile as? YAMLFile ?: return emptyArray()
            currentFile.documents.ofWorkflowTemplate()
                .mapNotNull { workflowTemplate -> workflowTemplate.templates }
            val singleStep = stepTemplate.upToStep
            val template = singleStep.upToTemplate
            val templateCollection = template.collection ?: return emptyArray()

            TODO("Not yet implemented")
        }
    }
}
