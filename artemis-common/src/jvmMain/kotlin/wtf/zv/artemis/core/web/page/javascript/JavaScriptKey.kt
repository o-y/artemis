package wtf.zv.artemis.core.web.page.javascript

import wtf.zv.artemis.root.ArtemisBuildGraphRoot
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@SinceKotlin("1.3")
annotation class JavaScriptKey(vararg val javascriptKeys: KClass<out ArtemisBuildGraphRoot>)