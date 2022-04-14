package entrega.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import entrega4.DAOInterface.ReservaDAO;
import entrega4.DAOInterface.UsuarioDAO;
import entrega4.ModelClasses.Reserva;
import entrega4.ModelClasses.Servicio;
import entrega4.ModelClasses.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;



	
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value="/usuario", produces=MediaType.APPLICATION_JSON_VALUE)


public class UsuarioRestController {
	
@Autowired
UsuarioDAO usuarioDAO;

@Autowired
ReservaDAO reservaDAO;
	 
//Recupero todos los usuarios

@PostMapping
public ResponseEntity<Usuario> createUser(@RequestBody Usuario user, 
         @RequestHeader Map<String, String> mapHeaders) {
		
	     try {
		     List<Usuario> users =  usuarioDAO.recuperarTodos("nombre_usuario");
		     boolean existe=false;
		        int i=0;
		        while(i<users.size() && existe==false) 
		        {
		            if (users.get(i).getNombre_usuario().equals(user.getNombre_usuario()) || users.get(i).getEmail().equals(user.getEmail())) 
		            {
		                existe=true;
		            }
		            i++;
		        }
		        if(existe==true) {
		            return new ResponseEntity<Usuario>(HttpStatus.FORBIDDEN);
		        }
		        else {
		            Usuario u =usuarioDAO.persistir(user);
		            return new ResponseEntity<Usuario>(u, HttpStatus.CREATED);
		        }
	     }catch(Exception e) {
	         return new ResponseEntity<Usuario>(HttpStatus.FORBIDDEN);
	     }
		
	}


@GetMapping
public ResponseEntity<List<Usuario>> listAllUsers(@RequestHeader("idPersona") int idP,@RequestHeader("token") String token,
		 @RequestHeader Map<String, String> mapHeaders) {
	
		if(token=="")
			return new ResponseEntity<List<Usuario>>(HttpStatus.UNAUTHORIZED);
		
		Usuario current_user = usuarioDAO.findById(idP);
		if (permitido(current_user.getNombre_usuario(),token)) {
		
			 List<Usuario> users = usuarioDAO.recuperarTodos("nombre");
			 if(users.isEmpty()){
			 return new ResponseEntity<List<Usuario>>(HttpStatus.NO_CONTENT);
			 }
			 return new ResponseEntity<List<Usuario>>(users, HttpStatus.OK);
		}
		else
			return new ResponseEntity<List<Usuario>>(HttpStatus.UNAUTHORIZED);
	}

