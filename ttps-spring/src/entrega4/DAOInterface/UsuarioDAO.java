package entrega4.DAOInterface;



import entrega4.ModelClasses.Usuario;

public interface UsuarioDAO extends GenericDAO<Usuario> {
	
	 public Usuario findByUsernameAndPassword(String username,String password);

}
