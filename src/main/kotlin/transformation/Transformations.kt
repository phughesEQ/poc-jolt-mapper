package transformation

import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.json.JSONObject
import org.json.XML
import types.ResponseType

fun jsonTo(type: String, json: String) = when (type.uppercase()) {
    ResponseType.XML.name -> Response(OK).header("Content-Type", "application/xml").body(jsonToXml(json))
    ResponseType.QUERY.name -> Response(OK).body(jsonToQuery(json))
    else -> Response(OK).header("Content-Type", "application/json").body(json)
}

fun jsonToXml(input: String): String = XML.toString(JSONObject(input))

// Note: not finished, just poc, would want a library to do this instead
fun jsonToQuery(input: String): String =
    JSONObject(input)
        .toMap()
        .map(::queryVariable)
        .joinToString("&")
        .replace(" ", "%")

fun queryVariable(entry: Map.Entry<String, Any>): String = "${entry.key}=${entry.value}"
