package dev.minutest.examples

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.Assertions.assertEquals


class ImmutableExampleTests : JUnit5Minutests {

    // If you like this FP stuff, you may want to test an immutable fixture.
    fun tests() = rootContext<List<String>> {

        // List<String> is immutable
        fixture { emptyList() }

        // test_ allows you to return the fixture
        test_("add an item and return the fixture") {
            val newList = this + "item"
            assertEquals("item", newList.first())
            newList
        }

        // which will be available for inspection in after
        after {
            assertEquals("item", first())
        }
    }
}