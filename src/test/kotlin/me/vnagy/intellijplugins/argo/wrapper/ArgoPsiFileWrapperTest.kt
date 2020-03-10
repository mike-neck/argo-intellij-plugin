package me.vnagy.intellijplugins.argo.wrapper

import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.YAMLKeyValue

class ArgoPsiFileWrapperTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return ArgoPsiFileWrapperTest::class.java.getResource("/psi-files").toURI().path
    }

    fun testShouldReturnApiAndKindCorrectly() {
        val psiYamlFile = myFixture.configureByFile("basic-workflow.yml")
        val testObj = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        assertEquals("argoproj.io/v1alpha1", testObj.apiVersion)
        assertEquals("Workflow", testObj.kind)
    }

    fun testShouldReturnCorrectTemplateDefinitionsFromFile() {
        val psiYamlFile = myFixture.configureByFile("basic-workflow.yml")
        val testObj = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val templates = testObj.spec?.templates ?: emptyList()

        assertEquals(1, templates.size)

        val unzipTemplate = templates[0]
        assertEquals("unzip-archive-into-dir", unzipTemplate.name)
    }

    fun testShouldFindWrapperElementFromPsiElement() {
        val psiYamlFile = myFixture.configureByFile("basic-workflow.yml")
        val testObj = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val namePsiField = flatten(psiYamlFile)
            .filterIsInstance<YAMLKeyValue>()
            .filter { it.keyText == "name" && it.valueText == "unzip-archive-into-dir" }
            .uniqueOrNull()
            ?.value!!

        val namePsiWrappedField = testObj.findChildrenForPsiElement(namePsiField)!!
        assertTrue(namePsiWrappedField is GenericArgoPsiWrapper)
        assertTrue(namePsiWrappedField.parentElement is ArgoPsiTemplateSpec)
        assertEquals("unzip-archive-into-dir", (namePsiWrappedField.parentElement as ArgoPsiTemplateSpec).name)
    }

    fun testShouldParseDagTemplateSpecificationNameCorrectly() {
        val psiYamlFile = myFixture.configureByFile("basic-dag-workflow.yml")
        val testObj = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)

        val dagTask = testObj
            .spec
            ?.templates
            ?.filter { it.name == "dag-with-reference" }
            ?.uniqueOrNull()
            ?.dag
            ?.tasks
            ?.filter { it.template == "echo-hello-world" }
            ?.uniqueOrNull()

        assertNotNull(dagTask)
    }

    private fun flatten(psiElement: PsiElement): Sequence<PsiElement> {
        return sequenceOf(psiElement) + psiElement.children.asSequence().flatMap { flatten(it) }
    }
}
