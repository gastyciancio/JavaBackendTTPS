package entrega.controllers;

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
import entrega4.DAOInterface.ServicioDAO;
import entrega4.DAOInterface.UsuarioDAO;
import entrega4.ModelClasses.Reserva;
import entrega4.ModelClasses.Servicio;
import entrega4.ModelClasses.Usuario;
import entrega4.ModelClasses.Valoracion;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
	
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value="/servicio", produces=MediaType.APPLICATION_JSON_VALUE)

public class ServicioRestController {

@Autowired
ServicioDAO servicioDAO;

@Autowired
UsuarioDAO usuarioDAO;

//creo un servicio
@PostMapping
public ResponseEntity<Servicio> createService(@RequestBody Servicio serv, 
		 @RequestHeader Map<String, String> mapHeaders,@RequestHeader("idPersona") int idP,@RequestHeader("token") String token) {
	
	if(token=="")
		return new ResponseEntity<Servicio>(HttpStatus.UNAUTHORIZED);
	
	Usuario current_user = usuarioDAO.findById(idP);
	if (permitido(current_user.getNombre_usuario(),token)) {
	
		 Usuario user = usuarioDAO.findById(idP);
		 user.getServicios().add(serv);
		 serv.setUsuario(user);
		 usuarioDAO.actualizar(user);
		 return new ResponseEntity<Servicio>(serv, HttpStatus.CREATED);
	}
	else
		return new ResponseEntity<Servicio>(HttpStatus.UNAUTHORIZED);
}

private int devolverPromedioDeServicio(Servicio s) {
	if(s.getValoraciones().size()==0) return 0;
	
	int inicio=0;
	for (int i=0;i<s.getValoraciones().size();i++) {
	
		int sumaNotas=0;
		sumaNotas=sumaNotas+Integer.parseInt(s.getValoraciones().get(i).getCalidad_precio());
		sumaNotas=sumaNotas+Integer.parseInt(s.getValoraciones().get(i).getDiseno());
		sumaNotas=sumaNotas+Integer.parseInt(s.getValoraciones().get(i).getLimpieza());
		sumaNotas=sumaNotas+Integer.parseInt(s.getValoraciones().get(i).getSimpatia());
		sumaNotas=sumaNotas+Integer.parseInt(s.getValoraciones().get(i).getSabor());
		int promedioValoracion=sumaNotas/5;
		inicio=inicio+promedioValoracion;
		
	}
	
	return inicio/s.getValoraciones().size();
}

@GetMapping
public ResponseEntity<List<Servicio>> listAllServices(
		 @RequestHeader Map<String, String> mapHeaders,@RequestHeader("idPersona") int idP,@RequestHeader("token") String token) {
	
		if(token=="")
			return new ResponseEntity<List<Servicio>>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
		
			 List<Servicio> services = servicioDAO.recuperarTodos("nombre");
			 Servicio[] arrayS = new Servicio[services.size()];
			 services.toArray(arrayS);
			 
			 
			 Arrays.sort(arrayS, new Comparator<Servicio>() {
		         @Override
		         public int compare(Servicio first, Servicio second)
		         {
		             if (devolverPromedioDeServicio(first) != devolverPromedioDeServicio(second)) {
		                 return devolverPromedioDeServicio(second) -  devolverPromedioDeServicio(first);
		             }
		             else
		            	 return devolverPromedioDeServicio(first) -  devolverPromedioDeServicio(second);
		             
		         }
		     });
			 services = Arrays.asList(arrayS);
			 
			 
			 if(services.isEmpty()){
			 return new ResponseEntity<List<Servicio>>(HttpStatus.NO_CONTENT);
			 }
			 return new ResponseEntity<List<Servicio>>(services, HttpStatus.OK);
		}
		else
			return new ResponseEntity<List<Servicio>>(HttpStatus.UNAUTHORIZED);
	}

@GetMapping("/{id}")
public ResponseEntity<Servicio> getServiceFromId(@PathVariable("id") int id,
		 @RequestHeader Map<String, String> mapHeaders,@RequestHeader("idPersona") int idP,@RequestHeader("token") String token) {
	
	if(token=="")
			return new ResponseEntity<Servicio>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
		
		 Servicio servBD = servicioDAO.findById(id);
		 return new ResponseEntity<Servicio>(servBD, HttpStatus.OK);
		}
		else
			return new ResponseEntity<Servicio>(HttpStatus.UNAUTHORIZED);
	}


//modifico un servicio a partir de su id 
@PutMapping("/{id}")
public ResponseEntity<Servicio> editService(@PathVariable("id") int id,@RequestBody Servicio serv,@RequestHeader("idPersona") int idP,@RequestHeader("token") String token) {
	
	if(token=="")
			return new ResponseEntity<Servicio>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
	
				try 
				{
					Servicio servBD = servicioDAO.findById(id);
					servBD.setDescripcion(serv.getDescripcion());
					servBD.setNombre(serv.getNombre());
					servBD.setImagen1(serv.getImagen1());
					servBD.setImagen2(serv.getImagen2());
					servBD.setImagen3(serv.getImagen3());
					servBD.setTipo(serv.getTipo());
					servBD.setTwitter(serv.getTwitter());
					servBD.setInstagram(serv.getInstagram());
					servBD.setUrl(serv.getUrl());
					servBD.setWhatsapp(serv.getWhatsapp());
					servBD.setValoraciones(serv.getValoraciones());
					servicioDAO.actualizar(servBD);
					return new ResponseEntity<Servicio>(servBD, HttpStatus.OK);
					
				}
				catch(Exception e) 
				{
					return new ResponseEntity<Servicio>(HttpStatus.NOT_FOUND);
				}
		}
		else
			return new ResponseEntity<Servicio>(HttpStatus.UNAUTHORIZED);
	}


