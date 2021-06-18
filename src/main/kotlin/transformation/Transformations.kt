package transformation

import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.json.JSONObject
import org.json.XML

fun jsonTo(type: String, json: String) = when (type) {
    "xml" -> Response(OK).header("Content-Type", "application/xml").body(jsonToXml(json))
    "query" -> Response(OK).body(jsonToQuery(json))
    else -> Response(OK).header("Content-Type", "application/json").body(json)
}

fun jsonToXml(input: String): String = XML.toString(JSONObject(input))

// TODO: not finished, just poc
fun jsonToQuery(input: String): String =
    JSONObject(input)
        .toMap()
        .map(::queryVariable)
        .joinToString("&")
        .replace(" ", "%")

// TODO: handle if value is an object
fun queryVariable(entry: Map.Entry<String, Any>): String = "${entry.key}=${entry.value}"
