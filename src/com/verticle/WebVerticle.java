package com.verticle;

import com.App;
import com.controller.PersonController;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class WebVerticle extends AbstractVerticle {
	@Override
	public void start() {
		HttpServer server = vertx.createHttpServer();
		
		Router router = Router.router(vertx);
		
		// Register the essential static content handler
		// and the body parser and handler
		StaticHandler staticHandler = StaticHandler.create("./static");
		staticHandler.setCachingEnabled(false);
		
		router.route().handler(BodyHandler.create());
		router.route("/*").handler(staticHandler);
		
		// Register controllers
		new PersonController(vertx, router);
		
		server.requestHandler(router::accept).listen(App.webPort());
	}
}