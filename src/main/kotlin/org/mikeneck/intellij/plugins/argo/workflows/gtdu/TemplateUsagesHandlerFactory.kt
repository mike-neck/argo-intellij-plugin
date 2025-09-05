package org.mikeneck.intellij.plugins.argo.workflows.gtdu

import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesHandlerFactory
import com.intellij.lang.findUsages.LanguageFindUsages
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.diagnostic.logger
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReference
import com.intellij.psi.search.SearchScope
import org.jetbrains.annotations.Unmodifiable
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalar
import org.jetbrains.yaml.psi.YAMLValue
import org.mikeneck.intellij.plugins.argo.workflows.*
import org.mikeneck.intellij.plugins.argo.workflows.gtdu.TemplateUsagesHandlerFactory.Cmp.log

class TemplateUsagesHandlerFactory : FindUsagesHandlerFactory() {

    object Cmp {
        private val logger: Logger = logger<TemplateUsagesHandlerFactory>()
        fun log(message: String) = logger.info(message)
    }

    override fun canFindUsages(element: PsiElement): Boolean {
        return when (element) {
            is YAMLScalar -> {
                @Suppress("USELESS_CAST")
                val keyValue = element.asYAMLKeyValue() as ArgoWorkflowTemplateNameElement?
                val result = keyValue.fromTemplateNameToTemplates()
                log("can-find-usages(of: ${keyValue?.keyText ?: "unknown-key"}=${element.textValue.lines().firstOrNull()?:"<empty>"}): ${result != null}")
                result != null
            }
            else -> false
        }
    }

    override fun createFindUsagesHandler(
        element: PsiElement,
        forHighlightUsages: Boolean
    ): FindUsagesHandler? {
        if (element !is YAMLScalar) return null
        val name = element.asYAMLKeyValue() ?: return null
        val template = name.template ?: return null
        val templates = name.templateCollection.toList()
        if (templates.isEmpty()) {
            return null
        }
        log("create-find-usage-handler: ${element.javaClass.simpleName}(${name.valueText}) -> ${templates.size}")
        return FindTemplateUsagesHandler(element, template, templates, forHighlightUsages)
    }
}

class FindTemplateUsagesHandler(
    element: PsiElement,
    private val template: ArgoWorkflowTemplateElement,
    private val templateCollection: ArgoWorkflowTemplateElementCollection,
    private val forHighlightUsages: Boolean
) : FindUsagesHandler(element) {
    override fun getHelpId(): String? {
        return LanguageFindUsages.getHelpId(psiElement)
    }

    override fun getPrimaryElements(): Array<out PsiElement?> {
        log("get-primary-elements: ${template.javaClass.simpleName}(${template.name})")
        return templateCollection
            .filter { template ->
                when (val templateName = template.templateName) {
                    null -> false
                    else -> templateName != (template.name ?: return@filter false)
                }
            }
            .flatMap { template -> template.steps.all }
            .filter { step ->
                when (val templateName = step.templateName) {
                    null -> false
                    else -> templateName == template.name
                }
            }
            .peekLog("get-primary-elements")
            .mapNotNull { step -> step.stepNameElement }
            .toTypedArray()
    }

    fun <T: YAMLValue> Iterable<T>.peekLog(header: String): Iterable<T> {
        this.forEach { item:T ->
            log("$header: ${item.javaClass.simpleName}(${item.text})")
        }
        return this
    }

    override fun findReferencesToHighlight(
        target: PsiElement,
        searchScope: SearchScope
    ): @Unmodifiable Collection<PsiReference?> {
        log("find-references-to-highlight: ${target.text}(${target.javaClass.simpleName}) ${target.mapUpToFile { "${it.javaClass.simpleName}@${System.identityHashCode(it)}${if (it is YAMLKeyValue) "(${it.keyText})" else "" }" }}")
        return super.findReferencesToHighlight(target, searchScope)
    }

    tailrec fun <T: PsiElement, R: Any> T.mapUpToFile(list: MutableList<R> = mutableListOf(), mapper: (T) -> R): List<R> {
        if (this is PsiFile) {
            list.add(mapper(this))
            return list.toList()
        } else if (this is PsiDirectory) {
            return list.toList()
        }
        list.add(mapper(this))
        return when (this.parent) {
            null -> list.toList()
            else -> this.mapUpToFile(list, mapper)
        }
    }
}
