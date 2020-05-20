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

class ArgoCronWorkflowSchemaFileProvider(private val project: Project) :
    JsonSchemaFileProvider {

    private val psiManager = PsiManager.getInstance(project)

    override fun getName() = "Argo CronWorkflow Schema"

    override fun isAvailable(file: VirtualFile): Boolean {
        return runReadAction {
            val psiFile = psiManager.findFile(file)
            if (psiFile is YAMLFile) {
                val argoWrapper = ArgoPsiFileWrapper(psiFile)
                return@runReadAction argoWrapper.apiVersion == "argoproj.io/v1alpha1" && argoWrapper.kind == "CronWorkflow"
            }
            return@runReadAction false
        }
    }

    override fun getSchemaFile(): VirtualFile? {
        return VfsUtil.findFileByURL(
            ArgoCronWorkflowSchemaFileProvider::class
                .java
                .getResource("/jsonschema/schemas/cronworkflow.json")
        )
    }

    override fun getSchemaType(): SchemaType {
        return SchemaType.embeddedSchema
    }

}
