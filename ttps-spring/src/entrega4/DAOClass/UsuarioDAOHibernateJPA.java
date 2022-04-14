package entrega4.DAOClass;






import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import entrega4.DAOInterface.UsuarioDAO;
import entrega4.ModelClasses.Usuario;

@Repository
public class UsuarioDAOHibernateJPA extends GenericDAOHibernateJPA<Usuario> implements UsuarioDAO  {

	public UsuarioDAOHibernateJPA() {
		super(Usuario.class);
	}
	
	
	public Usuario findByUsernameAndPassword(String username,String password) {
		System.out.println("entramos al find by username y password");
	
	 Query consulta=
			 this.getEntityManager().createQuery("select e from "+ getPersistentClass().getSimpleName()+"e where e.nombre_usuario="+username+" and e.contraseña="+password);
	 Usuario resultado = (Usuario)consulta.getResultList();
	
	 return resultado;
	}
}
