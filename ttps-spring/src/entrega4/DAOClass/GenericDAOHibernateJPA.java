package entrega4.DAOClass;


import java.util.List;

import javax.persistence.EntityManager;

import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import entrega4.DAOInterface.GenericDAO;



@Transactional
public class GenericDAOHibernateJPA<T> implements GenericDAO<T> {
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager entityManager;

	
protected Class<T> persistentClass;
public GenericDAOHibernateJPA(Class<T> clase) {
	setPersistentClass(clase); 
}
	
public Class<T> getPersistentClass() {
	return persistentClass;
}

public void setPersistentClass(Class<T> persistentClass) {
	this.persistentClass = persistentClass;
}



public T persistir(T entity) {
	this.getEntityManager().persist(entity);
	return entity;
}

public T actualizar(T entity) {
	
		T entity1 = this.getEntityManager().merge(entity);
		return entity1;
	
}


public void borrar(T entity) {
	T e =this.getEntityManager().merge(entity);
	this.getEntityManager().remove(e);
	
	
}

public List<T> recuperarTodos(String columnOrder) {
	
	
	 Query consulta=
			 this.getEntityManager().createQuery("select e from "+ getPersistentClass().getSimpleName()+" e order by e."+columnOrder);
	 List<T> resultado = (List<T>)consulta.getResultList();
	 return resultado;
	}

public T findById(int id) {
	
	
	 Query consulta=
			 this.getEntityManager().createQuery("select e from "+ getPersistentClass().getSimpleName()+" e where e.id="+id);
	 T resultado = (T)consulta.getSingleResult();
	 return resultado;
	}






}
