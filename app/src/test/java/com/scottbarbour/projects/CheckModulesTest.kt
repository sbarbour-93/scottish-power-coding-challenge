package com.scottbarbour.projects

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.scottbarbour.projects.di.myKoinModules
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class CheckModulesTest : KoinTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `verify my Koin modules get created successfully`() {
        koinApplication {
            modules(myKoinModules)
            checkModules()
        }
    }
}