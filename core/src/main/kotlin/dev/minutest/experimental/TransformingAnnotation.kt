package dev.minutest.experimental

import dev.minutest.NodeBuilder
import dev.minutest.NodeTransform

/**
 * Convenience implementation of [TestAnnotation].
 */
open class TransformingAnnotation<in F>(
    private val transform: NodeTransform<@UnsafeVariance F>
) : TestAnnotation<F> {

    override fun applyTo(nodeBuilder: NodeBuilder<@UnsafeVariance F>) {
        nodeBuilder.transforms.add(0,  transform)
    }
}