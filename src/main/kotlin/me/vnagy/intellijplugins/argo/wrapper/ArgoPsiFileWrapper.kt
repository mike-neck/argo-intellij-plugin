package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLFile
import java.lang.Exception

class ArgoPsiFileWrapper(override val psiElement: YAMLFile) : ArgoPsi<YAMLFile> {

    val kind: String?
        get() = getTopLevelPropertyString("kind")

    val apiVersion: String?
        get() = getTopLevelPropertyString("apiVersion")

    val spec: ArgoPsiSpec?
        get() {
            val specElement = psiElement
                .documents[0]
                .topLevelValue["spec"]

            return specElement?.let { ArgoPsiSpec(specElement, this) }
        }

    private fun getTopLevelPropertyString(key: String): String? {
        return psiElement
            .documents[0]
            .topLevelValue[key]
            ?.text
    }
}
