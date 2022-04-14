package entrega4.DAOClass;



import org.springframework.stereotype.Repository;

import entrega4.DAOInterface.ServicioDAO;
import entrega4.DAOInterface.UsuarioDAO;

@Repository
public class DaoFactory {
	public static UsuarioDAO getUsuarioDAO() {
		return new UsuarioDAOHibernateJPA();
	}
	public static ServicioDAO getServicioDAO(){
		return new ServicioDAOHibernateJPA();
	}
} 
