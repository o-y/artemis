package wtf.zv.artemis.core.component

/** Composable fragment of HTML and CSS contents. */
abstract class ComponentPlugin {
    /**
     * Defines the contents of the given component, this should be created using the [createComponent] DSL.
     */
    abstract fun provideContents(): ComponentContents
}