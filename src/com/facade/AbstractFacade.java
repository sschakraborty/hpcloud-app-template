package com.facade;

import org.hibernate.Session;

import com.App;

public abstract class AbstractFacade<T> {
	private Class<T> entityClass;
	
	public AbstractFacade(Class<T> object) {
		entityClass = object;
	}
	
	public T find(Long id) {
		try {
			Session session = App.getSession();
			T object = session.find(entityClass, id);
			session.close();
			return object;
		} catch(Exception e) {
			App.err().print("Error in AbstractFacade: ");
			App.err().println(e.getMessage());
			App.err().flush();
		}
		return null;
	}
	
	public void delete(Long id) {
		try {
			Session session = App.getSession();
			T object = session.find(entityClass, id);
			if(object != null) {
				// session.delete(object);
				session.beginTransaction();
				session.remove(object);
				session.getTransaction().commit();
			}
			session.close();
		} catch(Exception e) {
			App.err().print("Error in AbstractFacade: ");
			App.err().println(e.getMessage());
			App.err().flush();
		}
	}
	
	public void delete(T object) {
		try {
			if(object != null) {
				// session.delete(object);
				Session session = App.getSession();
				session.beginTransaction();
				session.remove(object);
				session.getTransaction().commit();
				session.close();
			}
		} catch(Exception e) {
			App.err().print("Error in AbstractFacade: ");
			App.err().println(e.getMessage());
			App.err().flush();
		}
	}
	
	public void create(T object) {
		try {
			if(object != null) {
				Session session = App.getSession();
				session.beginTransaction();
				session.save(object);
				session.getTransaction().commit();
				session.close();
			}
		} catch(Exception e) {
			App.err().print("Error in AbstractFacade: ");
			App.err().println(e.getMessage());
			App.err().flush();
		}
	}
	
	public void update(T object) {
		try {
			if(object != null) {
				Session session = App.getSession();
				session.beginTransaction();
				session.merge(object);
				session.getTransaction().commit();
				session.close();
			}
		} catch(Exception e) {
			App.err().print("Error in AbstractFacade: ");
			App.err().println(e.getMessage());
			App.err().flush();
		}
	}
}
