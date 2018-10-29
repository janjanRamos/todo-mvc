package com.example.demo.controller;

import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Todo;
import com.example.demo.repository.TodoRepository;

@Controller
public class TodoController {

	@Autowired
	private TodoRepository todoRepository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(Model model) {
		model.addAttribute("todo", new Todo());
		ModelAndView modelAndView = new ModelAndView("index");
		List<Todo> todos = todoRepository.findAll();
		modelAndView.addObject("todos", todos);
		return modelAndView;
	}

	@RequestMapping(value = "/todo", method = RequestMethod.POST)
	public String create(Todo todo) {
		try {
		todoRepository.save(todo);
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());		}
		return "redirect:/";
	}

	@RequestMapping(value = "/delete/{id}")
	public String delete(@PathVariable("id") Integer id,  RedirectAttributes redirectAttributes) {
		try {
		Todo todo = todoRepository.findById(id).get();
		todoRepository.delete(todo);
		redirectAttributes.addFlashAttribute("success", MSG_SUCESS_DELETE);
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", MSG_ERROR);
			throw new ServiceException(e.getMessage());}
		return "redirect:/";
	}

	@RequestMapping(value = "/done", method = RequestMethod.POST)
	@ResponseBody
	public String done(@RequestParam Integer id) {
		Todo todo = todoRepository.findById(id).get();
		todo.setDone(todo.isDone() ? false : true);
		todoRepository.save(todo);
		return "redirect:/";
	}

	private static final String MSG_SUCESS_INSERT = "Todo inserted successfully.";
	private static final String MSG_SUCESS_DELETE = "Todo Student successfully.";
	private static final String MSG_ERROR = "Error.";

}
