package entrega4.DAOClass;


import org.springframework.stereotype.Repository;

import entrega4.DAOInterface.ValoracionDAO;
import entrega4.ModelClasses.Valoracion;

@Repository
public class ValoracionDAOHibernateJPA extends GenericDAOHibernateJPA<Valoracion> implements ValoracionDAO  {

	public ValoracionDAOHibernateJPA() {
		super(Valoracion.class);
	}

}
