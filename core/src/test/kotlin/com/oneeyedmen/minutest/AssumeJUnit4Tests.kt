package com.oneeyedmen.minutest

import com.oneeyedmen.minutest.junit.JUnit4Minutests
import example.junit4.AssumeInNestedContext
import example.junit4.AssumeInRootContext
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.Test
import org.junit.platform.engine.discovery.DiscoverySelectors
import org.junit.platform.launcher.EngineFilter
import org.junit.platform.launcher.LauncherDiscoveryRequest
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory
import org.junit.vintage.engine.descriptor.VintageTestDescriptor
import kotlin.reflect.KClass


class AssumeJUnit4Tests : JUnit4Minutests() {
    private val listener = JUnit5TestLogger()
    
    @Test
    fun testRootContext() {
        run<AssumeInRootContext>()
        assertTrue(listener.log.joinToString("\n"), listener.log.any { it.startsWith("test aborted") })
    }
    
    @Test
    fun testNestedContext() {
        run<AssumeInNestedContext>()
        assertTrue(listener.log.joinToString("\n"), listener.log.any { it.startsWith("test aborted") })
    }
    
    private inline fun <reified T : Any> run() {
        run(T::class)
    }
    
    private fun run(testClass: KClass<*>) {
        LauncherFactory.create().execute(discoveryRequest(testClass), listener)
    }
    
    private fun discoveryRequest(testClass: KClass<*>): LauncherDiscoveryRequest {
        return LauncherDiscoveryRequestBuilder.request()
            .filters(EngineFilter.includeEngines(VintageTestDescriptor.ENGINE_ID))
            .selectors(DiscoverySelectors.selectClass(testClass.java))
            .build()
    }
}
