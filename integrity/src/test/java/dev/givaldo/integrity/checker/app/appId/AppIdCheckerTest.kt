package dev.givaldo.integrity.checker.app.appId

import android.content.Context
import dev.givaldo.integrity.SecurityCheck
import dev.givaldo.integrity.ValidationType
import dev.givaldo.integrity.checker.app.AppFlagReason.AppIdNotMatching
import dev.givaldo.integrity.common.stubConfiguration
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class AppIdCheckerTest {
    private val contextMock = mockk<Context>()
    private val subject = AppIdChecker(context = contextMock, configuration = stubConfiguration)


    @Test
    fun `getIdentifier validation type verification`() {
        Assert.assertEquals(ValidationType.AppPackageName, subject.identifier)
    }

    @Test
    fun `check function success on matching package names`() = runTest {
        every { contextMock.packageName } returns stubConfiguration.appId
        val result = subject.check()
        Assert.assertEquals(SecurityCheck.Secure, result)
    }

    @Test
    fun `check function failure on null expectedAppId`() = runTest {
        val conf = stubConfiguration.copy(appId = null)
        val subject = AppIdChecker(context = contextMock, configuration = conf)
        val result = subject.check()
        Assert.assertTrue(result is SecurityCheck.Error)
    }

    @Test
    fun `check function failure on blank expectedAppId`() = runTest {
        val conf = stubConfiguration.copy(appId = "")
        val subject = AppIdChecker(context = contextMock, configuration = conf)
        val result = subject.check()
        Assert.assertTrue(result is SecurityCheck.Error)
    }

    @Test
    fun `check function flagged on mismatched package names`() = runTest {
        every { contextMock.packageName } returns "com.example.otherapp"
        val result = subject.check()
        Assert.assertTrue(result is SecurityCheck.Flagged)
    }

    @Test
    fun `check function error code verification`() = runTest {
        every { contextMock.packageName } returns "com.example.otherapp"
        val result = subject.check()
        Assert.assertTrue((result as SecurityCheck.Flagged).code == AppIdNotMatching.code)
    }
}