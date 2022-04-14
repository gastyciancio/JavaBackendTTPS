package entrega.controllers;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import entrega4.DAOInterface.EstadoDAO;
import entrega4.DAOInterface.ReservaDAO;
import entrega4.DAOInterface.UsuarioDAO;
import entrega4.ModelClasses.Estado;
import entrega4.ModelClasses.Reserva;
import entrega4.ModelClasses.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;



@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value="/estado", produces=MediaType.APPLICATION_JSON_VALUE)
public class EstadoRestController {

	
	@Autowired
	EstadoDAO estadoDAO;
	
	@Autowired
	ReservaDAO reservaDAO;
	
	@Autowired
	UsuarioDAO usuarioDAO;
	
	
	//creo un estado
	@PostMapping
	public ResponseEntity<Estado> createEstado(@RequestBody Estado est, 
			 @RequestHeader Map<String, String> mapHeaders,@RequestHeader("idReserva") int idR, @RequestHeader("token") String token, @RequestHeader("idPersona") int idP) {
		if(token=="")
			return new ResponseEntity<Estado>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
			 Reserva rese = reservaDAO.findById(idR);
			 if(rese.getEstado()==null) {
				 	rese.setEstado(est);
				 	est.setReserva(rese);
				 	reservaDAO.actualizar(rese);
				 	return new ResponseEntity<Estado>(est, HttpStatus.CREATED);
			 }
			 else
				 return new ResponseEntity<Estado>(HttpStatus.PRECONDITION_FAILED);
		}
		else return new ResponseEntity<Estado>(HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Estado> getStatusFromIdReserva(@PathVariable("id") int id,
			 @RequestHeader Map<String, String> mapHeaders,@RequestHeader("token") String token, @RequestHeader("idPersona") int idP) {
		
		if(token=="")
			return new ResponseEntity<Estado>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
			 Reserva rese = reservaDAO.findById(id);
			 Estado statusBD = rese.getEstado();
			 return new ResponseEntity<Estado>(statusBD, HttpStatus.OK);
		}
		else 
			return new ResponseEntity<Estado>(HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping
	public ResponseEntity<List<Estado>> listAllStatus(
			 @RequestHeader Map<String, String> mapHeaders,@RequestHeader("token") String token, @RequestHeader("idPersona") int idP) {
		
		if(token=="")
			return new ResponseEntity<List<Estado>>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
			 List<Estado> estados = estadoDAO.recuperarTodos("estado");
			 if(estados.isEmpty()){
			 return new ResponseEntity<List<Estado>>(HttpStatus.NO_CONTENT);
			 }
	
			 return new ResponseEntity<List<Estado>>(estados, HttpStatus.OK);
			}
		
		else
			return new ResponseEntity<List<Estado>>(HttpStatus.UNAUTHORIZED);
	}


	//modifico un estado a partir de su id 
	@PutMapping("/{id}")
	public ResponseEntity<Estado> editEstado(@PathVariable("id") int id,@RequestBody Estado est,@RequestHeader("token") String token, @RequestHeader("idPersona") int idP) {
		
		if(token=="")
			return new ResponseEntity<Estado>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {

				try 
				{
					Estado estadoBD = estadoDAO.findById(id);
					estadoBD.setEstado(est.getEstado());
					estadoDAO.actualizar(estadoBD);
					return new ResponseEntity<Estado>(estadoBD, HttpStatus.OK);
					
				}
				catch(Exception e) 
				{
					return new ResponseEntity<Estado>(HttpStatus.NOT_FOUND);
				}
		}
		else 
			return new ResponseEntity<Estado>(HttpStatus.UNAUTHORIZED);
	}

	
	@PutMapping("/borrar/{id}")
	public ResponseEntity<Estado> deleteEstado(@PathVariable("id") long id, @RequestHeader("token") String token, @RequestHeader("idPersona") int idP){
		
		if(token=="")
			return new ResponseEntity<Estado>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
			try{
				int myInt = (int) id;
				Estado est= estadoDAO.findById(myInt);
				Reserva r=est.getReserva();
				r.setEstado(null);
				est.setReserva(null);
				estadoDAO.actualizar(est);
				estadoDAO.borrar(est);
	
				return new ResponseEntity<Estado>(est, HttpStatus.OK);
					
				}catch(Exception e) {
					e.printStackTrace();
					return new ResponseEntity<Estado>(HttpStatus.NOT_FOUND);
				}
		}
		else 
			return new ResponseEntity<Estado>(HttpStatus.UNAUTHORIZED);
	}
	
	private boolean permitido(String username, String token) {
		
		Claims misDatos=getAllClaimsFromToken(token);
		if (misDatos == null) return false;
		if (misDatos.getSubject().equals(username)  && !misDatos.getExpiration().before(new Date()))
			return true;
		else
			return false;
		}


	private Claims getAllClaimsFromToken(String token) {
	    Claims claims;
	    try {
	        claims = Jwts.parser()
	                .setSigningKey("mi_clave")
	                .parseClaimsJws(token)
	                .getBody();
	    } catch (Exception e) {
	        System.out.println("Could not get all claims Token from passed token");
	        claims = null;
	    }
	    return claims;
	}
	
	
	

}

