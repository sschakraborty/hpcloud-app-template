package com.verticle;

import com.App;

import io.vertx.core.AbstractVerticle;

public class InitVerticle extends AbstractVerticle {
	@Override
	public void start() {
		App.out().print("Vertx description: ");
		App.out().println(vertx.toString());
		App.out().print("Is clustered: ");
		App.out().println(vertx.isClustered());
		App.out().flush();
	}
}
