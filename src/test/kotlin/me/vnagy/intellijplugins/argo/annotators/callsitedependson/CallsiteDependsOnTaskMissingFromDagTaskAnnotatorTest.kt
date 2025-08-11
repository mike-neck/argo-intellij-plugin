package me.vnagy.intellijplugins.argo.annotators.callsitedependson

import com.intellij.lang.annotation.AnnotationBuilder
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import me.vnagy.intellijplugins.argo.psiFiles
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiFileWrapper
import org.jetbrains.yaml.psi.YAMLFile
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CallsiteDependsOnTaskMissingFromDagTaskAnnotatorTest : BasePlatformTestCase() {

    private val testObj = CallsiteDependsOnTaskMissingFromDagTaskAnnotator()

    override fun getTestDataPath(): String {
        return psiFiles()
    }

    fun testShouldReportErrorOnDependsOnTextWhenItRefersToANonExistingTask() {
        val psiYamlFile = myFixture.configureByFile("dependencies/dag/non-existing-dag-task-dependency.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val annotator: AnnotationHolder = mock(Mockito.RETURNS_DEEP_STUBS)
        val annotationBuilder: AnnotationBuilder = mock()

        whenever(annotator.newAnnotation(any(), any())).thenReturn(annotationBuilder)

        val parametersPsiElement = argoPsiWrapper
            .spec
            ?.getTemplateByName("dag-with-reference")
            ?.dag
            ?.getTaskByName("dependent-on-non-existing-task")
            ?.dependencies
            ?.get(0)
            ?.valuePsiElement!!

        testObj.annotate(parametersPsiElement, annotator)

        verify(annotator).newAnnotation(
            HighlightSeverity.ERROR,
            "The dag task `this-task-does-not-exists` don't exist in the parent DAG template."
        )
        verify(annotationBuilder).create()
    }
}
