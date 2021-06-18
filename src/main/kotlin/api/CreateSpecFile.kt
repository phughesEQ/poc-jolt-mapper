package api

import org.http4k.contract.ContractRoute
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.*
import org.http4k.core.ContentType.Companion.TEXT_PLAIN
import org.http4k.core.Method.POST
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.*
import java.lang.Exception

fun createSpecFile(writeFile: (String, String) -> Unit): ContractRoute {
    val body: BiDiBodyLens<String> = Body.string(TEXT_PLAIN).toLens()

    fun response(specName: String): HttpHandler = { request ->
        try {
            writeFile(specName, body(request))
            Response(OK).body("File Created")
        } catch (e: Exception) {
            Response(INTERNAL_SERVER_ERROR).body("Unable to create file")
        }
    }

    return "/createSpecFile" / Path.of("specName", "Name of the spec file") meta {
        description = "Creates a new spec file"
        returning(OK to "File Created")
        returning(INTERNAL_SERVER_ERROR to "Unable to create file")
    } bindContract POST to ::response
}