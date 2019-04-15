package dev.minutest

import dev.minutest.experimental.TestAnnotation

/**
 * Common interface for the DSL components that build [Node]s.
 */
interface NodeBuilder<F> {

    fun buildNode(): Node<F>

    /**
     * Experimental - see [TestAnnotation].
     */
    fun prependAnnotation(annotation: TestAnnotation<F>)

    val transforms: MutableList<NodeTransform<F>>

}

/**
 * Marker interface for the root [Context] builder.
 */
interface RootContextBuilder<F> : NodeBuilder<Unit>