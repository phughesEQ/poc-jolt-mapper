package transform

import com.bazaarvoice.jolt.JsonUtils
import org.http4k.core.Status
import org.http4k.routing.header
import org.junit.Test
import transformation.getJsonTransformation
import transformation.getSchemaFrom
import transformation.transformJson
import types.Schema
import java.io.File
import kotlin.test.assertEquals

class JoltTransformationsTest {
    private val input = File("src/test/resources/Input.json").readText()

    @Test
    fun `Schema should read correct file from job code`() {
        val specName = "TestSpec"
        val expectedResponseType = "json"

        val expectedSpec = JsonUtils.classpathToList("/$specName.json").toString()
        val schema: Schema = getSchemaFrom("""{"job_code": "$specName"}""")

        assertEquals(expectedSpec, schema.spec.toString())
        assertEquals(expectedResponseType, schema.responseType)

    }

    @Test
    fun `Input is correctly mapped when shift is applied`() {
        val spec: List<Any> = JsonUtils.classpathToList("/ShiftTestSpec.json")

        val expectedOutput = """
            {
              "lead" : {
                "contact" : {
                  "addressLine1" : "123 adga",
                  "city" : "Schenectady",
                  "state" : "NY"
                }
              }
            }
        """.trimIndent()

        val output = transformJson(input, spec)

        assertEquals(expectedOutput, output)
    }

    @Test
    fun `Input is correctly mapped when concat is applied`() {
        val spec: List<Any> = JsonUtils.classpathToList("/ConcatTestSpec.json")

        val expectedOutput = """
            {
              "lead" : {
                "contact" : {
                  "fullName" : "Homer Simpson"
                }
              }
            }
        """.trimIndent()

        val output = transformJson(input, spec)

        assertEquals(expectedOutput, output)
    }

    @Test
    fun `Input is correctly mapped when boolean operation is applied`() {
        val spec: List<Any> = JsonUtils.classpathToList("/OperationTestSpec.json")

        val expectedOutput = """
            {
              "lead" : {
                "contact" : {
                  "occupation" : "Surgeon"
                }
              }
            }
        """.trimIndent()

        val output = transformJson(input, spec)

        assertEquals(expectedOutput, output)
    }

    @Test
    fun `Input is returned as correct response when transformed`() {
        val expectedStatus = Status.OK
        val expectedHeader = "application/json"
        val expectedOutput = """
            {
              "lead" : {
                "contact" : {
                  "addressLine1" : "123 adga",
                  "city" : "Schenectady",
                  "fullName" : "Homer Simpson",
                  "occupation" : "Surgeon",
                  "state" : "NY"
                }
              }
            }
        """.trimIndent()

        val response = getJsonTransformation(input)

        assertEquals(expectedStatus, response.status)
        assertEquals(expectedHeader, response.header("Content-Type"))
        assertEquals(expectedOutput, response.bodyString())
    }

}