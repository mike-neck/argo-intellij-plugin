package me.vnagy.intellijplugins.argo.completioncontributor.dependencies

import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import me.vnagy.intellijplugins.argo.references.callsitetemplate.CallsiteTemplateNameReferenceTest
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiFileWrapper
import org.jetbrains.yaml.psi.YAMLFile

class DagDependencyAutoCompletionContributorTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return DagDependencyAutoCompletionContributorTest::class.java.getResource("/psi-files").toURI().path
    }

    fun testShouldCompleteOnlyAutoCompleteElementWhenSpaceIsAfterNameKey() {
        val psiYamlFile = myFixture.configureByFile("dependencies-autocompletion/basic-dag-workflow.yml") as YAMLFile
        val argoPsi = ArgoPsiFileWrapper(psiYamlFile)
        val dependencyElement = argoPsi
            .spec
            ?.getTemplateByName("dag-with-reference")
            ?.dag
            ?.getTaskByName("dependent-on-hello")
            ?.dependencies
            ?.get(0)!!

        myFixture.editor.caretModel.moveToOffset(dependencyElement.psiElement.firstChild.endOffset)
        myFixture.type(" ")

        assertNull(dependencyElement.value)

        val lookupElements = myFixture.completeBasic()

        // `completeBasic` returns null if the only completion was inserted
        assertNull(lookupElements)
        assertEquals("echo-hello", dependencyElement.value)
    }
}

