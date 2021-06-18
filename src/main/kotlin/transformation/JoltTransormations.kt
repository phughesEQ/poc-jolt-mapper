package transformation

import com.bazaarvoice.jolt.Chainr
import com.bazaarvoice.jolt.JsonUtils
import org.http4k.core.Response
import org.json.JSONObject
import types.Schema

fun getJsonTransformation(input: String): Response {
    val schema: Schema = getSchemaFrom(input)

    val json: String = transformJson(input, schema.spec)

    return jsonTo(schema.responseType, json)
}

//TODO: These would have catchable exceptions
fun getSchemaFrom(input: String): Schema {
    val specName: String = JSONObject(input).getString("job_code") ?: ""

    val spec: List<Any> = JsonUtils.classpathToList("/$specName.json") //TODO: This would become an S3 bucket read

    val responseType: String = JSONObject(JsonUtils.toPrettyJsonString(spec[0])).getString("responseType") ?: ""

    return Schema(spec, responseType)
}

fun transformJson(input: String, spec: List<Any>): String {
    val chain: Chainr = Chainr.fromSpec(spec)

    val json = chain.transform(JsonUtils.jsonToObject(input))

    return JsonUtils.toPrettyJsonString(json)
}
