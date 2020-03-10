package me.vnagy.intellijplugins.argo.references.workflowname

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiFileWrapper
import me.vnagy.intellijplugins.argo.wrapper.uniqueOrNull
import org.jetbrains.yaml.psi.YAMLFile

class WorkflowTemplateNameReferenceTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return WorkflowTemplateNameReferenceTest::class.java.getResource("/psi-files").toURI().path
    }

    fun testShouldResolveDagTemplateNameReference() {
        val psiYamlFile = myFixture.configureByFile("dag-and-steps-workflow.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val dagTemplateReference = argoPsiWrapper
            .spec
            ?.getTemplateByName("dag-with-reference")
            ?.dag
            ?.getTaskByName("steps-with-reference")
            ?.templatePsiElement!!

        val expectedTemplateNameElement = argoPsiWrapper
            .spec
            ?.getTemplateByName("steps-with-reference")
            ?.namePsiElement

        val testObj = WorkflowTemplateNameReference(dagTemplateReference)
        val resolvedElement = testObj.resolve()
        assertNotNull(resolvedElement)
        assertEquals(expectedTemplateNameElement, resolvedElement)
    }

    fun testShouldResolveDagTemplateNameReferenceWhenDagTaskHasNoName() {
        val psiYamlFile = myFixture.configureByFile("basic-dag-workflow.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val dagTemplateReference = argoPsiWrapper
            .spec
            ?.getTemplateByName("dag-with-reference")
            ?.dag
            ?.tasks
            ?.uniqueOrNull()
            ?.templatePsiElement!!

        val expectedTemplateNameElement = argoPsiWrapper
            .spec
            ?.getTemplateByName("echo-hello-world")
            ?.namePsiElement

        val testObj = WorkflowTemplateNameReference(dagTemplateReference)
        val resolvedElement = testObj.resolve()
        assertNotNull(resolvedElement)
        assertEquals(expectedTemplateNameElement, resolvedElement)
    }

    fun testShouldResolveStepsTemplateNameReference() {
        val psiYamlFile = myFixture.configureByFile("dag-and-steps-workflow.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val stepTemplateReference = argoPsiWrapper
            .spec
            ?.getTemplateByName("steps-with-reference")
            ?.steps
            ?.getStepByName("echo-hello-world")
            ?.templatePsiElement!!

        val expectedTemplateNameElement = argoPsiWrapper
            .spec
            ?.getTemplateByName("echo-hello-world")
            ?.namePsiElement

        val testObj = WorkflowTemplateNameReference(stepTemplateReference)
        val resolvedElement = testObj.resolve()
        assertNotNull(resolvedElement)
        assertEquals(expectedTemplateNameElement, resolvedElement)
    }

    fun testShouldNotResolveDagNameElement() {
        val psiYamlFile = myFixture.configureByFile("dag-and-steps-workflow.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val namePsiElement = argoPsiWrapper
            .spec
            ?.getTemplateByName("dag-with-reference")
            ?.dag
            ?.getTaskByName("steps-with-reference")
            ?.namePsiElement!!

        val testObj = WorkflowTemplateNameReference(namePsiElement)
        val resolvedElement = testObj.resolve()
        assertNull(resolvedElement)
    }

    fun testShouldNotResolveStepsNameElement() {
        val psiYamlFile = myFixture.configureByFile("dag-and-steps-workflow.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val stepTemplateReference = argoPsiWrapper
            .spec
            ?.getTemplateByName("steps-with-reference")
            ?.steps
            ?.getStepByName("echo-hello-world")
            ?.namePsiElement!!

        val testObj = WorkflowTemplateNameReference(stepTemplateReference)
        val resolvedElement = testObj.resolve()
        assertNull(resolvedElement)
    }
}

