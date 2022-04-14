package entrega4.ModelClasses;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Valoracion {
	
	@Id @GeneratedValue
    @Column(name="VALORACION_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="servicio_id")
	@JsonIgnore
	private Servicio servicio;
	
	@OneToOne
	@JsonIgnore
	private Reserva reserva;
	
	private String limpieza;
	private String calidad_precio;
	private String simpatia;
	private String sabor;
	private String diseno;
	

	public Valoracion(String limpieza, String calidad_precio, String simpatia, String sabor, String diseno) {
		super();
		this.limpieza = limpieza;
		this.calidad_precio = calidad_precio;
		this.simpatia = simpatia;
		this.sabor = sabor;
		this.diseno = diseno;
	}
	
	public Valoracion() {}
	
	public String getLimpieza() {
		return limpieza;
	}
	public void setLimpieza(String limpieza) {
		this.limpieza = limpieza;
	}
	public String getCalidad_precio() {
		return calidad_precio;
	}
	public void setCalidad_precio(String calidad_precio) {
		this.calidad_precio = calidad_precio;
	}
	public String getSimpatia() {
		return simpatia;
	}
	public void setSimpatia(String simpatia) {
		this.simpatia = simpatia;
	}
	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}
	
	public Long getId() {
		return id;
	}

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSabor() {
		return sabor;
	}

	public void setSabor(String sabor) {
		this.sabor = sabor;
	}

	public String getDiseno() {
		return diseno;
	}

	public void setDiseno(String diseño) {
		this.diseno = diseño;
	}
	

}
