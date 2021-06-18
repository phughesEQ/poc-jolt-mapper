package transform

import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.junit.Test
import transformation.jsonTo
import transformation.jsonToQuery
import transformation.jsonToXml
import kotlin.test.assertEquals


class TransformationsTest {
    private val json = """
            {
                "A": "B",
                "C": 1,
                "E": {
                    "F": "G"
                }
            }
        """.trimIndent()

    private val flatJson = """
            {
                "A": "B",
                "C": 1,
                "E": "F"
            }
        """.trimIndent()


    @Test
    fun `Converts Json to XML as expected`() {
        val expectedXml = """<A>B</A><C>1</C><E><F>G</F></E>"""

        val xml = jsonToXml(json)

        assertEquals(expectedXml, xml)
    }

    @Test
    fun `Converts Json to XML`() {
        val expectedQuery = "A=B&C=1&E=F"

        val query = jsonToQuery(flatJson)

        assertEquals(expectedQuery, query)
    }

    @Test
    fun `Json to XML returns correct response`() {
        val type = "xml"

        val expectedResponse = Response(OK)
            .header("Content-Type", "application/$type")
            .body("""<A>B</A><C>1</C><E><F>G</F></E>""")

        val actualResponse = jsonTo(type, json)

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `Json to Json returns correct response`() {
        val type = "json"

        val expectedResponse = Response(OK)
            .header("Content-Type", "application/$type")
            .body(json)

        val actualResponse = jsonTo(type, json)

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `Json to Query returns correct response`() {
        val type = "query"

        val expectedResponse = Response(OK)
            .body("A=B&C=1&E=F")

        val actualResponse = jsonTo(type, flatJson)

        assertEquals(expectedResponse, actualResponse)
    }

}