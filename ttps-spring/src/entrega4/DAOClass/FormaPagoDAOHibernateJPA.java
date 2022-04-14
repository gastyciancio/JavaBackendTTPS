package entrega4.DAOClass;


import org.springframework.stereotype.Repository;

import entrega4.DAOInterface.FormaPagoDAO;
import entrega4.ModelClasses.FormaPago;

@Repository
public class FormaPagoDAOHibernateJPA extends GenericDAOHibernateJPA<FormaPago> implements FormaPagoDAO  {

	public FormaPagoDAOHibernateJPA() {
		super(FormaPago.class);
	}

}

