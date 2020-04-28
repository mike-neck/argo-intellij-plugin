package me.vnagy.intellijplugins.argo.annotators.parameters

import com.intellij.lang.annotation.Annotation
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

    fun testShouldReportErrorOnMissingParameterInWorkflow() {
        val psiYamlFile = myFixture.configureByFile("arguments/empty-parameters-in-template-call.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val annotator: AnnotationHolder = mock(defaultAnswer = RETURNS_DEEP_STUBS)
        val parametersPsiElement = argoPsiWrapper
            .spec
            ?.getTemplateByName("steps-with-reference")
            ?.steps
            ?.getStepByName("echo-hello-world")
            ?.arguments
            ?.parameters!!

        testObj.annotate(parametersPsiElement.psiElement, annotator)

        verify(annotator).newAnnotation(
            HighlightSeverity.ERROR,
            "The parameter(s) [name] are missing from the template."
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
            ?.parameters!!

        testObj.annotate(parametersPsiElement.psiElement, annotator)

        verify(annotator, never()).newAnnotation(any(), any())
    }

    // TODO #7
    fun _testShouldAddParameterNameQuickFix() {
        val psiYamlFile = myFixture.configureByFile("arguments/empty-parameters-in-template-call.yml")
        val argoPsiWrapper = ArgoPsiFileWrapper(psiYamlFile as YAMLFile)
        val annotator: AnnotationHolder = mock(defaultAnswer = RETURNS_DEEP_STUBS)
        val annotation: Annotation = mock()

        whenever(annotator.createAnnotation(any(), any(), any())).thenReturn(annotation)

        val parametersPsiElement = argoPsiWrapper
            .spec
            ?.getTemplateByName("steps-with-reference")
            ?.steps
            ?.getStepByName("echo-hello-world")
            ?.arguments
            ?.parameters!!

        testObj.annotate(parametersPsiElement.psiElement, annotator)

        verify(annotator).createAnnotation(any(), any(), any())
        verify(annotation).registerFix(any())
    }
}
