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

import entrega4.DAOInterface.FormaPagoDAO;
import entrega4.DAOInterface.ReservaDAO;
import entrega4.DAOInterface.UsuarioDAO;
import entrega4.ModelClasses.FormaPago;
import entrega4.ModelClasses.Reserva;
import entrega4.ModelClasses.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value="/formaPago", produces=MediaType.APPLICATION_JSON_VALUE)
public class FormaPagoRestController {
	

	@Autowired
	ReservaDAO reservaDAO;
	
	@Autowired
	FormaPagoDAO formaPagoDAO;
	
	@Autowired
	UsuarioDAO usuarioDAO;

	
	
	//creo una forma de pago
	@PostMapping
	public ResponseEntity<FormaPago> createFormaPago(@RequestBody FormaPago formP, 
			 @RequestHeader Map<String, String> mapHeaders,@RequestHeader("idReserva") int idR, @RequestHeader("token") String token, @RequestHeader("idPersona") int idP) {
		
		if(token=="")
			return new ResponseEntity<FormaPago>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
		
		
			 Reserva rese = reservaDAO.findById(idR);
			 if(rese.getForma_de_pago()==null) {
				 rese.setForma_de_pago(formP);
				 formP.setReserva(rese);
				 reservaDAO.actualizar(rese);
			 return new ResponseEntity<FormaPago>(formP, HttpStatus.CREATED);
			 }
			 else
				 return new ResponseEntity<FormaPago>(HttpStatus.PRECONDITION_FAILED);
		}
		else return new ResponseEntity<FormaPago>(HttpStatus.UNAUTHORIZED);
			
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<FormaPago> getStatusFromIdReserva(@PathVariable("id") int id,
			 @RequestHeader Map<String, String> mapHeaders, @RequestHeader("token") String token, @RequestHeader("idPersona") int idP) {
		
		if(token=="")
			return new ResponseEntity<FormaPago>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
		
		
			 Reserva rese = reservaDAO.findById(id);
			 FormaPago formaPagoBD = rese.getForma_de_pago();
			 return new ResponseEntity<FormaPago>(formaPagoBD, HttpStatus.OK);
		}
		else 
			return new ResponseEntity<FormaPago>(HttpStatus.UNAUTHORIZED);
				 
	}
	
	@GetMapping
	public ResponseEntity<List<FormaPago>> listAllFormaPagos(
			 @RequestHeader Map<String, String> mapHeaders,@RequestHeader("token") String token, @RequestHeader("idPersona") int idP) {
		if(token=="")
			return new ResponseEntity<List<FormaPago>>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
		
			 List<FormaPago> formaPagos = formaPagoDAO.recuperarTodos("forma_pago");
			 if(formaPagos.isEmpty()){
			 return new ResponseEntity<List<FormaPago>>(HttpStatus.NO_CONTENT);
			 }
			 return new ResponseEntity<List<FormaPago>>(formaPagos, HttpStatus.OK);
		}
		else 
			return new ResponseEntity<List<FormaPago>>(HttpStatus.UNAUTHORIZED);
	}


	//modifico una forma de pago a partir de su id 
	@PutMapping("/{id}")
	public ResponseEntity<FormaPago> editFormaPago(@PathVariable("id") int id,@RequestBody FormaPago formP,@RequestHeader("token") String token, @RequestHeader("idPersona") int idP) {
		
		if(token=="")
			return new ResponseEntity<FormaPago>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {

				try 
				{
					FormaPago formaPagoBD = formaPagoDAO.findById(id);
					formaPagoBD.setForma_pago(formP.getForma_pago());
					formaPagoDAO.actualizar(formaPagoBD);
					return new ResponseEntity<FormaPago>(formaPagoBD, HttpStatus.OK);
					
				}
				catch(Exception e) 
				{
					return new ResponseEntity<FormaPago>(HttpStatus.NOT_FOUND);
				}
		}
		else
			return new ResponseEntity<FormaPago>(HttpStatus.UNAUTHORIZED);
	}

	
	@PutMapping("/borrar/{id}")
	public ResponseEntity<FormaPago> deleteFormaPago(@PathVariable("id") long id,@RequestHeader("token") String token, @RequestHeader("idPersona") int idP){
		
		if(token=="")
			return new ResponseEntity<FormaPago>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
			try{
				int myInt = (int) id;
				FormaPago formP= formaPagoDAO.findById(myInt);
				Reserva r=formP.getReserva();
				r.setForma_de_pago(null);
				formP.setReserva(null);
				formaPagoDAO.actualizar(formP);
				formaPagoDAO.borrar(formP);
				return new ResponseEntity<FormaPago>(formP, HttpStatus.OK);
					
				}catch(Exception e) {
					e.printStackTrace();
					return new ResponseEntity<FormaPago>(HttpStatus.NOT_FOUND);
				}
		}
		else
			return new ResponseEntity<FormaPago>(HttpStatus.UNAUTHORIZED);
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
