import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import es.uvigo.det.ro.simpledns.AAAAResourceRecord;
import es.uvigo.det.ro.simpledns.AResourceRecord;
import es.uvigo.det.ro.simpledns.CNAMEResourceRecord;
import es.uvigo.det.ro.simpledns.DomainName;
import es.uvigo.det.ro.simpledns.Message;
import es.uvigo.det.ro.simpledns.NSResourceRecord;
import es.uvigo.det.ro.simpledns.RRType;
import es.uvigo.det.ro.simpledns.ResourceRecord;
import excepciones.ArgsLengthException;
import excepciones.DNS_raizException;
import excepciones.TransportProtocolException;
import transporte.Resolver;
import util.AnswerRecord;
import util.QueryRecord;

public class dnsclient {

	private static Map<QueryRecord, AnswerRecord> lista_consultas_respuestas = new LinkedHashMap<QueryRecord, AnswerRecord>();
	private static String DNS_raiz, transport_protocol;
	private static InetAddress last_dns_used;
	private static boolean last_question;

	public static void main(String[] args) {
		boolean hacer_consulta = true;
		Scanner scanner_consulta = null;

		try {
			last_question = false;
			ArgsLengthException.isCorrect(args);
			transport_protocol = args[0].equals("-u") ? "UDP" : "TCP";
			DNS_raiz = args[1];
			TransportProtocolException.isCorrect(transport_protocol);
			DNS_raizException.isCorrect(DNS_raiz);

			Resolver resolver = new Resolver(InetAddress.getByName(DNS_raiz));
			Message message = null;
			if (transport_protocol.equals("TCP"))
				resolver.setUseUdp(false);

			String consulta, Nombre;
			RRType rrtype;
			scanner_consulta = new Scanner(System.in);

			while (scanner_consulta.hasNext()) {

				consulta = scanner_consulta.nextLine();
				if (!consulta.startsWith("-")) {
					rrtype = RRType.valueOf(consulta.trim().split("\\ ")[0].toUpperCase());
					Nombre = consulta.trim().split("\\ ")[1];

					/*
					 * INICIO bloque : Comprobar si la consulta ya se realizo
					 * anteriormente en caso de que si este, se imprime la
					 * consulta realizada y su respuesta final
					 */
					QueryRecord query_realizada = new QueryRecord(transport_protocol, InetAddress.getByName(DNS_raiz),
							rrtype, new DomainName(Nombre));
					Iterator<QueryRecord> it = lista_consultas_respuestas.keySet().iterator();

					while (it.hasNext()) {
						QueryRecord querycache = (QueryRecord) it.next();
						if (querycache.toString().equals(query_realizada.toString())
								&& lista_consultas_respuestas.get(querycache).getCache() == false) {
							AnswerRecord answer = new AnswerRecord(lista_consultas_respuestas.get(querycache), true);
							querycache.setCache(true);
							System.out.println(querycache);
							System.out.println(answer);
							hacer_consulta = false;
							break;
						} else {
							hacer_consulta = true;
						}
					}
					// FIN bloque

					if (hacer_consulta) {
						// Primera consulta
						message = resolver.request(Nombre, rrtype);
						AnswerRecord answer;

						System.out.println(query_realizada);

						// Comprobamos si el campo Answers esta vacio
						if (message.getAnswers().isEmpty()) {
							message = consultasIterativas(message, Nombre, rrtype);
							last_question = false;
							answer = new AnswerRecord(message, last_dns_used);

						} else {
							answer = new AnswerRecord(message, InetAddress.getByName(DNS_raiz));

						}

						if (answer.getType().contains(RRType.CNAME)) {
							System.out.println(answer);
							message = sacarCNAMES(message);
							answer = new AnswerRecord(message, last_dns_used);
						}

						if (message.getAnswers().isEmpty()) {
							System.out.println(
									"-- El campo Answers está vacío. Del mismo modo que el campo Authority y Additional de todos los servidores DNS autorizados. "
											+ "Por lo tanto no es posible realizar la consulta. "
											+ "Para poder obtener una respuesta a dicha consulta, por favor intente con otro DNS raíz --");
						} else {
							lista_consultas_respuestas.put(query_realizada, answer);
							System.out.println(answer);
						}
					}
					System.out.println();
				}
			}

		} catch (

		ArgsLengthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransportProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DNS_raizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			scanner_consulta.close();
		}
	}

	private static Message sacarCNAMES(Message message) throws UnknownHostException {
		last_dns_used = InetAddress.getByName(DNS_raiz);
		Message message_aux = null;
		for (int i = 0; i < message.getAnswers().size(); i++) {
			if (message.getAnswers().get(i) instanceof CNAMEResourceRecord) {
				CNAMEResourceRecord cname_record = (CNAMEResourceRecord) message.getAnswers().get(i);
				Resolver resolver = new Resolver(InetAddress.getByName(DNS_raiz));
				System.out.println("Q: " + transport_protocol + " " + last_dns_used.getHostAddress() + " " + RRType.A
						+ " " + cname_record.getCname());
				message_aux = resolver.request(cname_record.getCname().toString(), RRType.A);
				message_aux = consultasIterativas(message_aux, cname_record.getCname().toString(), RRType.A);
				for (int x = 0; x < message_aux.getAnswers().size(); x++) {
					if (message_aux.getAnswers().get(x) instanceof CNAMEResourceRecord) {
						CNAMEResourceRecord cname_record_aux = (CNAMEResourceRecord) message_aux.getAnswers().get(i);
						message_aux = resolver.request(cname_record_aux.getCname().toString(), RRType.A);
						message_aux = consultasIterativas(message_aux, cname_record_aux.getCname().toString(),
								RRType.A);
						System.out
								.println("A: " + " " + cname_record_aux.getDomain() + " " + cname_record_aux.getRRType()
										+ " " + cname_record_aux.getTTL() + " " + cname_record_aux.getCname());
					}
					if (message_aux.getAnswers().get(x) instanceof AResourceRecord) {
						AResourceRecord a_record = (AResourceRecord) message_aux.getAnswers().get(x);
						System.out.println("A: " + " " + a_record.getDomain() + " " + a_record.getRRType() + " "
								+ a_record.getTTL() + " " + a_record.getAddress().getHostAddress());
					}
				}

			}

		}

		return message_aux;

	}

