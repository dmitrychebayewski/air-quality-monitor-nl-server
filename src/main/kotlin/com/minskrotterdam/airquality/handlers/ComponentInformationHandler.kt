package com.minskrotterdam.airquality.handlers

import com.minskrotterdam.airquality.extensions.safeLaunch
import com.minskrotterdam.airquality.services.ComponentInformationService
import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.http.endAwait
import io.vertx.kotlin.core.http.writeAwait
import ru.gildor.coroutines.retrofit.await

class ComponentInformationHandler {

    suspend fun pollutantComponentInfoHandler(ctx: RoutingContext) {
        safeLaunch(ctx) {
            ctx.response().headers().set("Content-Type", "application/json")
            getPollutantInfo(ctx)
        }
    }

    suspend fun pollutantComponentLimitHandler(ctx: RoutingContext) {
        safeLaunch(ctx) {
            ctx.response().headers().set("Content-Type", "application/json")
            getUpperPollutantLimit(ctx)
        }
    }

    private suspend fun getPollutantInfo(ctx: RoutingContext) {
        val response = ctx.response()
        response.isChunked = true
        val formula = ctx.pathParam("formula")
        val pollutantInfo = ComponentInformationService().getPollutantInfo(formula).await()

        response.writeAwait(Json.encode(pollutantInfo))
        if (!response.ended()) {
            response.endAwait()
        }

    }

    private suspend fun getUpperPollutantLimit(ctx: RoutingContext) {
        val response = ctx.response()
        response.isChunked = true
        val formula = ctx.pathParam("formula")
        val pollutantInfo = ComponentInformationService().getPollutantInfo(formula).await()

        response.writeAwait(Json.encode(pollutantInfo.data.limits.last()))
        if (!response.ended()) {
            response.endAwait()
        }

    }
}
