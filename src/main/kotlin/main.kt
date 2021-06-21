import api.api
import org.http4k.filter.CorsPolicy
import org.http4k.filter.ServerFilters
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.routing.routes


fun main() {
    routes(api())
        .withFilter(ServerFilters.Cors(CorsPolicy.UnsafeGlobalPermissive))
        .asServer(Undertow(9000))
        .start()
}