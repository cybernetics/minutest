package dev.minutest.experimental

import dev.minutest.Context
import dev.minutest.Node
import dev.minutest.NodeTransform
import dev.minutest.Test


fun <F> Node<F>.transformedBy(annotations: List<TestAnnotation>): Node<F> {
    val transforms: List<NodeTransform> = annotations.filterIsInstance<NodeTransform>()
    return if (transforms.isEmpty())
        this
    else {
        transforms.reduce(NodeTransform::then).applyTo(this)
    }
}

fun Node<*>.hasA(predicate: (Node<*>) -> Boolean): Boolean = when (this) {
    is Test<*> -> predicate(this)
    is Context<*, *> -> hasA(predicate)
}

fun Context<*, *>.hasA(predicate: (Node<*>) -> Boolean): Boolean {
    return predicate(this) || children.find { it.hasA(predicate) } != null
}