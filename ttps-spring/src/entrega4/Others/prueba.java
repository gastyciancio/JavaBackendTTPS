package entrega4.Others;

import java.util.List;

import entrega4.DAOClass.DaoFactory;
import entrega4.DAOClass.ServicioDAOHibernateJPA;
import entrega4.DAOClass.UsuarioDAOHibernateJPA;
import entrega4.ModelClasses.Servicio;
import entrega4.ModelClasses.Usuario;

public class prueba {

	public static void main(String[] args) {
		
		//crud de usuarios//
		Usuario usuarioGaston=new Usuario("Gaston", "Ciancio", "gaston_ciancio","1234", "gaston@gmail.com");
		
		Servicio s1 = new Servicio("servicio1", "servicio1", "servicio1", "servicio1", "servicio1", "servicio1", "servicio1",usuarioGaston );
	
		usuarioGaston.getServicios().add(s1);
		UsuarioDAOHibernateJPA usuario= (UsuarioDAOHibernateJPA) DaoFactory.getUsuarioDAO();
		ServicioDAOHibernateJPA servicio= (ServicioDAOHibernateJPA) DaoFactory.getServicioDAO();
		usuario.persistir(usuarioGaston);
		servicio.persistir(s1);
		
		System.out.println("Listado con Gaston");
		List<Usuario> usuariosListados=usuario.recuperarTodos("nombre");
		for (int i=0;i<usuariosListados.size();i++) {
			System.out.println(usuariosListados.get(i).getNombre_usuario());
		}
		
		
		
		Usuario usu=usuario.persistir(new Usuario("Pedro", "Perez", "pedro_perez","1234", "pedro@gmail.com"));
		
		System.out.println("Listado con Pedro y Gaston");
		usuariosListados=usuario.recuperarTodos("nombre");
		for (int i=0;i<usuariosListados.size();i++) {
			System.out.println(usuariosListados.get(i).getNombre_usuario());
		}
		
		usu.setNombre_usuario("pedrooooo");
		usuario.actualizar(usu);
		servicio.actualizar(s1);
		
		System.out.println("Listado con nombre de usuario de Pedro cambiado ");
		usuariosListados=usuario.recuperarTodos("nombre");
		for (int i=0;i<usuariosListados.size();i++) {
			System.out.println(usuariosListados.get(i).getNombre_usuario());
		}
		
		Usuario usu2=usuario.persistir(new Usuario("Maria", "Perez", "Maria_perez","1234", "maria@gmail.com"));
		
		System.out.println("Listado con Maria ");
		usuariosListados=usuario.recuperarTodos("nombre");
		for (int i=0;i<usuariosListados.size();i++) {
			System.out.println(usuariosListados.get(i).getNombre_usuario());
		}
		
		usuario.borrar(usu2);
		
		System.out.println("Listado SIN maria");
		usuariosListados=usuario.recuperarTodos("nombre");
		for (int i=0;i<usuariosListados.size();i++) {
			System.out.println(usuariosListados.get(i).getNombre_usuario());
		}
		
		
		
		
	}

}
