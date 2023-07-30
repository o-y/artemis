package api

/**
 * Annotates a function which should be exposed to JVM targets to specify the annotated functions usage in the browser.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
@SinceKotlin("1.3")
public annotation class ArtemisFunction(val optionalKey: String = "")
