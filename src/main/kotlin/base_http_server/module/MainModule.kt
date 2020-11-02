package base_http_server.module

import com.google.inject.AbstractModule

class MainModule: AbstractModule() {

    override fun configure() {
        install(VertxModule())
        install(HealthServiceModule())
    }

}