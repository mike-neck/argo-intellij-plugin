package me.vnagy.intellijplugins.argo

import java.io.InputStream

inline fun <reified T: Any> T.psiFiles(path: String = "/psi-files"): String {
    return T::class.java.getResource(path)?.toURI()?.path?: error("Can't find $path")
}

inline fun <reified T: Any> T.resourceAsStream(path: String = "/psi-files"): InputStream {
    return T::class.java.getResourceAsStream(path)?: error("Can't find $path")
}


