package api

import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status

fun healthz(): ContractRoute {

    fun response(): HttpHandler = {
        Response(Status.OK).body("Status Okay")
    }

    return "/healthz" meta {
        description = "Health Check"
        returning(Status.OK to "Status Okay")
    } bindContract Method.GET to response()
}