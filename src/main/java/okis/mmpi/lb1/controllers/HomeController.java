package okis.mmpi.lb1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import okis.mmpi.lb1.Statistic;

@Controller
@RequestMapping("/")
public class HomeController {
	@GetMapping
	public ModelAndView getHomePage() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
		
		mv.addObject("min", 0.0);   
		mv.addObject("max", 51.2); 
		mv.addObject("delta", 0.2); 
		mv.addObject("n", 2); 
		
		return mv;
	}
	
	@PostMapping
	public ModelAndView getCulcPage(
			@RequestParam(name = "min") Double min,
			@RequestParam(name = "max") Double max,
			@RequestParam(name = "delta") Double delta,
			@RequestParam(name = "n") int n) {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
		
		Statistic stat = new Statistic(min, max, delta, n);
		String excelFile = stat.toExcelFile();
		
		mv.addObject("min", min);   
		mv.addObject("max", max); 
		mv.addObject("delta", delta); 
		mv.addObject("n", n); 
		
		mv.addObject("nodes", stat.getNodes());
		
		mv.addObject("excelFilePath", "files/"+excelFile); 
		
		return mv;
	}
}
