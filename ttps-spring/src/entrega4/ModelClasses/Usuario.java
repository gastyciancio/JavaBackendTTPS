package entrega4.ModelClasses;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;


import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Usuario {
	
	@Id @GeneratedValue
    @Column(name="USUARIO_ID")
	private long id;
	
	private String nombre;
	private String apellido;
	private String nombre_usuario;
	private String contrase�a;
	private String email;
	
	@OneToMany(fetch=FetchType.EAGER,mappedBy="usuario",cascade={CascadeType.ALL})
	@JsonIgnore
	private List<Servicio> servicios=new ArrayList<Servicio>();
	
	
	public Usuario() {}

	public Usuario(String nombre, String apellido, String nombre_usuario, String contrase�a, String email) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.nombre_usuario = nombre_usuario;
		this.contrase�a = contrase�a;
		this.email = email;
	}


	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getNombre_usuario() {
		return nombre_usuario;
	}
	public void setNombre_usuario(String nombre_usuario) {
		this.nombre_usuario = nombre_usuario;
	}
	public String getContrase�a() {
		return contrase�a;
	}
	public void setContrase�a(String contrase�a) {
		this.contrase�a = contrase�a;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public List<Servicio> getServicios() {
		return servicios;
	}


	public void setServicios(List<Servicio> servicios) {
		this.servicios = servicios;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	
	

}
