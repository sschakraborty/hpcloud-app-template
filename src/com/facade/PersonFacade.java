package com.facade;

import com.entity.Person;

public class PersonFacade extends AbstractFacade<Person> {
	private static PersonFacade personFacade;
	
	private PersonFacade() {
		super(Person.class);
	}
	
	static {
		personFacade = new PersonFacade();
	}
	
	public static PersonFacade get() {
		return personFacade;
	}
}
