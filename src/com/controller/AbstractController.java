package com.controller;

import com.App;
import com.facade.AbstractFacade;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;

public class AbstractController<T extends AbstractFacade> {
	public AbstractController(Vertx vertx, Router router, String url, T facade, JsonObject tempParam) {
		TemplateEngine engine = FreeMarkerTemplateEngine.create().setExtension("html");
		router.route(HttpMethod.GET, (url + "/create")).handler(context -> {
			context.put("param", tempParam);
			engine.render(context, "./template/", "createView", result -> {
				if(result.succeeded()) {
					context.response().end(result.result());
				} else {
					App.err().println(result.cause());
					App.out().flush();
				}
			});
		});
		
		router.route(HttpMethod.GET, (url + "/:id")).handler(context -> {
			vertx.executeBlocking(future -> {
				try {
					Long id = Long.parseLong(context.request().getParam("id"));
					Object obj = facade.find(id);
					JsonObject object;
					if(obj == null) {
						object = new JsonObject();
						object.put("message", "failure");
					} else {
						object = JsonObject.mapFrom(obj);
					}
					future.complete(object.encodePrettily());
				} catch(Exception e) {
					// Do nothing
				}
			}, result -> {
				context.response().putHeader("Content-type", "application/json");
				context.response().end((String) result.result());
			});
		});
		
		router.route(HttpMethod.DELETE, (url + "/:id")).handler(context -> {
			vertx.executeBlocking(future -> {
				try {
					Long id = Long.parseLong(context.request().getParam("id"));
					facade.delete(id);
					future.complete();
				} catch(Exception e) {
					// Do nothing
				}
			}, result -> {
				context.response().putHeader("Content-type", "application/json");
				JsonObject object = new JsonObject();
				if(result.succeeded()) {
					object.put("message", "success");
				} else {
					object.put("message", "failure");
				}
				context.response().end(object.encodePrettily());
			});
		});
		
		router.route(HttpMethod.POST, url).handler(context -> {
			vertx.executeBlocking(future -> {
				try {
					JsonObject body = context.getBodyAsJson();
					Object p = body.mapTo(facade.getEntityClass());
					facade.create(p);
					future.complete();
				} catch(Exception e) {
					// Do nothing
				}
			}, result -> {
				context.response().putHeader("Content-type", "application/json");
				JsonObject object = new JsonObject();
				if(result.succeeded()) {
					object.put("message", "success");
				} else {
					object.put("message", "failure");
				}
				context.response().end(object.encodePrettily());
			});
		});
		
		router.route(HttpMethod.PUT, url).handler(context -> {
			vertx.executeBlocking(future -> {
				JsonObject body = context.getBodyAsJson();
				Object p = body.mapTo(facade.getEntityClass());
				facade.update(p);
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
	}	
}
