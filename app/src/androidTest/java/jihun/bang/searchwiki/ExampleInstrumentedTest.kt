package jihun.bang.searchwiki

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import jihun.bang.searchwiki.network.NetworkModule
import jihun.bang.searchwiki.network.Protocol
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("jihun.bang.searchwiki", appContext.packageName)
    }

    @Test
    fun `HTTP_테스트`() {
        val result = NetworkModule.setConnection(
            protocol = Protocol.HTTP,
            api = "summary/google"
        )
        println(result)
    }

    @Test
    fun `HTTPS_테스트`() {
        val result = NetworkModule.setConnection(
            protocol = Protocol.HTTP,
            api = "summary/google"
        )
        println(result)
    }

    @Test
    fun `GET_테스트`() {
        val result = NetworkModule.setConnection(
            protocol = Protocol.HTTPS,
            uri = "webhook.site/24911492-a7ac-42b8-a2ca-feaf553e351f",
            api = "",
            method = "GET"
        )
        println("result =$result")
    }

    @Test
    fun `POST_테스트`() {
        val result = NetworkModule.setConnection(
            uri = "webhook.site/24911492-a7ac-42b8-a2ca-feaf553e351f",
            api = "",
            method = "POST",
            postData = """
                {
                    "method": "POST",
                    "name": "JIHUN BANG",
                    "age": 30
                }
            """.trimIndent(),
            requestProperties = listOf(
                "accept" to "test-accept",
                "content-Type" to "test-content"
            )
        )
        println(result)
    }

    @Test
    fun `DELETE_테스트`() {
        val result = NetworkModule.setConnection(
            uri = "webhook.site/24911492-a7ac-42b8-a2ca-feaf553e351f",
            api = "",
            method = "DELETE",
            postData = """
                {
                    "method": "DELETE",
                    "name": "JIHUN BANG",
                    "age": 30
                }
            """.trimIndent(),
        )
        println(result)
    }

    @Test
    fun `PUT_테스트`() {
        val result = NetworkModule.setConnection(
            uri = "webhook.site/24911492-a7ac-42b8-a2ca-feaf553e351f",
            api = "",
            method = "PUT",
            postData = """
                {
                    "method": "PUT",
                    "name": "JIHUN BANG",
                    "age": 30
                }
            """.trimIndent(),
        )
        println(result)
    }

    @Test
    fun `Response_Data_테스트`() {
        val result = NetworkModule.setConnection(
            protocol = Protocol.HTTPS,
            api = "html/google",
            method = "GET",
            requestProperties = listOf(
                "charset" to "utf-8",
                "profile" to "https://www.mediawiki.org/wiki/Specs/HTML/2.1.0"
            )
        )
        println("result =$result")
    }
}