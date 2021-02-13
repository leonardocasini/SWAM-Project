package it.unifi.dinfo.stlab.dogs_breeds_backend.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

public abstract class GenericDao<T> {
	
	@PersistenceContext(unitName="dogs-breeds-backend")
	protected EntityManager em;
	
	protected Class<T> genericClass;
	
	public void setClass(Class<T> myClass) {
		this.genericClass = myClass;
	}

	public boolean existsId(Long id ) {
		return em.find(genericClass, id) != null;
	}

	public boolean exist(Object entity ) {
		List<T> result = em.createQuery(" FROM "+genericClass.getName(), genericClass)
				.getResultList();
		return result.contains( entity );
	}
	
	public T findById(Long id){
        return (T) this.em.createQuery("SELECT t FROM " + genericClass.getTypeName() + " t WHERE t.id = :id")
                .setParameter("id",id).getSingleResult();
    }
	
	public T findByName(String name){
        return (T) this.em.createQuery("SELECT t FROM " + genericClass.getTypeName() + " t WHERE t.name = :name")
                .setParameter("name",name).getSingleResult();
    }
	
	public List<T> findAll() {
        return this.em.createQuery("SELECT t FROM " + genericClass.getTypeName() + " t").getResultList();
    }
	
	@Transactional
	public void persist(T object) {
		em.persist(object);
	}
	
	@Transactional
	public void update(T object) {
		em.merge(object);
	}
	
	@Transactional
    public void delete(Long id){
        this.em.remove(findById(id));
    }
	
	public int numberOfEntities(){
		return this.em.createQuery("count(id) from " + genericClass.getName(), genericClass).getFirstResult();
	}

}
