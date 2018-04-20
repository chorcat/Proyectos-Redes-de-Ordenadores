package util;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import es.uvigo.det.ro.simpledns.AAAAResourceRecord;
import es.uvigo.det.ro.simpledns.AResourceRecord;
import es.uvigo.det.ro.simpledns.CNAMEResourceRecord;
import es.uvigo.det.ro.simpledns.MXResourceRecord;
import es.uvigo.det.ro.simpledns.Message;
import es.uvigo.det.ro.simpledns.NSResourceRecord;
import es.uvigo.det.ro.simpledns.RRType;
import es.uvigo.det.ro.simpledns.SOAResourceRecord;
import es.uvigo.det.ro.simpledns.TXTResourceRecord;

/**
 * Clase que almacena los par�metros de las Respuestas del Servidor DNS que son
 * necesarias para imprimir.
 * 
 * @author Borja Gonzalez Enriquez - ro71
 * @author David Quintas Vilanova - ro67
 * @see QueryRecord
 *
 */

public class AnswerRecord {

	private InetAddress servidor;
	private final List<RRType> type;
	private final List<Integer> TTL;
	private final List<String> valor;
	private boolean isCache;

	/**
	 * Constructor que crea los 3 ArrayList (con el tama�o del n�mero de
	 * respuestas del message) y recorre el ArrayList de las respuestas del
	 * message.
	 * 
	 * Tanto el {@link RRType} como el TTL son comunes a cualquiera de las
	 * respuestas. En cambio, el Valor, depende el {@link RRType} escogido para
	 * la consulta.
	 * 
	 * En dicho caso, se hace un cast dependiendo de la subclase que sea la
	 * respuesta del message y se obtiene entonces el Valor dependiente de dicho
	 * RRType.
	 * 
	 * @param message
	 *            {@link Message} de respuesta del servidor DNS ra�z.
	 * @param servidor
	 *            IP del servidor DNS ra�z.
	 */

	public AnswerRecord(Message message, InetAddress servidor) {

		this.servidor = servidor;
		this.type = new ArrayList<>(message.getAnswers().size());
		this.TTL = new ArrayList<>(message.getAnswers().size());
		this.valor = new ArrayList<>(message.getAnswers().size());
		isCache = false;

		for (int i = 0; i < message.getAnswers().size(); i++) {
			this.type.add(message.getAnswers().get(i).getRRType());
			this.TTL.add(message.getAnswers().get(i).getTTL());

			if (message.getAnswers().get(i) instanceof AResourceRecord) {
				AResourceRecord record = (AResourceRecord) message.getAnswers().get(i);
				this.valor.add(record.getAddress().getHostAddress());
			} else if (message.getAnswers().get(i) instanceof NSResourceRecord) {
				NSResourceRecord record = (NSResourceRecord) message.getAnswers().get(i);
				this.valor.add(record.getNS().toString());
			} else if (message.getAnswers().get(i) instanceof AAAAResourceRecord) {
				AAAAResourceRecord record = (AAAAResourceRecord) message.getAnswers().get(i);
				this.valor.add(record.getAddress().getHostAddress());
			} else if (message.getAnswers().get(i) instanceof SOAResourceRecord) {
				SOAResourceRecord record = (SOAResourceRecord) message.getAnswers().get(i);
				this.valor.add(record.toString());
			} else if (message.getAnswers().get(i) instanceof TXTResourceRecord) {
				TXTResourceRecord record = (TXTResourceRecord) message.getAnswers().get(i);
				this.valor.add(record.getTXT_data());
			} else if (message.getAnswers().get(i) instanceof CNAMEResourceRecord) {
				CNAMEResourceRecord record = (CNAMEResourceRecord) message.getAnswers().get(i);
				this.valor.add(record.getCname().toString());
			} else if (message.getAnswers().get(i) instanceof MXResourceRecord) {
				MXResourceRecord record = (MXResourceRecord) message.getAnswers().get(i);
				this.valor.add(record.toString());
			}

			else {
				System.err.println("ERROR");
			}
		}
	}

	public AnswerRecord(AnswerRecord answer, boolean cache) {
		this.servidor = answer.getServidor();
		this.TTL = answer.getTTL();
		this.type = answer.getType();
		this.valor = answer.getValor();
		this.isCache = cache;
	}

	public List<String> getValor() {
		return this.valor;
	}

	public List<RRType> getType() {
		return this.type;
	}

	public List<Integer> getTTL() {
		return this.TTL;
	}

	public InetAddress getServidor() {
		return this.servidor;
	}

	public boolean getCache() {
		return isCache;
	}

	public void setIsCache(boolean cache) {
		this.isCache = cache;
	}

	public String toString() {
		String answer = "";
		if (isCache == false) {
			for (int i = 0; i < type.size(); i++) {
				if (i == type.size() - 1) {
					answer += "A: " + servidor.getHostAddress() + " " + type.get(i) + " " + TTL.get(i) + " "
							+ valor.get(i);
				} else {
					answer += "A: " + servidor.getHostAddress() + " " + type.get(i) + " " + TTL.get(i) + " "
							+ valor.get(i) + "\n";
				}
			}
		}

		else {
			for (int i = 0; i < type.size(); i++) {
				if (i == type.size() - 1) {
					answer += "A: cache " + type.get(i) + " " + TTL.get(i) + " " + valor.get(i);
				} else {
					answer += "A: cache " + type.get(i) + " " + TTL.get(i) + " " + valor.get(i) + "\n";
				}
			}
		}
		return answer;
	}
}
