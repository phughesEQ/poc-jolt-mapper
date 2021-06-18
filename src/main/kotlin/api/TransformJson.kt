package api

import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.*
import org.http4k.core.ContentType.Companion.TEXT_PLAIN
import org.http4k.core.Method.POST
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.*

fun transformJson(getJsonTransformation: (String) -> Response): ContractRoute {
    val input: BiDiBodyLens<String> = Body.string(TEXT_PLAIN).toLens()

    fun response(): HttpHandler = { request ->
        try {
            getJsonTransformation(input(request))
        } catch (exception: Exception) {
            val body = exception.message ?: "An unknown exception has occurred"
            Response(INTERNAL_SERVER_ERROR).body(body)
        }
    }

    return "/transformJson" meta {
        description = "Transform input json to match spec"
        returning(OK to "A transformed output")
        returning(INTERNAL_SERVER_ERROR to "Exception message")
    } bindContract POST to response()
}
