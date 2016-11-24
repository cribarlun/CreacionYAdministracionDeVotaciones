package domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Survey extends DomainEntity implements Serializable {

	/**
	 * @Class Survey
	 * @classDec La clase contiene los atributos que forman la entidad Survey
	 *           (votaci�n), as� como las relaciones que tiene esta entidad con
	 *           el resto de entidades del dominio.
	 * 
	 */
	private static final long serialVersionUID = 749544364605664829L;
	// Attributes
	private String title;
	private String description;
	private Date startDate;
	private Date endDate;
	private Integer census;
	private String tipo;
	private Collection<String> tipoVotacion;
	private Collection<String> opciones;

	public Survey() {
		super();
		questions = new LinkedList<Question>();
	}

	// Methods

	// T�tulo de la votaci�n

	/**
	 * @return Este metodo devuelve el t�tulo de la votaci�n.
	 */

	@NotBlank
	@Length(min = 5, max = 100, message = "The field must be between 5 and 10 characters")
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            es el titulo de la votacion.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	// Descripci�n de la votaci�n
	/**
	 * @return Este metodo devuelve la descripci�n de la votaci�n.
	 */
	@NotBlank
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 *            es la descripci�n de la votaci�n.
	 */

	public void setDescription(String description) {
		this.description = description;
	}

	// Fecha de creaci�n de la votaci�n
	// con @DateTimeFormat especificamos el formato para la fecha que se va a
	// almacenar en la base de datos del sistema.

	/**
	 * 
	 * @return Este metodo devuelve la fecha de inicio de la votaci�n.
	 */
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * 
	 * @param startDate
	 *            es la fecha de inicio de la votaci�n.
	 */

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	// Fecha fin de la votaci�n

	/**
	 * 
	 * @return Este metodo devuelve la fecha fin de la votaci�n.
	 */

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * 
	 * @param endDate
	 *            es la fecha fin de la votaci�n.
	 */

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	// El atributo tipo indica de que tipo de votaci�n se trata.

	/**
	 * 
	 * @return devuelve el tipo de censo (abierto o cerrado).
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * 
	 * @param tipo
	 *            es el tipo de censo de la votaci�n (abierto o cerrado).
	 */

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	// El atributo opciones indica las posibles opciones para elegir.
	@ElementCollection
	public Collection<String> getOpciones() {
		return opciones;
	}

	public void setOpciones(Collection<String> opciones) {
		this.opciones = opciones;
	}

	// El atributo opciones indica las posibles opciones para elegir.
	@ElementCollection
	public Collection<String> getTipoVotacion() {
		return tipoVotacion;
	}

	public void setTipoVotacion(Collection<String> tipoVotacion) {
		this.tipoVotacion = tipoVotacion;
	}

	// Indica la id del censo relacionado a la votacion

	/**
	 * 
	 * @return Este metodo devuelve la Id del censo al que pertenece la
	 *         votaci�n.
	 */
	public Integer getCensus() {
		return census;
	}

	/**
	 * 
	 * @param census
	 *            es la id del centro al que pertenece la votaci�n.
	 */
	public void setCensus(Integer census) {
		this.census = census;
	}

	// Relationships
	private Collection<Question> questions;
	private String usernameCreator;

	// Asociaci�n con la clase entidad userNameControl

	/**
	 * 
	 * @return Este metodo devuelve el nombre del creador de la votaci�n.
	 */
	public String getUsernameCreator() {
		return usernameCreator;
	}

	/**
	 * 
	 * @param usernameCreator
	 *            es el nombre del creador de la votaci�n
	 */

	public void setUsernameCreator(String usernameCreator) {
		this.usernameCreator = usernameCreator;
	}

	// Asociaci�n con la clase entidad questions

	/**
	 * 
	 * @return Este metodo devuelve la lista de questions (preguntas).
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Collection<Question> getQuestions() {
		return questions;
	}

	/**
	 * 
	 * @param questions
	 *            es una colecci�n de questions(preguntas).
	 */

	public void setQuestions(Collection<Question> questions) {
		this.questions = questions;
	}

	/**
	 * 
	 * @param q
	 *            es una question (pregunta).
	 */
	public void addQuestion(Question q) {
		questions.add(q);
	}

	/**
	 * 
	 * @param q
	 *            es una question (pregunta).
	 */
	public void removeQuestion(Question q) {
		questions.remove(q);
	}

	// M�todo toString para la representaci�n del la entidad Survey

	/**
	 * 
	 * @return Este metodo devuelve la representaci�n como cadena de la entidad
	 *         Survey (votaci�n).
	 */
	@Override
	public String toString() {
		return "Survey [title=" + title + ", description=" + description
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", census=" + census + ", questions=" + questions + "]";
	}

}
