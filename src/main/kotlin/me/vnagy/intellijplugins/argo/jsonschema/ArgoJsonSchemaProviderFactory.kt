package me.vnagy.intellijplugins.argo.jsonschema;

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory
import com.jetbrains.jsonSchema.extension.SchemaType
import me.vnagy.intellijplugins.argo.util.isWorkflowFile
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl

class ArgoJsonSchemaProviderFactory : JsonSchemaProviderFactory {

    override fun getProviders(project: Project): List<JsonSchemaFileProvider> {
        return listOf(ArgoJsonSchemaFileProvider(project))
    }
}

class ArgoJsonSchemaFileProvider(private val project: Project) : JsonSchemaFileProvider {

    private val psiManager = PsiManager.getInstance(project)

    override fun getName() = "Argo Json Schema"

    override fun isAvailable(file: VirtualFile): Boolean {
        val psiFile = psiManager.findFile(file)
        return isWorkflowFile(psiFile)
    }

    override fun getSchemaFile(): VirtualFile? {
        return VfsUtil.findFileByURL(
            ArgoJsonSchemaFileProvider::class
                .java
                .getResource("/jsonschema/schemas/workflow.json")
        )
    }

    override fun getSchemaType(): SchemaType {
        return SchemaType.embeddedSchema
    }

}
