package me.vnagy.intellijplugins.argo.annotators.parameters

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import me.vnagy.intellijplugins.argo.wrapper.ArgoParametersPsi
import org.jetbrains.yaml.YAMLUtil

class ParameterNameMissingInTemplateCallQuickFix(
    private val parametersElement: ArgoParametersPsi,
    private val missingParameterNames: List<String>
) : IntentionAction {

    override fun startInWriteAction() = true
    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?) = true
    override fun getText() = "Add the missing parameters"

    override fun getFamilyName() = "Argo Workflow"

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        parametersElement
            .psiElement
            .add()
    }

}
