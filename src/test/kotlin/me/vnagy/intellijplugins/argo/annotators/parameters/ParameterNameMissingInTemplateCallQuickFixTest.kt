package me.vnagy.intellijplugins.argo.annotators.parameters

import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import me.vnagy.intellijplugins.argo.references.callsitetemplate.CallsiteTemplateNameReferenceTest
import me.vnagy.intellijplugins.argo.wrapper.ArgoParametersPsi
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiFileWrapper
import org.jetbrains.yaml.psi.YAMLFile

class ParameterNameMissingInTemplateCallQuickFixTest : BasePlatformTestCase() {

    private lateinit var testObj: ParameterNameMissingInTemplateCallQuickFix
    private lateinit var psiFile: PsiFile
    private lateinit var parametersElement: ArgoParametersPsi

    override fun getTestDataPath(): String {
        return CallsiteTemplateNameReferenceTest::class.java.getResource("/psi-files").toURI().path
    }

    override fun isWriteActionRequired() = true

    override fun setUp() {
        super.setUp()
        psiFile = myFixture.configureByFile("arguments/empty-parameters-in-template-call.yml")
        parametersElement = ArgoPsiFileWrapper(psiFile as YAMLFile)
            .spec
            ?.getTemplateByName("steps-with-reference")
            ?.steps
            ?.getStepByName("echo-hello-world")
            ?.arguments
            ?.parameters!!

        testObj = ParameterNameMissingInTemplateCallQuickFix(parametersElement, listOf("name"))
    }

    fun testShouldAddTheMissingParameterAndAccessItByTheApi() {
        testObj.invoke(project, null, psiFile)

        assertEquals(1, parametersElement.parameters.count())
        assertEquals("name", parametersElement.parameters.first().name)
    }

    fun _testShouldAddTheMissingParameterToAnArrayOfParametersCorrectlyWhenTheArrayIsAYamlArray() {
        testObj.invoke(project, null, psiFile)

        val actualContent = psiFile.text
        val expectedContent = CallsiteTemplateNameReferenceTest::class.java
            .getResourceAsStream("/psi-files/arguments/expected-parameters-in-template-call.yml")
            .reader()
            .readText()

        assertEquals(expectedContent, actualContent)
    }
}
