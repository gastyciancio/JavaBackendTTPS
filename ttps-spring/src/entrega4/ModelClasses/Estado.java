package entrega4.ModelClasses;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Estado{
	
	@Id @GeneratedValue
    @Column(name="ESTADO_ID")
	private Long id;
	
	private String estado;
	
	@OneToOne
	@JsonIgnore
	private Reserva reserva;
	
	public Estado() {}
	
	public Estado(String estado, Reserva reserva) {
		super();
		this.estado = estado;
		this.reserva = reserva;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

}