	//Recupero un usuario dado
@GetMapping("/{id}")
	public ResponseEntity<Usuario> getUser(@PathVariable("id") int id,@RequestHeader("token") String token) {
	
		if(token=="")
			return new ResponseEntity<Usuario>(HttpStatus.UNAUTHORIZED);
	
		Usuario current_user = usuarioDAO.findById(id);
		if (permitido(current_user.getNombre_usuario(),token)) 
		{
			try 
			{
				Usuario user = usuarioDAO.findById(id);
				return new ResponseEntity<Usuario>(user, HttpStatus.OK);
			}
			catch(Exception e) 
			{
				return new ResponseEntity<Usuario>(HttpStatus.NOT_FOUND);
			}
		}
		else
			return new ResponseEntity<Usuario>(HttpStatus.UNAUTHORIZED);
	}

@GetMapping("/{id}/servicios")
public ResponseEntity<List<Servicio>> getUserServices(@PathVariable("id") int id,@RequestHeader("token") String token) {
	
	if(token=="")
		return new ResponseEntity<List<Servicio>>(HttpStatus.UNAUTHORIZED);

	
	Usuario current_user = usuarioDAO.findById(id);
	if (permitido(current_user.getNombre_usuario(),token)) 
	{
		try 
		{
			Usuario user = usuarioDAO.findById(id);
			List<Servicio> s=user.getServicios();
			return new ResponseEntity<List<Servicio>>(s, HttpStatus.OK);
		}
		catch(Exception e) 
		{
			return new ResponseEntity<List<Servicio>>(HttpStatus.NOT_FOUND);
		}
	}
	else
		return new ResponseEntity<List<Servicio>>(HttpStatus.UNAUTHORIZED);
}

@GetMapping("/{id}/reservas")
public ResponseEntity<List<Reserva>> getUserReservas(@PathVariable("id") int id,@RequestHeader("token") String token) {
	
	if(token=="")
		return new ResponseEntity<List<Reserva>>(HttpStatus.UNAUTHORIZED);
	
	Usuario current_user = usuarioDAO.findById(id);
	if (permitido(current_user.getNombre_usuario(),token)) 
	{
		try 
		{
			Usuario user = usuarioDAO.findById(id);
			List<Reserva> reservas=reservaDAO.recuperarTodos("informacion");
			List<Reserva> reservas_of_user = new ArrayList<Reserva>();
			 for (int i=0;i<reservas.size();i++) {
				
				 if ( reservas.get(i).getUsuario().getNombre_usuario().equals(user.getNombre_usuario())) {
					 reservas_of_user.add(reservas.get(i));
				 }
			 }
			return new ResponseEntity<List<Reserva>>(reservas_of_user, HttpStatus.OK);
		}
		catch(Exception e) 
		{
			return new ResponseEntity<List<Reserva>>(HttpStatus.NOT_FOUND);
		}
	}
	else
		return new ResponseEntity<List<Reserva>>(HttpStatus.UNAUTHORIZED);
}



@PutMapping("/{id}")
public ResponseEntity<Usuario> putUser(@PathVariable("id") int id,@RequestHeader("token") String token,@RequestBody Usuario user) {
	
		if(token=="")
			return new ResponseEntity<Usuario>(HttpStatus.UNAUTHORIZED);
	
	 	Usuario current_user = usuarioDAO.findById(id);
		if (permitido(current_user.getNombre_usuario(),token)) 
		{
			List<Usuario> users =  usuarioDAO.recuperarTodos("nombre_usuario");	
			boolean existe=false;
			int i=0;
			while(i<users.size() && existe==false) 
			{
				if (users.get(i).getNombre_usuario().equals(user.getNombre_usuario()) || users.get(i).getEmail().equals(user.getEmail())) 
				{
					if(users.get(i).getId()!=id) {
					existe=true;}
				}
				i++;
			}
			if(existe==false) {
			try 
			{
				Usuario usuarioBD = usuarioDAO.findById(id);
				usuarioBD.setNombre_usuario(user.getNombre_usuario());
				usuarioBD.setApellido(user.getApellido());
				usuarioBD.setNombre(user.getNombre());
				usuarioBD.setContraseña(user.getContraseña());
				usuarioBD.setEmail(user.getEmail());
				usuarioDAO.actualizar(usuarioBD);
				return new ResponseEntity<Usuario>(usuarioBD, HttpStatus.OK);
			}
			catch(Exception e) 
			{
				return new ResponseEntity<Usuario>(HttpStatus.NOT_FOUND);
			}
			}
			else {return new ResponseEntity<Usuario>(HttpStatus.FORBIDDEN);}
		}
		else
			return new ResponseEntity<Usuario>(HttpStatus.UNAUTHORIZED);
}


@DeleteMapping("/borrar/{id}")
public ResponseEntity<Usuario> deleteService(@PathVariable("id") long id,@RequestHeader("idPersona") int idP,@RequestHeader("token") String token){
	
	if(token=="")
		return new ResponseEntity<Usuario>(HttpStatus.UNAUTHORIZED);

 	Usuario current_user = usuarioDAO.findById(idP);
	if (permitido(current_user.getNombre_usuario(),token)) {
		
		try{
			int myInt = (int) id;
			Usuario u= usuarioDAO.findById(myInt);
			try {
				usuarioDAO.borrar(u);
			}
			catch(Exception e){
				e.printStackTrace();
				return new ResponseEntity<Usuario>(HttpStatus.PRECONDITION_REQUIRED);
			}
			return new ResponseEntity<Usuario>(u, HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Usuario>(HttpStatus.NOT_FOUND);
		}
	}
	else 
		return new ResponseEntity<Usuario>(HttpStatus.UNAUTHORIZED);
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







