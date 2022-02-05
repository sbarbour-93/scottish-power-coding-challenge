package com.scottbarbour.projects

import com.scottbarbour.projects.di.myKoinModules
import org.junit.Test
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class CheckModulesTest : KoinTest {

    @Test
    fun `verify my Koin modules get created successfully`() {
        koinApplication {
            modules(myKoinModules)
            checkModules()
        }
    }
}