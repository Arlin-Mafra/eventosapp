package com.eventoapp.eventoapp.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventoapp.eventoapp.models.Convidados;
import com.eventoapp.eventoapp.models.Evento;
import com.eventoapp.eventoapp.repository.ConvidadosRepository;
import com.eventoapp.eventoapp.repository.EventoRepository;

@Controller
public class EventoController {

	@Autowired
	private EventoRepository er;
	@Autowired
	private ConvidadosRepository cr;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "redirect:/eventos";
	}

	@RequestMapping(value = "/cadastrarEvento", method = RequestMethod.GET)
	public String form(Evento evento) {
		ModelAndView mv = new ModelAndView("/cadastrarEvento");
		mv.addObject("evento", evento);
		return "evento/formEvento";
	}

	@RequestMapping(value = "/cadastrarEvento", method = RequestMethod.POST)
	public String form(@Valid Evento evento, final BindingResult result,RedirectAttributes attributes,final ModelMap model ) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("message");
			return "evento/formEvento";
		}
		er.save(evento);
		return "redirect:/cadastrarEvento";
	}

	@RequestMapping("/eventos")
	public ModelAndView listaEventos() {
		ModelAndView mv = new ModelAndView("index");

		Iterable<Evento> eventos = er.findAll();
		mv.addObject("eventos", eventos);
		return mv;

	}

	@GetMapping(value = "/detalhesEvento/{codigo}")
	public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo) {
		ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		Evento evento = er.findByCodigo(codigo);
		mv.addObject("evento", evento);
		Iterable<Convidados> convidados = cr.findByEvento(evento);
		mv.addObject("convidados", convidados);
 
		return mv;

	}

	@PostMapping(value = "/detalhesEvento/{codigo}" )
	public String detalhesEvento(@PathVariable("codigo") long codigo, @Valid Convidados convidados,final ModelMap model,
			final BindingResult result, RedirectAttributes attributes ) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("message");
			attributes.addFlashAttribute("flashType", "alert-danger");
			return "evento/detalhesEvento";
		}
		

		Evento evento = er.findByCodigo(codigo);
		convidados.setEvento(evento);
		cr.save(convidados);
		attributes.addFlashAttribute("flashMessage", "Convidado adicionado com sucesso!"); 
		attributes.addFlashAttribute("flashType", "success");
		return "redirect:/detalhesEvento/{codigo}";

	}
}
