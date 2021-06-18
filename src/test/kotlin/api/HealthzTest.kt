package api

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.Test
import kotlin.test.assertEquals

class HealthzTest {
    private val contractRoute = healthz()

    @Test
    fun `Healthz endpoint returns 200 response`() {
        val expectedStatus = Status.OK

        val response = contractRoute(Request(Method.GET, "/healthz"))

        assertEquals(expectedStatus, response.status)
    }

    @Test
    fun `Healthz endpoint returns expected body in response`() {
        val expectedBody = "Status Okay"

        val response = contractRoute(Request(Method.GET, "/healthz"))

        assertEquals(expectedBody, response.bodyString())
    }
}