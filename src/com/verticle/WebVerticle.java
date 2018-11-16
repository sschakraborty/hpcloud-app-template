package com.verticle;

import com.App;
import com.entity.Person;
import com.facade.PersonFacade;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
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
		
		// Read portion of the code
		router.route(HttpMethod.GET, "/api/1/Person/:id").handler(context -> {
			context.response().putHeader("Content-type", "application/json");
			vertx.executeBlocking(future -> {
				Long id = Long.parseLong(context.request().getParam("id"));
				Person person = PersonFacade.get().find(id);
				future.complete(JsonObject.mapFrom(person).encodePrettily());
			}, result -> {
				context.response().end((String) result.result());
			});
		});
		
		// Delete portion of the code
		router.route(HttpMethod.DELETE, "/api/1/Person/:id").handler(context -> {
			context.response().putHeader("Content-type", "application/json");
			vertx.executeBlocking(future -> {
				Long id = Long.parseLong(context.request().getParam("id"));
				PersonFacade.get().delete(id);
				future.complete();
			}, result -> {
				JsonObject object = new JsonObject();
				if(result.succeeded()) {
					object.put("message", "success");
				} else {
					object.put("message", "internal error");
				}
				context.response().end(object.encodePrettily());
			});
		});
		
		router.route(HttpMethod.POST, "/api/1/Person").handler(context -> {
			vertx.executeBlocking(future -> {
				JsonObject body = context.getBodyAsJson();
				Person p = body.mapTo(Person.class);
				PersonFacade.get().create(p);
				future.complete();
			}, result -> {
				JsonObject resp = new JsonObject();
				if(result.succeeded()) {
					resp.put("message", "successful");
				} else {
					resp.put("message", "failure");
				}
				context.response().putHeader("Content-type", "application/json");
				context.response().end(resp.encodePrettily());
			});
		});
		
		router.route(HttpMethod.PUT, "/api/1/Person").handler(context -> {
			vertx.executeBlocking(future -> {
				JsonObject body = context.getBodyAsJson();
				Person p = body.mapTo(Person.class);
				PersonFacade.get().update(p);
				future.complete();
			}, result -> {
				JsonObject resp = new JsonObject();
				if(result.succeeded()) {
					resp.put("message", "successful");
				} else {
					resp.put("message", "failure");
				}
				context.response().putHeader("Content-type", "application/json");
				context.response().end(resp.encodePrettily());
			});
		});
		
		server.requestHandler(router::accept).listen(App.webPort());
	}
}