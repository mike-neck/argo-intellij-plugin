package me.vnagy.intellijplugins.argo.jsonschema

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.SchemaType
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiFileWrapper
import org.jetbrains.yaml.psi.YAMLFile

class ArgoWorkflowSchemaFileProvider(private val project: Project) :
    JsonSchemaFileProvider {

    private val psiManager = PsiManager.getInstance(project)

    override fun getName() = "Argo Workflow Schema"

    override fun isAvailable(file: VirtualFile): Boolean {
        return runReadAction {
            val psiFile = psiManager.findFile(file)
            if (psiFile is YAMLFile) {
                val argoWrapper = ArgoPsiFileWrapper(psiFile)
                return@runReadAction argoWrapper.apiVersion == "argoproj.io/v1alpha1" && argoWrapper.kind == "Workflow"
            }
            return@runReadAction false
        }
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
