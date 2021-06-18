package api

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.junit.Test
import java.lang.Exception
import kotlin.test.assertEquals

class CreateSpecFileTest {

    @Test
    fun `Successful creation of a file returns 200 response`() {
        fun mockWriteFile(a: String, b: String): Unit = Unit

        val expectedResponse = Response(Status.OK).body("File Created")

        val contractRoute = createSpecFile(::mockWriteFile)

        val response = contractRoute(Request(Method.POST, "/createSpecFile/abc"))

        assertEquals(expectedResponse, response)
    }

    @Test
    fun `File name and body are passed as expected`() {
        val expectedFileName = "Test"
        val expectedBody = """{"A": "B"}"""

        fun mockWriteFile(fileName: String, body: String): Unit {
            assertEquals(expectedFileName, fileName)
            assertEquals(expectedBody, body)
        }

        createSpecFile(::mockWriteFile)(
            Request(Method.POST, "/createSpecFile/$expectedFileName").body(expectedBody)
        )
    }

    @Test
    fun `Unsuccessful creation of a file returns 500 response`() {
        fun mockWriteFileThrow(a: String, b: String): Unit = throw Exception("Exception Message")

        val expectedResponse = Response(Status.INTERNAL_SERVER_ERROR).body("Unable to create file")

        val contractRoute = createSpecFile(::mockWriteFileThrow)

        val response = contractRoute(Request(Method.POST, "/createSpecFile/abc"))

        assertEquals(expectedResponse, response)
    }
}