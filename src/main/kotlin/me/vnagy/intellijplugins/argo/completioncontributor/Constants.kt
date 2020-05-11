package me.vnagy.intellijplugins.argo.completioncontributor

import com.intellij.openapi.util.Key
import me.vnagy.intellijplugins.argo.wrapper.ArgoParameterPsi
import me.vnagy.intellijplugins.argo.wrapper.ArgoPsiDagDependency

val PARAMETERS_ELEMENT: Key<ArgoParameterPsi> = Key("PARAMETERS_ELEMENT")
val DEPENDENCIES_ELEMENT: Key<ArgoPsiDagDependency> = Key("DEPENDENCIES_ELEMENT")
