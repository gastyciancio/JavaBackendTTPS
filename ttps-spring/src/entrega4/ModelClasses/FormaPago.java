package entrega4.ModelClasses;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class FormaPago {
	
	@Id @GeneratedValue
    @Column(name="FORMA_PAGO_ID")
	private Long id;
	
	@OneToOne
	@JsonIgnore
	private Reserva reserva;
	
	private String forma_pago;
	
	public FormaPago() {}
	
	public FormaPago(String forma_pago, Reserva reserva) {
		super();
		this.forma_pago = forma_pago;
		this.reserva = reserva;
	}

	public String getForma_pago() {
		return forma_pago;
	}

	public void setForma_pago(String forma_pago) {
		this.forma_pago = forma_pago;
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
