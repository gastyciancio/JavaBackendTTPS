package entrega4.ModelClasses;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Reserva {
	
	@Id @GeneratedValue
    @Column(name="RESERVA_ID")
	private Long id;
	
	private String informacion;
	private String descripcion;
	private String mail;
	private String telefono;
	private String fecha;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="usuario_id")
	private Usuario usuario;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="servicio_id")
	private Servicio servicio;
	
	@OneToOne(cascade={CascadeType.ALL},mappedBy = "reserva")
	@JsonIgnore
	private FormaPago forma_de_pago;
	
	@OneToOne(cascade={CascadeType.ALL},mappedBy = "reserva")
	@JsonIgnore
	private Estado estado;
	
	@OneToOne(cascade={CascadeType.ALL},mappedBy = "reserva", orphanRemoval = true)
	@JsonIgnore
	private Valoracion valoracion;
	
	
	public Reserva(){}
	

	public Reserva(String informacion, String descripcion, String mail, String telefono,Servicio ser,Estado e,FormaPago f) {
		super();
		this.informacion = informacion;
		this.descripcion = descripcion;
		this.mail = mail;
		this.telefono = telefono;
		this.servicio=ser;
		this.forma_de_pago=f;
		this.estado=e;
	}
	
	public FormaPago getForma_de_pago() {
		return forma_de_pago;
	}

	public void setForma_de_pago(FormaPago forma_de_pago) {
		this.forma_de_pago = forma_de_pago;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getInformacion() {
		return informacion;
	}
	public void setInformacion(String informacion) {
		this.informacion = informacion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}


	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public String getFecha() {
		return fecha;
	}


	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Valoracion getValoracion() {
		return valoracion;
	}


	public void setValoracion(Valoracion valoracion) {
		this.valoracion = valoracion;
	}
	
	
	
	


}
