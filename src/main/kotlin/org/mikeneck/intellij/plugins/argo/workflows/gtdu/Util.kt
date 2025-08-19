package org.mikeneck.intellij.plugins.argo.workflows.gtdu

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import org.jetbrains.yaml.psi.YAMLPsiElement

inline fun <reified T: YAMLPsiElement> psiElement(): PsiElementPattern.Capture<T> = PlatformPatterns.psiElement(T::class.java)
