package com;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class App {
	private static PrintWriter out; 
	private static PrintWriter err;
	private static BufferedReader in;
	private static int webPort;
	private static SessionFactory sessionFactory;
	
	static {
		out = new PrintWriter(new OutputStreamWriter(System.out));
		err = new PrintWriter(new OutputStreamWriter(System.err));
		in = new BufferedReader(new InputStreamReader(System.in));
		webPort = 8080;
		
		StandardServiceRegistry serviceRegistry;
		serviceRegistry = new StandardServiceRegistryBuilder()
				.configure() // hibernate.cfg.xml parsed
				.build();    // returns value
		
		sessionFactory = new MetadataSources(serviceRegistry)
				.buildMetadata().buildSessionFactory();
	}
	
	public static PrintWriter out() {
		return out;
	}
	
	public static PrintWriter err() {
		return err;
	}
	
	public static BufferedReader in() {
		return in;
	}
	
	public static int webPort() {
		return webPort;
	}
	
	public static Session getSession() {
		return sessionFactory.openSession();
	}
	
	public static StatelessSession getStatelessSession() {
		return sessionFactory.openStatelessSession();
	}
}
