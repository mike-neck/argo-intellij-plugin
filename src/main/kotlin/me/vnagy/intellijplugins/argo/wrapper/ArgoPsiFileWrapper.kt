package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLSequence

class ArgoPsiFileWrapper(override val psiElement: YAMLFile) : ArgoPsi<YAMLFile> {

    constructor(psiElement: PsiElement?): this(psiElement?.containingFile as YAMLFile)

    override fun toString(): String = "ArgoPsiFile[${psiElement.name}]"

    val kind: String?
        get() = getTopLevelPropertyString("kind")

    val apiVersion: String?
        get() = getTopLevelPropertyString("apiVersion")

    override val parentElement: Nothing?
        get() = null

    override val children: Sequence<ArgoPsi<*>>
        get() = sequenceOf(spec).filterNotNull()

    val spec: ArgoPsiSpec?
        get() {
            val specElement = psiElement
                .documents[0]
                .topLevelValue["spec"]
                ?.value
            if (this.kind == "Workflow" || this.kind == "WorkflowTemplate") {
                return specElement?.let { ArgoPsiWorkflowSpec(specElement, this) }
            } else if (this.kind == "CronWorkflow") {
                return specElement?.let { ArgoPsiCronWorkflowSpec(specElement, this) }
            } else {
                return null
            }
        }

    val templates: List<ArgoPsiTemplateSpec> get() {
        val document = psiElement.documents.firstOrNull() ?: return listOf()
        val spec = document.topLevelValue["spec"]?.value as? YAMLMapping ?: return listOf()
        val templates = (spec.getKeyValueByKey("templates")?.value as? YAMLSequence)?.items ?: return listOf()
        return templates.map { ArgoPsiTemplateSpec(it, ArgoPsiWorkflowSpec(spec, this)) }
    }

    private fun getTopLevelPropertyString(key: String): String? {
        return psiElement
            .documents[0]
            .topLevelValue[key]
            ?.value
            ?.text
    }
}
