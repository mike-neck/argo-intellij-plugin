package me.vnagy.intellijplugins.argo.annotators.parameters

import com.intellij.lang.annotation.AnnotationBuilder
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.nhaarman.mockitokotlin2.*
import me.vnagy.intellijplugins.argo.references.callsitetemplate.CallsiteTemplateNameReferenceTest
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiFileWrapper
import org.jetbrains.yaml.psi.YAMLFile
import org.mockito.Mockito.RETURNS_DEEP_STUBS

class ParameterNameMissingInTemplateCallAnnotatorTest  : BasePlatformTestCase() {

    private val testObj = ParameterNameMissingInTemplateCallAnnotator()

    override fun getTestDataPath(): String {
        return CallsiteTemplateNameReferenceTest::class.java.getResource("/psi-files").toURI().path
    }

    fun testShouldReportErrorOnMissingParameterInStepWorkflow() {
        val psiYamlFile = myFixture.configureByFile("arguments/steps/empty-parameters-in-template-call.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val annotator: AnnotationHolder = mock(defaultAnswer = RETURNS_DEEP_STUBS)
        val annotationBuilder: AnnotationBuilder = mock()

        whenever(annotator.newAnnotation(any(), any())).thenReturn(annotationBuilder)

        val parametersPsiElement = argoPsiWrapper
            .spec
            ?.getTemplateByName("steps-with-reference")
            ?.steps
            ?.getStepByName("echo-hello-world")
            ?.arguments
            ?.parameters
            ?.psiElement
            ?.key!!

        testObj.annotate(parametersPsiElement, annotator)

        verify(annotator).newAnnotation(
            HighlightSeverity.ERROR,
            "The parameter(s) [name] are missing from the template."
        )
        verify(annotationBuilder).create()
    }

    fun testShouldReportErrorOnMissingParameterInDagWorkflow() {
        val psiYamlFile = myFixture.configureByFile("arguments/dag/empty-parameters-in-template-call.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val annotator: AnnotationHolder = mock(defaultAnswer = RETURNS_DEEP_STUBS)
        val annotationBuilder: AnnotationBuilder = mock()

        whenever(annotator.newAnnotation(any(), any())).thenReturn(annotationBuilder)

        val parametersPsiElement = argoPsiWrapper
            .spec
            ?.getTemplateByName("dag-with-reference")
            ?.dag
            ?.getTaskByName("echo-hello-world")
            ?.arguments
            ?.parameters
            ?.psiElement
            ?.key!!

        testObj.annotate(parametersPsiElement, annotator)

        verify(annotator).newAnnotation(
            HighlightSeverity.ERROR,
            "The parameter(s) [name] are missing from the template."
        )
        verify(annotationBuilder).create()
    }

    fun testShouldReportErrorOnMissingParameterInWorkflowOnTheTemplateWhenTheWorkflowDoesNotHaveTheParametersTag() {
        val psiYamlFile = myFixture.configureByFile("arguments/steps/empty-parameters-in-template-call-without-arguments.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val annotator: AnnotationHolder = mock(defaultAnswer = RETURNS_DEEP_STUBS)
        val annotationBuilder: AnnotationBuilder = mock()

        whenever(annotator.newAnnotation(any(), any())).thenReturn(annotationBuilder)

        val templatePsiElement = argoPsiWrapper
            .spec
            ?.getTemplateByName("steps-with-reference")
            ?.steps
            ?.getStepByName("echo-hello-world")
            ?.templatePsiElement!!

        testObj.annotate(templatePsiElement, annotator)

        verify(annotator).newAnnotation(
            HighlightSeverity.ERROR,
            "The parameter(s) [name] are missing from the template."
        )
        verify(annotationBuilder).create()
    }

    fun testShouldReportErrorOnMissingParameterInDagWorkflowOnTheTemplateWhenTheWorkflowDoesNotHaveTheParametersTag() {
        val psiYamlFile = myFixture.configureByFile("arguments/dag/empty-parameters-in-template-call-without-arguments.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val annotator: AnnotationHolder = mock(defaultAnswer = RETURNS_DEEP_STUBS)
        val annotationBuilder: AnnotationBuilder = mock()

        whenever(annotator.newAnnotation(any(), any())).thenReturn(annotationBuilder)

        val templatePsiElement = argoPsiWrapper
            .spec
            ?.getTemplateByName("dag-with-reference")
            ?.dag
            ?.getTaskByName("echo-hello-world")
            ?.templatePsiElement!!

        testObj.annotate(templatePsiElement, annotator)

        verify(annotator).newAnnotation(
            HighlightSeverity.ERROR,
            "The parameter(s) [name] are missing from the template."
        )
        verify(annotationBuilder).create()
    }

    fun testShouldNotReportErrorOnMissingParameterInWorkflowOnTheTemplateWhenTheWorkflowDoesHaveTheParametersTag() {
        val psiYamlFile = myFixture.configureByFile("arguments/steps/empty-parameters-in-template-call.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val annotator: AnnotationHolder = mock(defaultAnswer = RETURNS_DEEP_STUBS)
        val annotationBuilder: AnnotationBuilder = mock()

        whenever(annotator.newAnnotation(any(), any())).thenReturn(annotationBuilder)

        val templatePsiElement = argoPsiWrapper
            .spec
            ?.getTemplateByName("steps-with-reference")
            ?.steps
            ?.getStepByName("echo-hello-world")
            ?.templatePsiElement!!

        testObj.annotate(templatePsiElement, annotator)

        verify(annotator, never()).newAnnotation(
            HighlightSeverity.ERROR,
            "The parameter(s) [name] are missing from the template."
        )
        verify(annotationBuilder, never()).create()
    }

    fun testShouldNotReportErrorOnMissingParameterInOtherParameterDefinition() {
        val psiYamlFile = myFixture.configureByFile("arguments/steps/empty-parameters-in-template-call.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val annotator: AnnotationHolder = mock(defaultAnswer = RETURNS_DEEP_STUBS)
        val parameterPsiElement = argoPsiWrapper
            .spec
            ?.getTemplateByName("steps-with-reference")
            ?.steps
            ?.getStepByName("echo-hello-world")
            ?.arguments
            ?.parameters
            ?.getParameterByName("foobar")
            ?.psiElement!!

        testObj.annotate(parameterPsiElement, annotator)

        verify(annotator, never()).newAnnotation(
            any(),
            any()
        )
    }

    fun testShouldNotReportErrorWhenTheParameterHasValueDefinedInTemplate() {
        val psiYamlFile = myFixture.configureByFile("arguments/parameter-defined-in-the-template.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val annotator: AnnotationHolder = mock(defaultAnswer = RETURNS_DEEP_STUBS)
        val parametersPsiElement = argoPsiWrapper
            .spec
            ?.getTemplateByName("steps-with-reference")
            ?.steps
            ?.getStepByName("echo-hello-world")
            ?.arguments
            ?.parameters
            ?.psiElement
            ?.key!!

        testObj.annotate(parametersPsiElement, annotator)

        verify(annotator, never()).newAnnotation(any(), any())
    }

    // TODO #7
    fun _testShouldAddParameterNameQuickFix() {
        val psiYamlFile = myFixture.configureByFile("arguments/steps/empty-parameters-in-template-call.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val annotator: AnnotationHolder = mock(defaultAnswer = RETURNS_DEEP_STUBS)
        val annotationBuilder: AnnotationBuilder = mock()

        whenever(annotator.newAnnotation(any(), any())).thenReturn(annotationBuilder)

        val parametersPsiElement = argoPsiWrapper
            .spec
            ?.getTemplateByName("steps-with-reference")
            ?.steps
            ?.getStepByName("echo-hello-world")
            ?.arguments
            ?.parameters
            ?.psiElement
            ?.key!!

        testObj.annotate(parametersPsiElement, annotator)

        verify(annotator).newAnnotation(any(), any())
        verify(annotationBuilder).create()
    }
}
