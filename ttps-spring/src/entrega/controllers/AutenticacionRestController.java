package entrega.controllers;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import entrega4.DAOInterface.UsuarioDAO;
import entrega4.ModelClasses.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value="/autenticacion", produces=MediaType.APPLICATION_JSON_VALUE)

public class AutenticacionRestController {
	
	
	@Autowired
	UsuarioDAO usuarioDAO;
	
	@PostMapping
	public ResponseEntity<Object> autenticar(@RequestHeader("usuario") String usuario,@RequestHeader("clave") String clave) {
		
		try 
		{
			List<Usuario> users =  usuarioDAO.recuperarTodos("nombre_usuario");	
			boolean existe=false;
			Usuario uAux=null;
			int i=0;
			while(i<users.size() && existe==false) 
			{
				if (users.get(i).getNombre_usuario().equals(usuario) && users.get(i).getContraseña().equals(clave)) 
				{
					existe=true;
					uAux=users.get(i);
				}
				i++;
			}
			
			if (existe==true)	
			{
				String KEY="mi_clave";
				long tiempo = System.currentTimeMillis();
				String jwt = Jwts.builder()
						.signWith(SignatureAlgorithm.HS256, KEY)
						.setSubject(uAux.getNombre_usuario())
						.setIssuedAt(new Date(tiempo))
						.setExpiration(new Date(tiempo+900000))
						.claim("idUser", String.valueOf(uAux.getId()))
						.compact();
				//String token=String.valueOf(uAux.getId())+"-123456";
				Map<String, Object> map = new HashMap<String, Object>();
		        map.put("token", jwt);
		        map.put("userid", uAux.getId());
		       

		         return new ResponseEntity<Object>(map,HttpStatus.OK);
				
				
			}
			else {
				return new ResponseEntity<Object>(HttpStatus.FORBIDDEN);
			}
		}
		catch(Exception e) 
		{
			return new ResponseEntity<Object>(HttpStatus.FORBIDDEN);
		}
		
	}
	
}
