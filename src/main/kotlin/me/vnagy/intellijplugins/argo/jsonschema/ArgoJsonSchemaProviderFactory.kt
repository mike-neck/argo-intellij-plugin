package me.vnagy.intellijplugins.argo.jsonschema;

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory
import com.jetbrains.jsonSchema.extension.SchemaType
import me.vnagy.intellijplugins.argo.util.isWorkflowFile
import me.vnagy.intellijplugins.argo.util.isWorkflowTemplateFile

class ArgoJsonSchemaProviderFactory : JsonSchemaProviderFactory {

    override fun getProviders(project: Project): List<JsonSchemaFileProvider> {
        return listOf(
            ArgoWorkflowSchemaFileProvider(project),
            ArgoWorkflowTemplateSchemaFileProvider(project)
        )
    }
}

class ArgoWorkflowSchemaFileProvider(private val project: Project) : JsonSchemaFileProvider {

    private val psiManager = PsiManager.getInstance(project)

    override fun getName() = "Argo Workflow Schema"

    override fun isAvailable(file: VirtualFile): Boolean {
        val psiFile = psiManager.findFile(file)
        return isWorkflowFile(psiFile)
    }

    override fun getSchemaFile(): VirtualFile? {
        return VfsUtil.findFileByURL(
            ArgoWorkflowSchemaFileProvider::class
                .java
                .getResource("/jsonschema/schemas/workflow.json")
        )
    }

    override fun getSchemaType(): SchemaType {
        return SchemaType.embeddedSchema
    }

}

class ArgoWorkflowTemplateSchemaFileProvider(private val project: Project) : JsonSchemaFileProvider {

    private val psiManager = PsiManager.getInstance(project)

    override fun getName() = "Argo WorkflowTemplate Schema"

    override fun isAvailable(file: VirtualFile): Boolean {
        val psiFile = psiManager.findFile(file)
        return isWorkflowTemplateFile(psiFile)
    }

    override fun getSchemaFile(): VirtualFile? {
        return VfsUtil.findFileByURL(
            ArgoWorkflowSchemaFileProvider::class
                .java
                .getResource("/jsonschema/schemas/workflowtemplate.json")
        )
    }

    override fun getSchemaType(): SchemaType {
        return SchemaType.embeddedSchema
    }

}