	private static Message consultasIterativas(Message message, String Nombre, RRType type)
			throws UnknownHostException {
		LinkedHashMap<NSResourceRecord, AResourceRecord> lista_auth_add;
		while (message.getAnswers().isEmpty()) {
			lista_auth_add = new LinkedHashMap<>();
			lista_auth_add = relacionar_Authority_Additional(message);
			if (lista_auth_add.isEmpty()) {
				break;
			}
			Iterator<NSResourceRecord> iterador = lista_auth_add.keySet().iterator();
			while (iterador.hasNext()) {
				NSResourceRecord ns_record = iterador.next();
				AResourceRecord a_record = lista_auth_add.get(ns_record);
				Resolver resolver = new Resolver(a_record.getAddress());
				if (!last_question) {
					System.out.println("A: " + " " + ns_record.getDomain() + " " + ns_record.getRRType() + " "
							+ ns_record.getTTL() + " " + ns_record.getNS());
					System.out.println("A: " + " " + a_record.getDomain() + " " + a_record.getRRType() + " "
							+ a_record.getTTL() + " " + a_record.getAddress().getHostAddress());
				}
				message = resolver.request(Nombre, type);
				System.out.println("Q: " + transport_protocol + " " + a_record.getAddress().getHostAddress() + " "
						+ type + " " + Nombre);

				last_dns_used = a_record.getAddress();
				last_question = false;
				message = consultasIterativas(message, Nombre, type);
				if (message.getAnswers().size() > 0)
					break;
			}
		}

		return message;
	}

	private static LinkedHashMap<NSResourceRecord, AResourceRecord> relacionar_Authority_Additional(Message message)
			throws UnknownHostException {
		LinkedHashMap<NSResourceRecord, AResourceRecord> lista_auth_add = new LinkedHashMap<>();
		List<ResourceRecord> lista_auth = new LinkedList<>();
		List<ResourceRecord> lista_add = new LinkedList<>();

		for (int i = 0; i < message.getNameServers().size(); i++) {
			if (message.getNameServers().get(i) instanceof NSResourceRecord)
				lista_auth.add(message.getNameServers().get(i));
		}

		for (int i = 0; i < message.getAdditonalRecords().size(); i++) {
			if (message.getAdditonalRecords().get(i) instanceof AResourceRecord)
				lista_add.add(message.getAdditonalRecords().get(i));
		}

		if (lista_add.isEmpty()) {
			for (int i = 0; i < lista_auth.size(); i++) {
				if (lista_auth.get(i) instanceof NSResourceRecord) {
					NSResourceRecord ns_record = (NSResourceRecord) lista_auth.get(i);
					Resolver resolver = new Resolver(InetAddress.getByName(DNS_raiz));
					QueryRecord query_realizada = new QueryRecord(transport_protocol, InetAddress.getByName(DNS_raiz),
							RRType.A, ns_record.getNS());
					System.out.println(
							"-- Faltan registros A en la sección Additional, se realizan nuevas consultas sobre los registros "
									+ "NS de la Authority para poder continuar con las consultas iterativas --");
					System.out.println(query_realizada);
					Message message_aux = resolver.request(ns_record.getNS().toString(), RRType.A);
					message_aux = consultasIterativas(message_aux, ns_record.getNS().toString(), RRType.A);

					for (int d = 0; d < message_aux.getAnswers().size(); d++) {
						if (message_aux.getAnswers().get(d) instanceof AResourceRecord)
							lista_add.add(message_aux.getAnswers().get(d));
					}
					AnswerRecord answer = new AnswerRecord(message_aux, last_dns_used);
					if (!message_aux.getAnswers().isEmpty()) {
						System.out.println(answer);
						System.out.println(
								"-- Hemos obtenido la dirección IP (registro A) del NS de la sección Authority, "
										+ "por tanto podemos continuar con las consultas iterativas --");
					}
				}
			}
			last_question = true;
		}

		for (int i = 0; i < lista_add.size(); i++) {
			if (lista_add.get(i) instanceof AAAAResourceRecord) {
				lista_add.remove(i);
			}
		}

		for (int i = 0; i < lista_auth.size(); i++) {
			NSResourceRecord ns_record = (NSResourceRecord) lista_auth.get(i);
			for (int d = 0; d < lista_add.size(); d++) {
				AResourceRecord a_record = (AResourceRecord) lista_add.get(d);
				if (ns_record.getNS().toString().equals(a_record.getDomain().toString())) {
					lista_auth_add.put(ns_record, a_record);
					break;
				}
			}
		}

		return lista_auth_add;
	}

}
