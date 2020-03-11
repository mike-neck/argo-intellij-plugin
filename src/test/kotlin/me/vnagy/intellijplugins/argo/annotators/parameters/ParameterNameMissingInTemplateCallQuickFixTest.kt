package me.vnagy.intellijplugins.argo.annotators.parameters

import com.intellij.psi.PsiFile
import com.intellij.testFramework.UsefulTestCase
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase
import me.vnagy.intellijplugins.argo.references.workflowname.WorkflowTemplateNameReferenceTest
import me.vnagy.intellijplugins.argo.wrapper.ArgoParametersPsi
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiFileWrapper
import org.jetbrains.yaml.psi.YAMLFile
import org.junit.Assert.*

class ParameterNameMissingInTemplateCallQuickFixTest : BasePlatformTestCase() {

    private lateinit var testObj: ParameterNameMissingInTemplateCallQuickFix
    private lateinit var psiFile: PsiFile
    private lateinit var parametersElement: ArgoParametersPsi

    override fun getTestDataPath(): String {
        return WorkflowTemplateNameReferenceTest::class.java.getResource("/psi-files").toURI().path
    }

    override fun setUp() {
        super.setUp()
        psiFile = myFixture.configureByFile("arguments/missing-parameter-in-template.yml")
        parametersElement = ArgoPsiFileWrapper(psiFile as YAMLFile)
            .spec
            ?.getTemplateByName("steps-with-reference")
            ?.steps
            ?.getStepByName("echo-hello-world")
            ?.arguments
            ?.parameters!!

        testObj = ParameterNameMissingInTemplateCallQuickFix(parametersElement, listOf("name"))
    }

    fun testAddMissingParametersToPsiElementWhenInvoked() {
        testObj.invoke(project, null, psiFile)

        assertEquals(1, parametersElement.parameters.count())
        assertEquals("name", parametersElement.parameters.first().name)
    }
}
