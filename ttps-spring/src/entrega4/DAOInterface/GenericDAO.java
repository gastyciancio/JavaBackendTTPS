package entrega4.DAOInterface;
import java.util.List;

public interface GenericDAO <T> {
	
 public T actualizar(T entity);
 
 public void borrar(T entity);
 
 
 public T persistir(T entity);
 
 
 public List<T> recuperarTodos(String column);
 
 public T findById(int id) ;
 

 
}