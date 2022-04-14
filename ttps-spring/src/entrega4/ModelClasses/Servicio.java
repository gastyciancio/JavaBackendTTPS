package entrega4.ModelClasses;


import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
public class Servicio {
	
	@Id @GeneratedValue
    @Column(name="SERVICIO_ID")
	private long id;
	
	private String nombre;
	private String tipo;
	private String descripcion;
	private String url;
	private String twitter;
	private String instagram;
	private String whatsapp;
	@Lob 
	@Column(name="imagen1", length=1892)
	private String imagen1;
	
	@Lob 
	@Column(name="imagen2", length=1892)
	private String imagen2;
	
	@Lob 
	@Column(name="imagen3", length=1892)
	private String imagen3;
	
	
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="usuario_id")
	@JsonIgnore
	private Usuario usuario;
	
	
	@OneToMany(fetch=FetchType.EAGER,mappedBy="servicio",cascade={CascadeType.ALL})
	@JsonIgnore
	private List<Valoracion> valoraciones=new ArrayList<Valoracion>();
	

	@OneToMany(mappedBy="servicio",cascade={CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore
	private List<Reserva> reserva=new ArrayList<Reserva>();
	
	public Servicio() {}
	
	public Servicio(String nombre, String tipo, String descripcion, String url, String twitter, String instagram,
			String whatsapp, Usuario usu) {
		super();
		this.nombre = nombre;
		this.tipo = tipo;
		this.descripcion = descripcion;
		this.url = url;
		this.twitter = twitter;
		this.instagram = instagram;
		this.whatsapp = whatsapp;
		this.usuario=usu;
		
		
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTwitter() {
		return twitter;
	}
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}
	public String getInstagram() {
		return instagram;
	}
	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}
	public String getWhatsapp() {
		return whatsapp;
	}
	public void setWhatsapp(String whatsapp) {
		this.whatsapp = whatsapp;
	}

	public List<Valoracion> getValoraciones() {
		return valoraciones;
	}

	public void setValoraciones(List<Valoracion> valoraciones) {
		this.valoraciones = valoraciones;
	}
	
	


	public String getImagen1() {
		return imagen1;
	}

	public void setImagen1(String imagen1) {
		this.imagen1 = imagen1;
	}

	public String getImagen2() {
		return imagen2;
	}

	public void setImagen2(String imagen2) {
		this.imagen2 = imagen2;
	}

	public String getImagen3() {
		return imagen3;
	}

	public void setImagen3(String imagen3) {
		this.imagen3 = imagen3;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Reserva> getReserva() {
		return reserva;
	}

	public void setReserva(List<Reserva> reserva) {
		this.reserva = reserva;
	}
	
	
	

}
