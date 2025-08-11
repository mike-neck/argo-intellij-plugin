package me.vnagy.intellijplugins.argo.references.callsitedependson

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import me.vnagy.intellijplugins.argo.psiFiles
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiFileWrapper
import org.jetbrains.yaml.psi.YAMLFile

class CallsiteDependsOnReferenceTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return psiFiles()
    }

    fun testShouldResolveDagDependsOnReferences() {
        val psiYamlFile = myFixture.configureByFile("dag-and-steps-workflow.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val dependencyDefinition = argoPsiWrapper
            .spec
            ?.getTemplateByName("dag-with-reference")
            ?.dag
            ?.getTaskByName("other-dag-step")
            ?.dependencies
            ?.get(0)
            ?.psiElement
            ?.value!!

        val expectedTemplateNameElement = argoPsiWrapper
            .spec
            ?.getTemplateByName("dag-with-reference")
            ?.dag
            ?.getTaskByName("steps-with-reference")
            ?.namePsiElement

        val testObj = CallsiteDependsOnReference(dependencyDefinition)
        val resolvedElement = testObj.resolve()
        assertNotNull(resolvedElement)
        assertEquals(
            "Expected `${expectedTemplateNameElement?.text}`, but was `${resolvedElement?.text}`.",
            expectedTemplateNameElement,
            resolvedElement
        )
    }
}

