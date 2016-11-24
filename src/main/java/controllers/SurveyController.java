package controllers;

import java.util.Collection;
import java.util.Date;

import main.java.Authority;
import main.java.AuthorityImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import services.QuestionService;
import services.SurveyService;
import services.requestsHttp;
import domain.CheckToken;
import domain.Question;
import domain.Survey;

/**
 * @Class SurveyController
 * @classDec La clase contiene el controlador que maneja las acciones de las
 *           votaciones, crear, a�adir preguntas, borrar, y la api para los
 *           otros m�dulos.
 */
@Controller
@RestController
@RequestMapping("/vote")
public class SurveyController {

	// Services ------------------------------------------

	@Autowired
	private SurveyService surveyService;

	@Autowired
	private QuestionService questionService;

//	private requestsHttp httpRequest = new requestsHttp();

	/**
	 * @return Constructor del Controlador.
	 */
	// Constructor ---------------------------------------
	public SurveyController() {
		super();
	}

	// Listing -------------------------------------------
	/**
	 * @return Este m�todo devuelve el modelo de vista con el listado de
	 *         votaciones.
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@CookieValue("user") String user) {
		ModelAndView result;
		System.out.println(user);
		Collection<Survey> surveis;
		surveis = surveyService.allCreatedSurveys(user);
		System.out.println( surveis);
		Date now = new Date(System.currentTimeMillis() - 1000);
		result = new ModelAndView("vote/list");
		result.addObject("surveis", surveis);
		result.addObject("hoy", now);

		return result;
	}

	// Creation ------------------------------------------
	/**
	 * @return Este m�todo devuelve el modelo de vista para el primer paso de la
	 *         votacion. El cual consiste en elegir el nombre, la descripci�n,
	 *         el tipo de censo y el intervalo de fecha de inicio y finalizaci�n
	 *         de la votaci�n.
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Survey survey;
		
		survey = surveyService.create();

		result = new ModelAndView("vote/create");
		result.addObject("survey", survey);
		result.addObject("actionURL", "vote/create.do");

		return result;
	}

	/**
	 * @param survey
	 *            La votaci�n que se ha creado en el primer paso.
	 * @param bindingResult
	 *            Este par�metro nos indica si hay algun error con los datos
	 *            introducidos compar�ndolos con las restricciones escritas en
	 *            el dominio.
	 * @return Este m�todo comprueba si la votaci�n es v�lida y en caso negativo
	 *         volvemos a la vista de creaci�n de la votaci�n para poder editar
	 *         los campos err�neos. En caso afirmativo, procederemos a la
	 *         siguiente vista la cual es para a�adir preguntas a la votaci�n.
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "addQuestion")
	public ModelAndView addQuestio(@CookieValue("user") String user,Survey survey, BindingResult bindingResult) {
		ModelAndView result;
		Assert.notNull(survey);
		Date now = new Date(System.currentTimeMillis() - 1000);
		System.out.println(survey.getStartDate());
		System.out.println(survey.getEndDate());
		if (bindingResult.hasErrors() || survey.getStartDate() == null || survey.getEndDate() == null
				|| survey.getTitle() == "" || survey.getTipo() == null || now.after(survey.getStartDate())
				|| now.after(survey.getEndDate()) || survey.getStartDate().after(survey.getEndDate())) {
			System.out.println(bindingResult.toString());
			result = new ModelAndView("vote/create");
			result.addObject("actionURL", "vote/create.do");
			result.addObject("survey", survey);
			if (survey.getStartDate() == null || survey.getEndDate() == null || survey.getTitle() == ""
					|| survey.getTipo() == null) {
				result.addObject("message", "survey.fields.empty");
			}
			if (now.after(survey.getStartDate()) || now.after(survey.getEndDate())) {
				result.addObject("message", "survey.dates.future");
			}
			if (survey.getStartDate().after(survey.getEndDate())) {
				result.addObject("message", "survey.start.end");
			}
		} else {
			try {
				Integer s2 = surveyService.save(survey,user);
				result = new ModelAndView("redirect:/vote/addQuestion.do");
				result.addObject("surveyId", s2);
			} catch (Throwable oops) {
				result = new ModelAndView("/vote/create");
				result.addObject("message", "survey.commit.error");
				result.addObject("survey", survey);
				if (survey.getStartDate() == null || survey.getEndDate() == null || survey.getTitle() == ""
						|| survey.getTipo() == null) {
					result.addObject("message", "survey.fields.empty");
				}
				if (survey.getStartDate() == null || survey.getEndDate() == null || survey.getTitle() == ""
						|| survey.getTipo() == null) {
					result.addObject("message", "survey.fields.empty");
				}
				if (now.after(survey.getStartDate()) || now.after(survey.getEndDate())) {
					result.addObject("message", "survey.dates.future");
				}
				if (survey.getStartDate().after(survey.getEndDate())) {
					result.addObject("message", "survey.start.end");
				}
			}
		}
		return result;
	}

	/**
	 * @param surveyId
	 *            La id votaci�n que a la que se le a�adiran preguntas.
	 * @return Este m�todo devuelve la vista en la que se podr�n a�adir
	 *         preguntas a la votaci�n.
	 */
	@RequestMapping(value = "/addQuestion", method = RequestMethod.GET)
	public ModelAndView addQuestion(Integer surveyId) {
		ModelAndView result;
		Survey survey = surveyService.findOne(surveyId);
		Question question = questionService.create(surveyId);
		question.setSurveyId(surveyId);
		result = new ModelAndView("vote/addQuestion");
		result.addObject("actionURL", "vote/addQuestion.do");
		result.addObject("survey", survey);
		result.addObject("questio", question);
		return result;
	}

