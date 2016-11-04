package com.itpro.restws.email.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDaoEmail<PK extends Serializable, T> {
	
	private final Class<T> persistentClass;
	
	@SuppressWarnings("unchecked")
	public AbstractDaoEmail(){
		this.persistentClass =(Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}
	
	@Autowired
	private SessionFactory email_sessionFactory;

	protected Session getSession(){
		return email_sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public T getByKey(PK key) {
		return (T) getSession().get(persistentClass, key);
	}
/***
 * Persist As mentioned above, this is used on transient objects. It does not return the generated ID.
 * @param entity
 */
	public void persist(T entity) {
		getSession().persist(entity);
		
	/**
	 * Save Persists an entity. 
	 * Will assign an identifier if one doesn't exist. If one does, it's essentially doing an update. Returns the generated ID of the entity.	
	 */
	}
	public Integer save(T entity) {
		return (Integer) getSession().save(entity);
		
	}

	public void update(T entity) {
		getSession().update(entity);
		
	}
	public void merge(T entity) {
		getSession().merge(entity);
		
	}
	
	public void delete(T entity) {
		getSession().delete(entity);
	}
	
	protected Criteria createEntityCriteria(){
		return getSession().createCriteria(persistentClass);
	}
	public void setFlushMode(FlushMode mode){
		getSession().setFlushMode(mode);
		
	}
	public void clear(){
		getSession().clear();
		
	}

}
