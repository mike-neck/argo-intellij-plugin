package org.mikeneck.intellij.plugins.argo.workflows.gtdu

import com.intellij.model.Symbol
import com.intellij.model.psi.PsiSymbolDeclaration
import com.intellij.model.psi.PsiSymbolReference
import com.intellij.model.psi.PsiSymbolService
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.util.endOffset
import com.intellij.psi.util.startOffset
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.Unmodifiable
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLPsiElement
import org.jetbrains.yaml.psi.YAMLScalar
import org.mikeneck.intellij.plugins.argo.workflows.*

class SingleStepTemplateReferenceContributor: PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        logger<SingleStepTemplateReferenceContributor>().info("new single-step-template-reference-contributor registration: ${registrar.javaClass.simpleName}")
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
                        logger<SingleStepTemplateReferenceContributor>().info(" empty by - ${it.first}; [${list.debugPsiStructureUpToRoot(element)}]")
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
            val template = singleStep.upToTemplate ?: return "template[${msg}]" to EMPTY
            val workflowOrWorkflowTemplate = template.upToWorkflowOrWorkflowTemplate ?: return "workflowOrWorkflowTemplate(<- ${template.javaClass.simpleName}@${System.identityHashCode(template)})[${msg}]" to EMPTY

            val currentFile = localTemplateName.containingFile as? YAMLFile ?: return "currentFile[${msg}]" to EMPTY
            val templateCollection = currentFile.documents.ofWorkflowOrWorkflowTemplate().mapNotNull { it.resource }
            if (templateCollection.isEmpty()) return "templateCollection[${msg}]" to EMPTY
            return templateCollection
                .map {
                    logger<LocalTemplateCandidatePsiReference>().info("new LocalTemplateCandidatePsiReference ${element.javaClass.simpleName} -> ${localTemplateName.valueText}(${localTemplateName.value?.javaClass?.simpleName ?: "<null>"})")
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
        logger<LocalTemplateCandidatePsiReference>().info("resolve ${localTemplateName.valueText} -> ${template?.javaClass?.simpleName}[${template.getValue<YAMLScalar>("name")?.textValue}]")
        return template.templateNameElement
    }
}

@Suppress("UnstableApiUsage", "JavaDefaultMethodsNotOverriddenByDelegation")
data class LocalTemplatePsiReference(
    val templateName: String,
    val element: ArgoWorkflowStepLocalTemplateNameElement,
    val template: ArgoWorkflowTemplateElement
): PsiReferenceBase<ArgoWorkflowTemplateElement>(template), NavigatablePsiElement by template {
    override fun getTextRangeInParent(): TextRange {
        return super.getTextRangeInParent()
    }

    override fun getOwnDeclarations(): @Unmodifiable Collection<PsiSymbolDeclaration> {
        return when(val nameElement = template.templateNameElement) {
            null -> emptyList()
            is YAMLScalar -> listOf(object : PsiSymbolDeclaration {
                override fun getDeclaringElement(): PsiElement = nameElement

                override fun getRangeInDeclaringElement(): TextRange {
                    return nameElement.textRange ?: TextRange.from(nameElement.startOffset, nameElement.textLength)
                }

                override fun getSymbol(): Symbol {
                    val psiSymbolService = PsiSymbolService.getInstance()
                    return psiSymbolService.asSymbol(nameElement)
                }
            })
        }
    }

    override fun getOwnReferences(): @Unmodifiable Collection<PsiSymbolReference> {
        return template.ownReferences
    }

    override fun navigate(requestFocus: Boolean) {
        when(val nameElement = template.templateNameElement) {
            null -> template.navigate(requestFocus)
            else -> nameElement.navigate(requestFocus)
        }
    }

    override fun canNavigate(): Boolean {
        return template.templateNameElement?.canNavigate() ?: false
    }

    override fun canNavigateToSource(): Boolean {
        return template.isValid && element.isValid && template.templateName != null
    }

    override fun resolve(): PsiElement = template
}