	/**
	 * @param questio
	 *            La pregunta introducida de la votaci�n
	 * @param bindingResult
	 *            Este par�metro nos indica si hay algun error con los datos
	 *            introducidos comparandolos con las restricciones escritas en
	 *            el dominio.
	 * @return Este m�todo a�ade la pregunta a la votaci�n siempre que no est�
	 *         en blanco y vuelve a la misma vista para a�adir otra pregunta.
	 */
	@RequestMapping(value = "/addQuestion", method = RequestMethod.POST, params = "addQuestion")
	public ModelAndView addAnotherQuestion(Question questio, BindingResult bindingResult) {
		ModelAndView result;
		Assert.notNull(questio);
		Survey survey = surveyService.findOne(questio.getSurveyId());
		if (bindingResult.hasErrors()||questio.getText() == "") {
			result = new ModelAndView("vote/addQuestion");
			result.addObject("actionURL", "vote/addQuestion.do");
			result.addObject("surveyId", survey.getId());
			result.addObject("questio", questio);
			if(questio.getText() == ""){
			result.addObject("message","survey.fields.empty");
			}
		} else {
			try {
				int idQuestion = questionService.saveAndFlush(questio);
				surveyService.saveAddQuestion(survey.getId(), idQuestion, false);
				result = new ModelAndView("vote/addQuestion");
				Question question = questionService.create(survey.getId());
				question.setSurveyId(survey.getId());
				result.addObject("survey", survey);
				result.addObject("questio", question);

			} catch (Throwable oops) {
				result = new ModelAndView("vote/addQuestion");
				result.addObject("message", "survey.commit.error");
				result.addObject("actionURL", "vote/addQuestion.do");
				result.addObject("survey", survey.getId());
				result.addObject("questio", questio);
				if(questio.getText() == ""){
					result.addObject("message","survey.fields.empty");
					}
			}
		}
		return result;
	}

	/**
	 * @param questio
	 *            La pregunta introducida de la votaci�n
	 * @param bindingResult
	 *            Este par�metro nos indica si hay algun error con los datos
	 *            introducidos comparandolos con las restricciones escritas en
	 *            el dominio.
	 * @return Este m�todo a�ade la pregunta a la votaci�n siempre que no est�
	 *         en blanco y guarda del todo la votaci�n. Comprueba que el usuario
	 *         este en el sistema en la integraci�n con Verificaci�n. Establece
	 *         la conexi�n con Censo para crear el censo de la votacion y
	 *         Establece conexi�n con deliberaciones para crear el hilo de las
	 *         deliberaciones.
	 */
	@RequestMapping(value = "/addQuestion", method = RequestMethod.POST, params = "save")
	public ModelAndView saveSurvey(Question questio, BindingResult bindingResult) {
		ModelAndView result;
		Assert.notNull(questio);
		Survey survey = surveyService.findOne(questio.getSurveyId());
		if (bindingResult.hasErrors()|| questio.getText() == "") {
			result = new ModelAndView("vote/addQuestion");
			result.addObject("actionURL", "vote/addQuestion.do");
			result.addObject("survey", survey.getId());
			result.addObject("questio", questio);
			if(questio.getText() == ""){
				result.addObject("message","survey.fields.empty");
				}
		} else {
			try {
				int idQuestion = questionService.saveAndFlush(questio);
				Survey s1 = surveyService.saveAddQuestion(survey.getId(), idQuestion, true);
				// Integracion con Verificaci�n.
				Authority a = new AuthorityImpl();
				Integer token = CheckToken.calculateToken(survey.getId());
				System.out.println("Id Votacion: " + survey.getId());
				a.postKey(String.valueOf(survey.getId()), token);
				System.out.println("Token: " + token);
				// TODO integracion con censo. Falla el metodo que devuelve
				// censo.
				Integer censoId = 1;
				// censoId = httpRequest.generaPeticionCenso(survey.getId(),
				// survey.getStartDate(), survey.getEndDate(),
				// survey.getTitle(), survey.getTipo());
				surveyService.addCensus(censoId, s1.getId());

				// TODO integracion con deliberaciones.(Cambiar url de despliegue en el metodo)
//				httpRequest.generaPeticionDeliberations(survey.getId(), survey.getTitle());
				result = new ModelAndView("redirect:/vote/list.do");
			} catch (Throwable oops) {
				result = new ModelAndView("vote/addQuestion");
				result.addObject("message", "survey.commit.error");
				result.addObject("actionURL", "vote/addQuestion.do");
				result.addObject("survey", survey);
				result.addObject("questio", questio);
				if(questio.getText() == ""){
					result.addObject("message","survey.fields.empty");
					}
			}
		}
		return result;
	}

