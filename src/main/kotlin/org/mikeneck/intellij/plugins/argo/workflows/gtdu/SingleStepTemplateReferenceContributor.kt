package org.mikeneck.intellij.plugins.argo.workflows.gtdu

import com.intellij.kubernetes.get
import com.intellij.openapi.diagnostic.logger
import com.intellij.psi.*
import com.intellij.psi.util.endOffset
import com.intellij.psi.util.startOffset
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLPsiElement
import org.jetbrains.yaml.psi.YAMLScalar
import org.mikeneck.intellij.plugins.argo.workflows.*

class SingleStepTemplateReferenceContributor: PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            psiElement<YAMLScalar>(),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<out PsiReference?> = (when (element) {
                    is YAMLScalar -> SingleStepTemplateReferenceProvider.findReferences(element, context)
                    else -> "different-type: ${element.javaClass.simpleName}" to emptyArray()
                }).let {
                    if (it.first != null) {
                        val list = mutableListOf<String>()
                        logger<SingleStepTemplateReferenceContributor>().debug("Unbound element by - ${it.first}; [${list.debugPsiStructureUpToRoot(element)}]")
                    }
                    return@let it.second
                }
            }
        )
    }

    tailrec fun MutableList<String>.debugPsiStructureUpToRoot(e: PsiElement?): String {
        if (e == null || e is PsiDirectory) return this.joinToString("/")
        else {
            this.add(when (e) {
                is YAMLKeyValue -> "KeyValue=${e.keyText}"
                is PsiFile -> "File=${e.name}"
                else -> e.javaClass.simpleName + '@' + System.identityHashCode(e)
            })
            return this.debugPsiStructureUpToRoot(e.parent)
        }
    }

    object SingleStepTemplateReferenceProvider {
        val EMPTY: Array<PsiReference> = emptyArray()

        fun findReferences(
            element: YAMLScalar,
            context: ProcessingContext
        ): Pair<String?, Array<PsiReference>> {
            val msg = "reference provider: ${element.javaClass.simpleName}(${element.containingFile.virtualFile?.name}:${element.startOffset}-${element.endOffset})"
            val localTemplateName = element.asArgoWorkflowStepLocalTemplateNameElement ?: return "localTemplateName[${msg}]" to EMPTY
            val singleStep = localTemplateName.upToStep ?: return "singleStep[${msg}]" to EMPTY
            val currentTemplate = singleStep.upToTemplate ?: return "template[${msg}]" to EMPTY
            val workflowOrWorkflowTemplate = currentTemplate.upToWorkflowOrWorkflowTemplate ?: return "workflowOrWorkflowTemplate(<- ${currentTemplate.javaClass.simpleName}@${System.identityHashCode(currentTemplate)})[${msg}]" to EMPTY

            val currentFile = localTemplateName.containingFile as? YAMLFile ?: return "currentFile[${msg}]" to EMPTY
            val templateCollection = currentFile.documents.ofWorkflowOrWorkflowTemplate().mapNotNull { it.resource }
            if (templateCollection.isEmpty()) return "templateCollection[${msg}]" to EMPTY
            return templateCollection
                .map {
                    logger<LocalTemplateCandidatePsiReference>().info("new LocalTemplateCandidatePsiReference ${localTemplateName.valueText}:${element.textValue.lines().firstOrNull() ?: "<empty>"} ->  ${it["name"] ?: "<null>"}")
                    LocalTemplateCandidatePsiReference(localTemplateName, workflowOrWorkflowTemplate, it)
                }
                .toTypedArray<PsiReference>()
                .let { (null as String?) to it }
        }
    }
}

data class LocalTemplateCandidatePsiReference(
    private val localTemplateName: ArgoWorkflowStepLocalTemplateNameElement,
    private val workflowOrWorkflowTemplate: ArgoWorkflowElement,
    private val workflowOrWorkflowTemplateFromRoot: ArgoWorkflowElement,
): PsiReferenceBase<YAMLPsiElement>(localTemplateName.value ?: localTemplateName) {
    override fun resolve(): PsiElement? {
        if (workflowOrWorkflowTemplate.workflowName != workflowOrWorkflowTemplateFromRoot.workflowName) return null
        val template = workflowOrWorkflowTemplateFromRoot
            .spec
            .templates
            .find { when(val name = it.templateName) {
                    null -> false
                    else -> name == localTemplateName.valueText
                }
            }
        logger<LocalTemplateCandidatePsiReference>().info("resolve ${localTemplateName.valueText} -> ${template?.javaClass?.simpleName}[${template.getValue<YAMLScalar>("name")?.textValue ?: "<null>"}]")
        return template.templateNameElement ?: template
    }
}