@PutMapping("/borrar/{id}")
public ResponseEntity<Servicio> deleteService(@PathVariable("id") long id,@RequestHeader("idPersona") int idP,@RequestHeader("token") String token){
	
	if(token=="")
		return new ResponseEntity<Servicio>(HttpStatus.UNAUTHORIZED);
	
	Usuario current_user = usuarioDAO.findById(idP);
	if (permitido(current_user.getNombre_usuario(),token)) {

		try{
			int myInt = (int) id;
			Servicio serv= servicioDAO.findById(myInt);
			Usuario u=serv.getUsuario();
			u.getServicios().remove(serv);
			serv.setUsuario(null);
			servicioDAO.actualizar(serv);
			return new ResponseEntity<Servicio>(serv, HttpStatus.OK);
				
			}catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<Servicio>(HttpStatus.NOT_FOUND);
			}
	}
	else
		return new ResponseEntity<Servicio>(HttpStatus.UNAUTHORIZED);
	
}

@PutMapping("/borrar2/{id}")
public ResponseEntity<Servicio> deleteService2(@PathVariable("id") long id,@RequestHeader("idPersona") int idP,@RequestHeader("token") String token){
	
	if(token=="")
		return new ResponseEntity<Servicio>(HttpStatus.UNAUTHORIZED);
	
	Usuario current_user = usuarioDAO.findById(idP);
	if (permitido(current_user.getNombre_usuario(),token)) {
		try{
			int myInt = (int) id;
			Servicio serv= servicioDAO.findById(myInt);
			servicioDAO.borrar(serv);
	
			return new ResponseEntity<Servicio>(serv, HttpStatus.OK);
				
			}catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<Servicio>(HttpStatus.NOT_FOUND);
			}
	}
	else
		return new ResponseEntity<Servicio>(HttpStatus.UNAUTHORIZED);

}

@PutMapping("/borrar3/{id}")
public ResponseEntity<Servicio> deleteService3(@PathVariable("id") long id,@RequestHeader("idPersona") int idP,@RequestHeader("token") String token){
	
	if(token=="")
		return new ResponseEntity<Servicio>(HttpStatus.UNAUTHORIZED);
	
	Usuario current_user = usuarioDAO.findById(idP);
	if (permitido(current_user.getNombre_usuario(),token)) {
	
		try{
			int myInt = (int) id;
			Servicio serv= servicioDAO.findById(myInt);
			Usuario u=serv.getUsuario();
			System.out.println(u.getServicios().size());
			u.getServicios().remove(serv);
			serv.setUsuario(null);
			servicioDAO.actualizar(serv);
			servicioDAO.borrar(serv);
			System.out.println(u.getServicios().size());
			return new ResponseEntity<Servicio>(serv, HttpStatus.OK);
				
			}catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<Servicio>(HttpStatus.NOT_FOUND);
			}
	}
	else
		return new ResponseEntity<Servicio>(HttpStatus.UNAUTHORIZED);

}

@GetMapping("/{id}/reservas")
public ResponseEntity<List<Reserva>> listAllReservas(@RequestHeader("token") String token,
		 @RequestHeader Map<String, String> mapHeaders,@PathVariable("id") int id,@RequestHeader("idPersona") int idP) {
	
	if(token=="")
		return new ResponseEntity<List<Reserva>>(HttpStatus.UNAUTHORIZED);
	
	Usuario user = usuarioDAO.findById(idP);
	if (permitido(user.getNombre_usuario(),token))
	{
		try 
		{
			Servicio servicio = servicioDAO.findById(id);
			List<Reserva> r=servicio.getReserva();
			return new ResponseEntity<List<Reserva>>(r, HttpStatus.OK);
		}
		catch(Exception e) 
		{
			return new ResponseEntity<List<Reserva>>(HttpStatus.NOT_FOUND);
		}
	}
	else
		return new ResponseEntity<List<Reserva>>(HttpStatus.UNAUTHORIZED);
}
@GetMapping("/{id}/valoraciones")
public ResponseEntity<List<Valoracion>> listAllValorations(@PathVariable("id") int id,@RequestHeader("idPersona") int idP,@RequestHeader("token") String token) {
		
		if(token=="")
			return new ResponseEntity<List<Valoracion>>(HttpStatus.UNAUTHORIZED);
		
		Usuario user = usuarioDAO.findById(idP);
		if (permitido(user.getNombre_usuario(),token)){
	
		
			try 
			{
				Servicio servicio = servicioDAO.findById(id);
				List<Valoracion> v=servicio.getValoraciones();
				return new ResponseEntity<List<Valoracion>>(v, HttpStatus.OK);
			}
			catch(Exception e) 
			{
				return new ResponseEntity<List<Valoracion>>(HttpStatus.NOT_FOUND);
			}
		}
		else
			return new ResponseEntity<List<Valoracion>>(HttpStatus.UNAUTHORIZED);
	
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

