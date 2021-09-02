package transformation

import com.bazaarvoice.jolt.Chainr
import com.bazaarvoice.jolt.JsonUtils
import org.http4k.core.Response
import org.json.JSONObject
import types.ResponseType
import types.Schema

fun getJsonTransformation(jobCode: String, input: String): Response {
    val schema: Schema = getSchemaFrom(jobCode)

    val json: String = transformJson(input, schema.spec)

    return jsonTo(schema.responseType, json)
}

// * These would have catchable exceptions
// * Would become a S3 bucket read instead of file class
fun getSchemaFrom(jobCode: String): Schema {
    val spec: List<Any> = JsonUtils.classpathToList("/$jobCode.json")

    val responseType: String = JSONObject(JsonUtils.toPrettyJsonString(spec[0])).getString("responseType")
        ?: ResponseType.JSON.name

    return Schema(spec, responseType)
}

fun transformJson(input: String, spec: List<Any>): String {
    val chain: Chainr = Chainr.fromSpec(spec)

    val json = chain.transform(JsonUtils.jsonToObject(input))

    return JsonUtils.toPrettyJsonString(json)
}