	/**
	 * @param surveyId
	 *            ID de la votaci�n a borrar
	 * @return Este m�todo cancela la votaci�n si se decide que no se terminar�n
	 *         de rellenar los datos del formulario de creaci�n(Una vez que se
	 *         pasa a a�adirle las preguntas).
	 */
	@RequestMapping(value = "/cancelSurvey", method = RequestMethod.GET)
	public ModelAndView cancelSurvey(@RequestParam int surveyId) {
		ModelAndView result;
		Assert.notNull(surveyId);
		try {
			surveyService.delete(surveyId);
			result = new ModelAndView("redirect:/vote/list.do");
		} catch (Throwable oops) {
			result = new ModelAndView("vote/addQuestion");
			result.addObject("message", "survey.commit.error");
			result.addObject("actionURL", "vote/addQuestion.do");
			result.addObject("survey", surveyId);
		}
		return result;
	}

	// Details ----------------------------------------------

	/**
	 * @param surveyId
	 *            ID de la votaci�n de la que se desea ver los detalles
	 * @return Este m�todo muestra la vista de los detalles de la votacion.
	 */
	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public ModelAndView details(@RequestParam int surveyId) {
		ModelAndView result;
		Survey survey;
		survey = surveyService.findOne(surveyId);
		Assert.notNull(survey);
		result = new ModelAndView("vote/details");
		result.addObject("survey", survey);
		return result;
	}

	/**
	 * @param surveyId
	 *            ID de la votaci�n de la que se desea ver los detalles
	 * @return Este m�todo muestra la vista de los detalles de la votacion.
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam int surveyId) {
		ModelAndView result;
		try {
			surveyService.delete(surveyId);
			result = new ModelAndView("redirect:/vote/list.do");
		} catch (Throwable oops) {
			result = new ModelAndView("vote/list");
			result.addObject("message", "survey.error.dates");
		}

		return result;
	}

	/**
	 * @param surveyId
	 *            ID de la votaci�n de la que se desea ver los detalles
	 * @return Este m�todo forma parte de la API de integraci�n. Devuelve un
	 *         JSON con los datos de la votaci�n
	 */
	@RequestMapping(value = "/survey", method = RequestMethod.GET)
	public Survey getSurvey(@RequestParam int id) {
		Survey result = surveyService.findOne(id);
		return result;
	}

	/**
	 * @return Este m�todo forma parte de la API de integraci�n. Devuelve un
	 *         JSON con los datos de todas la votaci�n finalizadas.
	 */
	@RequestMapping(value = "/finishedSurveys", method = RequestMethod.GET)
	public Collection<Survey> findAllfinishedSurveys() {
		Collection<Survey> result = surveyService.allFinishedSurveys();
		return result;
	}

	/**
	 * @return Este m�todo forma parte de la API de integraci�n. Devuelve un
	 *         JSON con los datos de todas la votaci�nes.
	 */
	@RequestMapping(value = "/allSurveys", method = RequestMethod.GET)
	public Collection<Survey> findAllSurveys() {
		Collection<Survey> result = surveyService.allSurveys();
		return result;
	}

}