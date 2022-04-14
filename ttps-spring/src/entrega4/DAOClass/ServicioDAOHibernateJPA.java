package entrega4.DAOClass;






import java.util.List;

import javax.persistence.Query;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import entrega4.DAOInterface.ServicioDAO;
import entrega4.ModelClasses.Servicio;
import entrega4.Others.EMF;


@Repository
public  class ServicioDAOHibernateJPA extends GenericDAOHibernateJPA<Servicio> implements ServicioDAO  {

	public ServicioDAOHibernateJPA() {
		super(Servicio.class);
	}
	
	
		

}



