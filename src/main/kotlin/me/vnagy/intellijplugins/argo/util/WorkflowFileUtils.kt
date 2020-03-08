package me.vnagy.intellijplugins.argo.util

import com.intellij.psi.PsiFile
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl

fun isWorkflowFile(psiFile: PsiFile?): Boolean {
    if (psiFile is YAMLFile) {
        val topLevelValue = psiFile.documents.firstOrNull()?.topLevelValue
        if (topLevelValue != null) {
            val apiVersionMatches = topLevelValue
                .children
                .filterIsInstance(YAMLKeyValueImpl::class.java)
                .any { it.keyText == "apiVersion" && it.valueText == "argoproj.io/v1alpha1" }
            val isWorkflowKind = topLevelValue
                .children
                .filterIsInstance(YAMLKeyValueImpl::class.java)
                .any { it.keyText == "kind" && it.valueText == "Workflow" }
            return apiVersionMatches && isWorkflowKind
        }
    }
    return false
}
