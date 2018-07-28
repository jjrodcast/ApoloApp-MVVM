/*
 * *
 *  * Developed by Jorge Rodriguez on 27/07/18 11:35 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 23/07/18 04:31 PM
 *
 */
package pe.com.onecode.apoloapp

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("pe.com.onecode.apoloapp", appContext.packageName)
    }
}
