package api

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.junit.Test
import java.lang.Exception
import kotlin.test.assertEquals

class TransformJsonTest {

    @Test
    fun `Json returns 200 response on successful transformation`() {
        val jobCode = "JobCode"
        val body = "Body"

        fun mockGetJsonOutputThrow(a: String, b: String): Response = Response(Status.OK).body("$a : $b")

        val expectedResponse = Response(Status.OK).body("$jobCode : $body")

        val contractRoute = transformJson(::mockGetJsonOutputThrow)

        val response = contractRoute(
            Request(Method.POST, "/transformJson/$jobCode").body(body)
        )

        assertEquals(expectedResponse, response)
    }

    @Test
    fun `Json returns 500 response on unsuccessful transformation`() {
        val exceptedBody = "Exception Message"
        fun mockGetJsonOutputThrow(a: String, b: String): Response = throw Exception(exceptedBody)

        val expectedResponse = Response(Status.INTERNAL_SERVER_ERROR).body(exceptedBody)

        val contractRoute = transformJson(::mockGetJsonOutputThrow)

        val response = contractRoute(Request(Method.POST, "/transformJson/jobCode"))

        assertEquals(expectedResponse, response)
    }
}