package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.yaml.psi.YAMLFile

class ArgoParametersPsiTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return ArgoPsiFileWrapperTest::class.java.getResource("/psi-files").toURI().path
    }

    fun testShouldParseParameterNameCorrectly() {
        val psiYamlFile = myFixture.configureByFile("basic-parsing/parameters/template-with-parameters.yaml")
        val psiFileWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)

        val parameters = psiFileWrapper
            .spec
            ?.getTemplateByName("steps-with-reference")
            ?.steps
            ?.getStepByName("echo-hello-world-in-step")
            ?.arguments
            ?.parameters
            ?.parameters
            ?.toList() ?: emptyList()

        assertEquals(1, parameters.size)
        assertEquals("name", parameters[0].name)
    }

    fun testShouldParseEmptyParametersArray() {
        val psiYamlFile = myFixture.configureByFile("basic-parsing/parameters/missing-parameter-in-template.yml")
        val psiFileWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)

        val parameters = psiFileWrapper
            .spec
            ?.getTemplateByName("steps-with-reference")
            ?.steps
            ?.getStepByName("echo-hello-world-in-step")
            ?.arguments
            ?.parameters
            ?.parameters
            ?.toList()

        assertEquals(0, parameters?.size)
    }
}

