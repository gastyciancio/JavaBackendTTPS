package entrega4.DAOClass;


import org.springframework.stereotype.Repository;

import entrega4.DAOInterface.EstadoDAO;
import entrega4.ModelClasses.Estado;

@Repository
public class EstadoDAOHibernateJPA extends GenericDAOHibernateJPA<Estado> implements EstadoDAO  {

	public EstadoDAOHibernateJPA() {
		super(Estado.class);
	}

}
