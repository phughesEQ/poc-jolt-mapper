package transform

import com.bazaarvoice.jolt.JsonUtils
import org.junit.Test
import transformation.transformJson
import kotlin.test.assertEquals

/*
* This class is a proof of concept over some of the more difficult JSON mapping's within the pong project. For
* understanding the input and output have been shown in full in code to make for easier understanding of the mappings.
*
 */
class ComplexSchemaTests {
    private val basePath = "/complexSchemas"

    // Link: https://github.com/adharmonics/pong/blob/d1b0b970230db7f68597ac73a72b5502c42200fd/lib/driver_auto_matcher.rb#L21
    @Test
    fun `Set Reference Indices - Input is correctly mapped when indexing of arrays is applied`() {
        val spec: List<Any> = JsonUtils.classpathToList("$basePath/IndexArrayTestSpec.json")

        val input = """
            {
              "drivers": [{
                  "first_name": "John"
                },
                {
                  "first_name": "David"
              }],
              "autos": [{
                  "make": "Ford"
                },
                {
                  "make": "Tesla"
              }]
            }
        """.trimIndent()

        val expectedOutput = """
            {
              "autos" : [ {
                "ref_index" : 1,
                "make" : "Ford"
              }, {
                "ref_index" : 2,
                "make" : "Tesla"
              } ],
              "drivers" : [ {
                "ref_index" : 1,
                "first_name" : "John"
              }, {
                "ref_index" : 2,
                "first_name" : "David"
              } ]
            }
        """.trimIndent()

        val output = transformJson(input, spec)

        assertEquals(expectedOutput, output)
    }

    // Link: https://github.com/adharmonics/pong/blob/d1b0b970230db7f68597ac73a72b5502c42200fd/lib/driver_auto_matcher.rb#L31
    @Test
    fun `Link driver and car - Input is correctly mapped when concat of arrays is applied`() {
        val spec: List<Any> = JsonUtils.classpathToList("$basePath/ConcatArraysByIdTestSpec.json")

        val input = """
            {
              "drivers": [
                {
                  "ordinal": "2",
                  "ref_index": 111,
                  "first_name": "John"
                },
                {
                  "ordinal": "1",
                  "ref_index": 222,
                  "first_name": "David"
                }
              ],
              "autos": [
                {
                  "driver_ordinal": "1",
                  "ref_index": 333,
                  "make": "Ford"
                },
                {
                  "driver_ordinal": "2",
                  "ref_index": 444,
                  "make": "Tesla"
                }
              ]
            }
        """.trimIndent()

        val expectedOutput = """
            {
              "drivers" : [ {
                "ordinal" : "2",
                "ref_index" : 111,
                "auto_index" : 444,
                "firstName" : "John"
              }, {
                "ordinal" : "1",
                "ref_index" : 222,
                "auto_index" : 333,
                "firstName" : "David"
              } ],
              "autos" : [ {
                "ordinal" : "2",
                "driver_index" : 111,
                "ref_index" : 444,
                "make" : "Tesla"
              }, {
                "ordinal" : "1",
                "driver_index" : 222,
                "ref_index" : 333,
                "make" : "Ford"
              } ]
            }
        """.trimIndent()

        val output = transformJson(input, spec)

        assertEquals(expectedOutput, output)
    }


    // Link: https://github.com/adharmonics/pong/blob/d1b0b970230db7f68597ac73a72b5502c42200fd/lib/driver_auto_matcher.rb#L44
    @Test
    fun `Link driver and car - Input is correctly mapped when conditional is applied`() {
        val spec: List<Any> = JsonUtils.classpathToList("$basePath/ConditionalTestSpec.json")

        val input = """
            {
              "drivers": [
                {
                  "ordinal": "2",
                  "ref_index": 111,
                  "first_name": "John"
                },
                {
                  "ordinal": "1",
                  "ref_index": 222,
                  "first_name": "David"
                }
              ],
              "autos": [
                {
                  "driver_ordinal": "1",
                  "ref_index": 333,
                  "make": "Ford"
                },
                {
                  "driver_ordinal": "2",
                  "ref_index": 444,
                  "make": "Tesla",
                  "autoType": "Primary"
                }
              ]
            }
        """.trimIndent()

        val expectedOutput = """
          {
            "drivers" : [ {
              "ordinal" : "2",
              "ref_index" : 111,
              "firstName" : "John"
            }, {
              "ordinal" : "1",
              "ref_index" : 222,
              "firstName" : "David"
            } ],
            "autos" : [ {
              "ordinal" : "2",
              "make" : "Tesla",
              "driver_ref" : 111
            }, {
              "ordinal" : "1",
              "make" : "Ford"
            } ]
          }
        """.trimIndent()

        val output = transformJson(input, spec)

        assertEquals(expectedOutput, output)
    }

    // Link: https://github.com/adharmonics/pong/blob/d1b0b970230db7f68597ac73a72b5502c42200fd/lib/driver_auto_matcher.rb#L89
    @Test
    fun `Identify Primary Driver - Input is correctly mapped when filter array is applied`() {
        val spec: List<Any> = JsonUtils.classpathToList("$basePath/FilterArrayTestSpec.json")

        val input = """
           {
              "drivers": [
                {
                  "ordinal": "1",
                  "ref_index": 111,
                  "first_name": "John"
                },
                {
                  "ordinal": "2",
                  "ref_index": 222,
                  "first_name": "David",
                  "type": "Secondary"
                }
              ],
              "autos": [
                {
                  "driver_ordinal": "2",
                  "ref_index": 333,
                  "make": "Ford",
                  "type": "Primary"
                },
                {
                  "driver_ordinal": "1",
                  "ref_index": 444,
                  "make": "Tesla"
                }
              ]
            }
        """.trimIndent()

        val expectedOutput = """
          {
            "primaryDriver" : {
              "driver_ordinal" : "2",
              "ref_index" : 333,
              "make" : "Ford",
              "type" : "Primary"
            }
          }
        """.trimIndent()

        val output = transformJson(input, spec)

        assertEquals(expectedOutput, output)
    }

    // Link: https://github.com/adharmonics/pong/blob/d1b0b970230db7f68597ac73a72b5502c42200fd/lib/driver_auto_matcher.rb#L97
    @Test
    fun `Identify Primary Driver when primary type is not set - Input is correctly mapped when filter array is applied`() {
        val spec: List<Any> = JsonUtils.classpathToList("$basePath/FilterArrayTestSpec.json")

        val input = """
           {
              "drivers": [
                {
                  "ordinal": "1",
                  "ref_index": 111,
                  "first_name": "John"
                },
                {
                  "ordinal": "2",
                  "ref_index": 222,
                  "first_name": "David",
                  "type": "Secondary"
                }
              ],
              "autos": [
                {
                  "driver_ordinal": "2",
                  "ref_index": 333,
                  "make": "Ford"
                },
                {
                  "driver_ordinal": "1",
                  "ref_index": 444,
                  "make": "Tesla"
                }
              ]
            }
        """.trimIndent()

        val expectedOutput = """
          {
            "primaryDriver" : {
              "ordinal" : "1",
              "ref_index" : 111,
              "first_name" : "John"
            }
          }
        """.trimIndent()

        val output = transformJson(input, spec)

        assertEquals(expectedOutput, output)
    }

}