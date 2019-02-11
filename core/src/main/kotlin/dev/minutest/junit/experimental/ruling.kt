package dev.minutest.junit.experimental

import dev.minutest.*
import dev.minutest.experimental.TestAnnotation
import dev.minutest.experimental.annotateWith
import org.junit.rules.TestRule
import org.junit.runner.Description.createTestDescription
import org.junit.runners.model.Statement

/**
 * Apply a JUnit test rule in a fixture
 */
fun <F, R : TestRule> ContextBuilder<F>.applyRule(ruleExtractor: F.() -> R) {
    annotateWith(
        ApplyRule(ruleExtractor)
    )
}

// TODO - I think this will fail if you change the fixture type between declaration and the test
class ApplyRule<F, R : TestRule>(private val ruleExtractor: F.() -> R) : TestAnnotation, NodeTransform {
    override fun <F2> applyTo(node: Node<F2>): Node<F2> = when (node) {
        is Test<F2> -> {
            Test(node.name, node.annotations) { fixture, testDescriptor ->
                fixture.also {
                    val rule = ruleExtractor(fixture as F)
                    val wrappedTestAsStatement = node.asJUnitStatement(fixture, testDescriptor)
                    val fullName = testDescriptor.fullName()
                    rule.apply(
                        wrappedTestAsStatement,
                        createTestDescription(fullName.first(), fullName.drop(1).joinToString("."))
                    ).evaluate()
                }
            }
        }
        is Context<F2, *> ->
            node.withTransformedChildren(this)
    }
}


private fun <F> Testlet<F>.asJUnitStatement(fixture: F, testDescriptor: TestDescriptor) = object : Statement() {
    override fun evaluate() {
        this@asJUnitStatement(fixture, testDescriptor)
    }
}