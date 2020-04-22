package me.vnagy.intellijplugins.argo.annotators.parameters

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import me.vnagy.intellijplugins.argo.wrapper.ArgoParametersPsi
import me.vnagy.intellijplugins.argo.wrapper.get
import org.intellij.lang.annotations.Language
import org.jetbrains.yaml.YAMLElementGenerator
import org.jetbrains.yaml.psi.impl.YAMLArrayImpl

class ParameterNameMissingInTemplateCallQuickFix(
    private val parametersElement: ArgoParametersPsi,
    private val missingParameterNames: List<String>
) : IntentionAction {

    override fun startInWriteAction() = true
    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?) = true
    override fun getText() = "Add the missing parameters"

    override fun getFamilyName() = "Argo Workflow"

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        val existingParametersPsi = parametersElement
            .parameters
            .map { it.psiElement }
            .toList()

        val arrayValue = parametersElement
            .psiElement
            .value as? YAMLArrayImpl

        if (arrayValue != null) {
            arrayValue.deleteChildRange(arrayValue.firstChild, arrayValue.lastChild)
            existingParametersPsi.forEach { arrayValue.add(it) }
        }

        val elementGenerator = YAMLElementGenerator.getInstance(project)
        missingParameterNames.forEach {
            @Language("yaml")
            val psiFile = elementGenerator.createDummyYamlWithText(
                """
                parameters:
                 - name: "${it}"
            """.trimIndent()
            )

            parametersElement
                .psiElement
                .value
                ?.add(psiFile.documents[0].topLevelValue["parameters"]?.value?.children?.get(0)!!)
        }
    }

}
