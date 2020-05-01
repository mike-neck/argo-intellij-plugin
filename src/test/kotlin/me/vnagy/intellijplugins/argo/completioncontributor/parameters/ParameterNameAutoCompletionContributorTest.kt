package me.vnagy.intellijplugins.argo.completioncontributor.parameters

import com.intellij.refactoring.suggested.endOffset
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import me.vnagy.intellijplugins.argo.references.callsitetemplate.CallsiteTemplateNameReferenceTest
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiFileWrapper
import org.jetbrains.yaml.psi.YAMLFile

class ParameterNameAutoCompletionContributorTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return CallsiteTemplateNameReferenceTest::class.java.getResource("/psi-files").toURI().path
    }

    fun testShouldCompleteOnlyAutoCompleteElementWhenSpaceIsAfterNameKey() {
        val psiYamlFile = myFixture.configureByFile("parameter-autocompletion/basic-steps-workflow.yml") as YAMLFile
        val argoPsi = ArgoPsiFileWrapper(psiYamlFile)
        val parameterElement = argoPsi
            .spec
            ?.getTemplateByName("steps-with-reference")
            ?.steps
            ?.getStepByName("hello-vilmos")
            ?.arguments
            ?.parameters
            ?.parameters
            ?.first()!!

        myFixture.editor.caretModel.moveToOffset(parameterElement.psiElement.endOffset)
        myFixture.type(" ")

        assertNull(parameterElement.name)

        val lookupElements = myFixture.completeBasic()

        // `completeBasic` returns null if the only completion was inserted
        assertNull(lookupElements)
        assertEquals("firstName", parameterElement.name)
    }

    fun testNotShouldCompleteAnythingOnOtherElements() {
        val psiYamlFile = myFixture.configureByFile("parameter-autocompletion/basic-steps-workflow.yml") as YAMLFile
        val argoPsi = ArgoPsiFileWrapper(psiYamlFile)
        val parametersElement = argoPsi
            .spec
            ?.getTemplateByName("steps-with-reference")
            ?.steps
            ?.getStepByName("hello-vilmos")
            ?.arguments
            ?.parameters!!

        myFixture.editor.caretModel.moveToOffset(parametersElement.psiElement.endOffset)

        val lookupElements = myFixture.completeBasic()
        assertEquals(0, lookupElements.size)
    }
}

