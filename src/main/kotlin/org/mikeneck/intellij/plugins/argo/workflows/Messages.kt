package org.mikeneck.intellij.plugins.argo.workflows

import com.intellij.DynamicBundle
import java.util.function.Supplier

object Messages {

    const val BUNDLE_NAME = "messages.ArgoWorkflowsBundle"

    private val BUNDLE: DynamicBundle = DynamicBundle(Messages::class.java, BUNDLE_NAME)

    fun message(key: String, vararg params: Any): String = BUNDLE.getMessage(key, *params)

    fun messagePointer(key: String, vararg params: Any): Supplier<String> = BUNDLE.getLazyMessage(key, *params)
}
