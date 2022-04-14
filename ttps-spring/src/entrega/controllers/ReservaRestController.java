package entrega.controllers;


import java.util.Date;
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

import entrega4.DAOInterface.ReservaDAO;
import entrega4.DAOInterface.ServicioDAO;
import entrega4.DAOInterface.UsuarioDAO;
import entrega4.ModelClasses.Estado;
import entrega4.ModelClasses.FormaPago;
import entrega4.ModelClasses.Reserva;
import entrega4.ModelClasses.Servicio;
import entrega4.ModelClasses.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value="/reserva", produces=MediaType.APPLICATION_JSON_VALUE)
public class ReservaRestController {
	
	@Autowired
	UsuarioDAO usuarioDAO;
	
	@Autowired
	ReservaDAO reservaDAO;
	
	@Autowired
	ServicioDAO servicioDAO;

	//creo un servicio
	@PostMapping
	public ResponseEntity<Reserva> createReserva(@RequestBody Reserva rese, 
			 @RequestHeader Map<String, String> mapHeaders,@RequestHeader("idPersona") int idP,@RequestHeader("idServicio") int idS,
			 	@RequestHeader("formapago") String formaP, @RequestHeader("token") String token) {
		
		if(token=="")
			return new ResponseEntity<Reserva>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
			try {
				
			 Estado status = new Estado();
			 status.setEstado("Pendiente");
			 status.setReserva(rese);
			 
			 FormaPago formaPago = new FormaPago();
			 formaPago.setForma_pago(formaP);
			 formaPago.setReserva(rese);
			 
			 Usuario user = usuarioDAO.findById(idP);
			 
			 rese.setEstado(status);
			 rese.setForma_de_pago(formaPago);
			 rese.setUsuario(user);
			 
			 usuarioDAO.actualizar(user);
			 
			 Servicio s = servicioDAO.findById(idS);
			 boolean ocupado=false;
			 for (int i=0;i<s.getReserva().size();i++) {
				 if (s.getReserva().get(i).getFecha().equals(rese.getFecha()))
					 ocupado=true;
			 }
			 if(ocupado==false) {
				 s.getReserva().add(rese);
				 rese.setServicio(s);
				 servicioDAO.actualizar(s);
				 return new ResponseEntity<Reserva>(rese, HttpStatus.CREATED);
			 }
			 else
				 return new ResponseEntity<Reserva>(HttpStatus.PRECONDITION_FAILED);
			}catch (Exception e) {
				 return new ResponseEntity<Reserva>(HttpStatus.PRECONDITION_FAILED);
			}
		}
		else
			return new ResponseEntity<Reserva>(HttpStatus.UNAUTHORIZED);
	}


	//modifico una reserva a partir de su id 
	@PutMapping("/{id}")
	public ResponseEntity<Reserva> editReserva(@PathVariable("id") int id,@RequestBody Reserva rese,@RequestHeader("token") String token, @RequestHeader("idPersona") int idP) {
		if(token=="")
			return new ResponseEntity<Reserva>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {

				try 
				{
					Reserva reseBD = reservaDAO.findById(id);
					reseBD.setDescripcion(rese.getDescripcion());
					reseBD.setInformacion(rese.getInformacion());
					reseBD.setMail(rese.getMail());
					reseBD.setTelefono(rese.getTelefono());
					reseBD.setFecha(rese.getFecha());
					reservaDAO.actualizar(reseBD);
					return new ResponseEntity<Reserva>(reseBD, HttpStatus.OK);
					
				}
				catch(Exception e) 
				{
					return new ResponseEntity<Reserva>(HttpStatus.NOT_FOUND);
				}
		}
		else
			return new ResponseEntity<Reserva>(HttpStatus.UNAUTHORIZED);
	}

	
	@PutMapping("/borrar/{id}")
	public ResponseEntity<Reserva> deleteReserva(@PathVariable("id") long id,@RequestHeader("token") String token, @RequestHeader("idPersona") int idP){
		if(token=="")
			return new ResponseEntity<Reserva>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
			try{
				int myInt = (int) id;
				Reserva rese= reservaDAO.findById(myInt);
				rese.setUsuario(null);
				
				Servicio s=rese.getServicio();
				s.getReserva().remove(rese);
				rese.setServicio(null);
				
				reservaDAO.actualizar(rese);
				reservaDAO.borrar(rese);
				return new ResponseEntity<Reserva>(rese, HttpStatus.OK);
					
				}catch(Exception e) {
					e.printStackTrace();
					return new ResponseEntity<Reserva>(HttpStatus.NOT_FOUND);
				}
		}
		else
			return new ResponseEntity<Reserva>(HttpStatus.UNAUTHORIZED);
			

	}
	
	@GetMapping("/{id}/servicio")
	public ResponseEntity<Servicio> getServiceOfReserva(@PathVariable("id") int id,@RequestHeader("token") String token, @RequestHeader("idPersona") int idP) {
		
		if(token=="")
			return new ResponseEntity<Servicio>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {

			try 
			{
				Reserva r = reservaDAO.findById(id);
				return new ResponseEntity<Servicio>(r.getServicio(), HttpStatus.OK);
			}
			catch(Exception e) 
			{
				return new ResponseEntity<Servicio>(HttpStatus.NOT_FOUND);
			}
		}
		else
			return new ResponseEntity<Servicio>(HttpStatus.UNAUTHORIZED);
		
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Reserva> getReservaById(@PathVariable("id") int id,@RequestHeader("token") String token, @RequestHeader("idPersona") int idP) {
		if(token=="")
			return new ResponseEntity<Reserva>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
			try 
			{
				Reserva r = reservaDAO.findById(id);
				return new ResponseEntity<Reserva>(r, HttpStatus.OK);
			}
			catch(Exception e) 
			{
				return new ResponseEntity<Reserva>(HttpStatus.NOT_FOUND);
			}
		}
		else
			return new ResponseEntity<Reserva>(HttpStatus.UNAUTHORIZED);
			
		
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
