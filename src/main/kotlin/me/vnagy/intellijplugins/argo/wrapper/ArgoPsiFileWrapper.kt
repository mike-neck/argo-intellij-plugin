package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLFile

class ArgoPsiFileWrapper(override val psiElement: YAMLFile) : ArgoPsi<YAMLFile> {

    constructor(psiElement: PsiElement?): this(psiElement?.containingFile as YAMLFile)

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

    private fun getTopLevelPropertyString(key: String): String? {
        return psiElement
            .documents[0]
            .topLevelValue[key]
            ?.value
            ?.text
    }
}
