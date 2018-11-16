package com;

import com.verticle.InitVerticle;
import com.verticle.WebVerticle;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class Main {
	public static void main(String[] args) {
		try {
			VertxOptions options = new VertxOptions();
			options.setBlockedThreadCheckInterval(1000*60*60);
			
			Vertx vertx = Vertx.vertx(options);
			
			vertx.deployVerticle(new InitVerticle(), result -> {
				if(result.succeeded()) {
					// Deployment of InitVerticle succeeded
					App.out().println("com.verticle.InitVerticle deployed");
					App.out().flush();
					vertx.deployVerticle(new WebVerticle(), result2 -> {
						if(result2.succeeded()) {
							App.out().println("com.verticle.WebVerticle deployed");
							App.out().flush();
						} else {
							vertx.close();
						}
					});
				} else {
					vertx.close();
				}
			});
		} catch(Exception e) {
			App.err().print("Exception in ");
			App.err().print("com.Main: ");
			App.err().println(e.getMessage());
			App.err().flush();
		}
	}
}
