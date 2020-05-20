package me.vnagy.intellijplugins.argo.jsonschema;

import com.intellij.openapi.project.Project
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory

class ArgoJsonSchemaProviderFactory : JsonSchemaProviderFactory {

    override fun getProviders(project: Project): List<JsonSchemaFileProvider> {
        return listOf(
            ArgoWorkflowSchemaFileProvider(project),
            ArgoWorkflowTemplateSchemaFileProvider(project),
            ArgoCronWorkflowSchemaFileProvider(project)
        )
    }
}

