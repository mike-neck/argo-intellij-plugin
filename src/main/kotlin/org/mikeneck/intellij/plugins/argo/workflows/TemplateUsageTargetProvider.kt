package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.usages.UsageTarget
import com.intellij.usages.UsageTargetProvider

class TemplateUsageTargetProvider: UsageTargetProvider {

    override fun getTargets(
        editor: Editor,
        file: PsiFile
    ): Array<out UsageTarget?>? {
        val caretModel = editor.caretModel
        val offset = caretModel.offset
        val element = file.findElementAt(offset) ?: return null
        return getTargets(editor, file, element)
    }

    override fun getTargets(psiElement: PsiElement): Array<out UsageTarget?>? {
        val psiFile = psiElement.containingFile ?: return null
        val virtualFile: VirtualFile = psiFile.viewProvider.virtualFile
        val document = FileDocumentManager.getInstance().getDocument(virtualFile) ?: return null

        val editorFactory = EditorFactory.getInstance()
        val project = psiFile.project
        val editor =
            editorFactory.getEditors(document, project).firstOrNull() ?: editorFactory.createEditor(document, project)
            ?: return null
        return getTargets(editor, psiFile, psiElement)
    }

    fun getTargets(editor: Editor, file: PsiFile, element: PsiElement): Array<out UsageTarget?>? {
        TODO("Not yet implemented")
    }
}
