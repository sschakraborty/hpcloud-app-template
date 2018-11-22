package com.controller;

import com.facade.PersonFacade;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class PersonController extends AbstractController<PersonFacade> {
	public PersonController(Vertx vertx, Router router) {
		super(vertx, router, "/api/1/Person", PersonFacade.get(), new JsonObject()
				.put("title", "Person")
				.put("model", new JsonObject()
						.put("firstName", "")
						.put("middleName", "")
						.put("lastName", "")
						.encode())
				.put("fieldName", new JsonObject()
						.put("firstName", "First Name")
						.put("middleName", "Middle Name")
						.put("lastName", "Last Name")
						.encode())
				.put("postURL", "/api/1/Person"));
	}
}