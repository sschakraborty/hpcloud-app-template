package com.verticle;

import org.hibernate.Session;

import com.App;
import com.entity.Person;

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
		router.route("/").handler(staticHandler);
		
		// Read portion of the code
		router.route(HttpMethod.GET, "/api/1/Person/read/:id").handler(context -> {
			context.response().putHeader("Content-type", "application/json");
			vertx.executeBlocking(future -> {
				Session session = App.getSession();
				Person person = session.find(Person.class, Long.parseLong(context.request().getParam("id")));
				session.close();
				future.complete(JsonObject.mapFrom(person).encodePrettily());
			}, result -> {
				context.response().end((String) result.result());
			});
		});
		
		// Delete portion of the code
		router.route(HttpMethod.GET, "/api/1/Person/delete/:id").handler(context -> {
			context.response().putHeader("Content-type", "application/json");
			vertx.executeBlocking(future -> {
				System.out.println(context.request().getParam("id"));
				Session session = App.getSession();
				Long id = Long.parseLong(context.request().getParam("id"));
				Person p = session.find(Person.class, id);
				
				session.beginTransaction();
				if(p != null) {
					// session.delete(p);
					session.remove(p);
				}
				session.getTransaction().commit();
				session.close();
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
			JsonObject body = context.getBodyAsJson();
			Person p = body.mapTo(Person.class);
			Session session = App.getSession();
			session.beginTransaction();
			session.save(p);
			session.getTransaction().commit();
			session.close();
			JsonObject resp = new JsonObject();
			resp.put("message", "successful");
			context.response().putHeader("Content-type", "application/json");
			context.response().end(resp.encodePrettily());
		});
		
		server.requestHandler(router::accept).listen(App.webPort());
	}
}