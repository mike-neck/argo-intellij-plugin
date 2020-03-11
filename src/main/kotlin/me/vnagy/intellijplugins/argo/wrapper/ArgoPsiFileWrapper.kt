package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLFile

class ArgoPsiFileWrapper(override val psiElement: YAMLFile) : ArgoPsi<YAMLFile> {

    val kind: String?
        get() = getTopLevelPropertyString("kind")

    val apiVersion: String?
        get() = getTopLevelPropertyString("apiVersion")

    override val parentElement: ArgoPsi<*>?
        get() = null

    override val children: Sequence<ArgoPsi<*>>
        get() = sequenceOf(spec).filterNotNull()

    val spec: ArgoPsiSpec?
        get() {
            val specElement = psiElement
                .documents[0]
                .topLevelValue["spec"]
                ?.value

            return specElement?.let { ArgoPsiSpec(specElement, this) }
        }

    private fun getTopLevelPropertyString(key: String): String? {
        return psiElement
            .documents[0]
            .topLevelValue[key]
            ?.value
            ?.text
    }
}
