package me.vnagy.intellijplugins.argo.wrapper

import org.jetbrains.yaml.psi.YAMLSequence
import org.jetbrains.yaml.psi.YAMLSequenceItem

class ArgoPsiSteps(
    override val psiElement: YAMLSequence,
    override val parentElement: ArgoPsiTemplateSpec
) : ArgoPsi<YAMLSequence> {

    fun getStepByName(stepName: String): ArgoPsiStepSpecification? {
        return children
            .filter { it.name == stepName }
            .uniqueOrNull()

    }

    override val children: Sequence<ArgoPsiStepSpecification>
        get() {
            return psiElement
                .children
                .asSequence()
                .map { it as YAMLSequenceItem }
                .map { ArgoPsiStepSpecification(it, this) }
        }

}
