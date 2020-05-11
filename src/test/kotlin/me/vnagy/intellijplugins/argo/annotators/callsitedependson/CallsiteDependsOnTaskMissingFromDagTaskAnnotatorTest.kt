package me.vnagy.intellijplugins.argo.annotators.callsitedependson

import com.intellij.lang.annotation.AnnotationBuilder
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import me.vnagy.intellijplugins.argo.annotators.parameters.ParameterNameMissingInTemplateCallAnnotator
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiFileWrapper
import org.jetbrains.yaml.psi.YAMLFile
import org.mockito.Mockito

class CallsiteDependsOnTaskMissingFromDagTaskAnnotatorTest : BasePlatformTestCase() {

    private val testObj = CallsiteDependsOnTaskMissingFromDagTaskAnnotator()

    override fun getTestDataPath(): String {
        return CallsiteDependsOnTaskMissingFromDagTaskAnnotatorTest::class.java.getResource("/psi-files").toURI().path
    }

    fun testShouldReportErrorOnDependsOnTextWhenItRefersToANonExistingTask() {
        val psiYamlFile = myFixture.configureByFile("dependencies/dag/non-existing-dag-task-dependency.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val annotator: AnnotationHolder = mock(defaultAnswer = Mockito.RETURNS_DEEP_STUBS)
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
