package entrega4.DAOClass;


import org.springframework.stereotype.Repository;

import entrega4.DAOInterface.ReservaDAO;
import entrega4.ModelClasses.Reserva;

@Repository
public class ReservaDAOHibernateJPA extends GenericDAOHibernateJPA<Reserva> implements ReservaDAO  {

	public ReservaDAOHibernateJPA() {
		super(Reserva.class);
	}

}

