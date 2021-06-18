package api

import fileIO.writeSpec
import org.http4k.contract.contract
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import transformation.getJsonTransformation

fun api(): RoutingHttpHandler = "/api" bind routes(
    contract {
        descriptionPath = "/api-docs"
        routes += transformJson(::getJsonTransformation)
        routes += createSpecFile(::writeSpec)
        routes += healthz()
    }
)

