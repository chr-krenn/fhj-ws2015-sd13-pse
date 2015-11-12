package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the delivery_system database table.
 * 
 */
@Entity
@Table(name="delivery_system")
@NamedQueries({
	@NamedQuery(name="DeliverySystem.findAll", query="SELECT d FROM DeliverySystem d"),
	@NamedQuery(name="DeliverySystem.findByToken", query="SELECT d FROM DeliverySystem d WHERE d.token = :token")
})
public class DeliverySystem implements Serializable {
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + deliverySystemId;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DeliverySystem)) {
			return false;
		}
		DeliverySystem other = (DeliverySystem) obj;
		if (deliverySystemId != other.deliverySystemId) {
			return false;
		}
		return true;
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="delivery_system_id", unique=true, nullable=false)
	private int deliverySystemId;

	@Column(nullable=false, length=64)
	private String name;

	@Column(nullable=false, length=16)
	private String token;

	//bi-directional many-to-one association to Message
	@OneToMany(mappedBy="deliverySystem")
	private List<Message> messages;

	public DeliverySystem() {
	}

	public int getDeliverySystemId() {
		return this.deliverySystemId;
	}

	public void setDeliverySystemId(int deliverySystemId) {
		this.deliverySystemId = deliverySystemId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Message addMessage(Message message) {
		getMessages().add(message);
		message.setDeliverySystem(this);

		return message;
	}

	public Message removeMessage(Message message) {
		getMessages().remove(message);
		message.setDeliverySystem(null);

		return message;
	}

}