package entrega.controllers;


import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import entrega4.DAOInterface.ValoracionDAO;
import entrega4.ModelClasses.Reserva;
import entrega4.ModelClasses.Servicio;
import entrega4.ModelClasses.Usuario;
import entrega4.ModelClasses.Valoracion;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value="/valoracion", produces=MediaType.APPLICATION_JSON_VALUE)
public class ValoracionRestController {
	@Autowired
	ServicioDAO servicioDAO;
	
	@Autowired
	ValoracionDAO valoracionDAO;
	
	@Autowired
	UsuarioDAO usuarioDAO;
	
	@Autowired
	ReservaDAO reservaDAO;

	//creo una valoracion
	@PostMapping
	public ResponseEntity<Valoracion> createValoracion(@RequestBody Valoracion val, 
			 @RequestHeader Map<String, String> mapHeaders,@RequestHeader("idServicio") int idS,@RequestHeader("idPersona") int idP,@RequestHeader("token") String token,@RequestHeader("idReserva") int idR) {
		
		if(token=="")
		return new ResponseEntity<Valoracion>(HttpStatus.UNAUTHORIZED);

	 	Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
				try {
					Reserva res = reservaDAO.findById(idR);
					Servicio ser = servicioDAO.findById(idS);
					if (res.getValoracion()==null) {
						res.setValoracion(val);
						val.setReserva(res);
						ser.getValoraciones().add(val);
				   		val.setServicio(ser);
				   		reservaDAO.actualizar(res);
				   		servicioDAO.actualizar(ser);
				   		return new ResponseEntity<Valoracion>(val, HttpStatus.CREATED);
					}
					else {
						 return new ResponseEntity<Valoracion>(HttpStatus.FORBIDDEN);
					}
					
				}
				catch(Exception e) {
			         return new ResponseEntity<Valoracion>(HttpStatus.NOT_FOUND);
				}
		}
		else
			return new ResponseEntity<Valoracion>(HttpStatus.UNAUTHORIZED);
	}


	//modifico una valoracion a partir de su id 
	@PutMapping("/{id}")
	public ResponseEntity<Valoracion> editValoracion(@PathVariable("id") int id,@RequestBody Valoracion val,@RequestHeader("idPersona") int idP,@RequestHeader("token") String token) {
		
		if(token=="")
			return new ResponseEntity<Valoracion>(HttpStatus.UNAUTHORIZED);

	 	Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
	

				try 
				{
					Valoracion valBD = valoracionDAO.findById(id);
					valBD.setCalidad_precio(val.getCalidad_precio());
					valBD.setLimpieza(val.getLimpieza());
					valBD.setSimpatia(val.getSimpatia());
					valoracionDAO.actualizar(valBD);
					return new ResponseEntity<Valoracion>(valBD, HttpStatus.OK);
					
				}
				catch(Exception e) 
				{
					return new ResponseEntity<Valoracion>(HttpStatus.NOT_FOUND);
				}
		}
		else
			return new ResponseEntity<Valoracion>(HttpStatus.UNAUTHORIZED);
	}

	
	@PutMapping("/borrar/{id}")
	public ResponseEntity<Valoracion> deleteValoracion(@PathVariable("id") long id,@RequestHeader("idPersona") int idP,@RequestHeader("token") String token){
		

		if(token=="")
			return new ResponseEntity<Valoracion>(HttpStatus.UNAUTHORIZED);

	 	Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
		
			try{
				int myInt = (int) id;
				Valoracion val= valoracionDAO.findById(myInt);
				Servicio s=val.getServicio();
				s.getValoraciones().remove(val);
				val.setServicio(null);
				valoracionDAO.actualizar(val);
				valoracionDAO.borrar(val);
				return new ResponseEntity<Valoracion>(val, HttpStatus.OK);
					
				}catch(Exception e) {
					e.printStackTrace();
					return new ResponseEntity<Valoracion>(HttpStatus.NOT_FOUND);
				}
		}
		else
			return new ResponseEntity<Valoracion>(HttpStatus.UNAUTHORIZED);

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
