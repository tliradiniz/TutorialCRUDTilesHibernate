package com.websystique.springmvc.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.websystique.springmvc.model.Country;
import com.websystique.springmvc.model.CountryJsonResponse;
import com.websystique.springmvc.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;




@Controller
public class AppController {
	
	@Autowired
	CountryService countryService;

	@RequestMapping(value = { "/"}, method = RequestMethod.GET)
	public String homePage(ModelMap model) {
		return "home";
	}

//	@RequestMapping(value = { "/products"}, method = RequestMethod.GET)
//	public String productsPage(ModelMap model) {
//		return "products";
//	}

	@RequestMapping(value = { "/contactus"}, method = RequestMethod.GET)
	public String contactUsPage(ModelMap model) {
		return "contactus";
	}
	
	
	
	@RequestMapping(value = "/getAllCountries", method = RequestMethod.GET, headers = "Accept=application/json")
	public String getCountries(Model model) {
		
		List<Country> listOfCountries = countryService.getAllCountries();
		model.addAttribute("country", new Country());
		model.addAttribute("listOfCountries", listOfCountries);
		return "products";
	}

	@RequestMapping(value = "/getCountry/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public Country getCountryById(@PathVariable int id) {
		return countryService.getCountry(id);
	}

//	@RequestMapping(value = "/addCountry", method = RequestMethod.POST, headers = "Accept=application/json")
//	public String addCountry(@ModelAttribute("country") Country country) {	
//		if(country.getId()==0)
//		{
//			countryService.addCountry(country);
//		}
//		else
//		{	
//			countryService.updateCountry(country);
//		}
//		
//		return "redirect:/getAllCountries";
//	}
	
	#TODO UPDATECOUNTRY/ADDCOUNTRY ESTÁ ERRADO, DEVE MANDAR O ID.
	
	@RequestMapping(value = "/addCountry", method = RequestMethod.POST, headers = "Accept=application/json")
//	@PostMapping(value = "addCountry", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public CountryJsonResponse addCountry(@ModelAttribute @Valid Country country, BindingResult result) {
		
		CountryJsonResponse respone = new CountryJsonResponse();
		
		if(country.getId()==0)
		{	
			if(result.hasErrors()){
		         
		         //Get error message
		         Map<String, String> errors = result.getFieldErrors().stream()
		               .collect(
		                     Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
		                 );
		         
		         respone.setValidated(false);
		         respone.setErrorMessages(errors);
		         System.out.println("--------------------------"+respone.getErrorMessages()+"MENSAGENS DE ERRO");
		         return respone;
		      }else{
		         // Implement business logic to save employee into database
		         //..
		         respone.setValidated(true);
		         respone.setCountry(country);
		         countryService.addCountry(country);
		         System.out.println("VALIDADO");
		         return respone;
		      }			
		}
		else
		{	
			if(result.hasErrors()){
		         System.out.println("CHEGOU NO UPDATE ERROR!!");
		         //Get error message
		         Map<String, String> errors = result.getFieldErrors().stream()
		               .collect(
		                     Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
		                 );
		         
		         respone.setValidated(false);
		         respone.setErrorMessages(errors);
		         System.out.println("--------------------------"+respone.getErrorMessages()+"MENSAGENS DE ERRO");
		         return respone;
		      }else{
		         // Implement business logic to save employee into database
		         //..
		    	  System.out.println("CHEGOU NO UPDATE ERROR!!");
		         respone.setValidated(true);
		         respone.setCountry(country);
		         countryService.updateCountry(country);
		         System.out.println("VALIDADO UPDATE");
		         return respone;
		      }
			
		}
		
	     
	}

	@RequestMapping(value = "/updateCountry/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public String updateCountry(@PathVariable("id") int id,Model model) {
		 model.addAttribute("country", this.countryService.getCountry(id));
	        model.addAttribute("listOfCountries", this.countryService.getAllCountries());
	        return "products";
	}

	@RequestMapping(value = "/deleteCountry/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public String deleteCountry(@PathVariable("id") int id) {
		countryService.deleteCountry(id);
		 return "redirect:/getAllCountries";

	}	
}