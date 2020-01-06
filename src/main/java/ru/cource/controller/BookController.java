package ru.cource.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.cource.model.domain.Author;
import ru.cource.model.domain.Book;
import ru.cource.model.domain.EnumOfGenres;
import ru.cource.model.domain.User;
import ru.cource.model.service.BookShopServiceImpl;
import ru.cource.model.validation.BookValidator;



/**
 * Created by user on 12.11.2019.
 */
@Controller
@RequestMapping("/")
public class BookController {
    @Autowired
    BookShopServiceImpl bookShopService;
    
    @Autowired
    BookValidator bookValidator;
    
    static Set<String> allGenre;
    
    static {
    	 allGenre=Arrays.asList(EnumOfGenres.values()).stream()
    												  .map(en->en.name())
    			                                      .collect(Collectors.toSet());
    }
    
    @GetMapping("/Home")
    public String home(@AuthenticationPrincipal User user,Model model){
    	model.addAttribute("user", user);
        return "HomePage";
    }

    @GetMapping("/getAll")
    public String getAllUsers(Model model){
        model.addAttribute("books",bookShopService.GetAllBook());
        return "Book/AllBooks";
    }


    @GetMapping("/CreateBook")
    //page in which we are creating new book
    //should have all possible GENRES
    public String createBookPage(Model model){
        model.addAttribute("AllGenres",allGenre);
        return "Book/CreateBookPage";
    }

    @PostMapping("/CreateBook")
    //page after which we have book
    //should create complete book and send it to the service
    public String creatingBook(@Valid @ModelAttribute Book book,BindingResult bindingResult,Model model) {
    	bookValidator.validate(book, bindingResult);
    	if(bindingResult.hasErrors()) {
    		//should return our wrong  user and error message to show
    		Map<String,String> FiledErrors=ControllerUtils.getErrors(bindingResult);
    		model.mergeAttributes(FiledErrors);
    		model.addAttribute("AllGenres",allGenre);
    		model.addAttribute("book",book);
    		return "Book/CreateBookPage";
    	}
        bookShopService.createBook(book);
        //book succesfully added redirect to all book
        return "redirect:/getAll";
    }

    @GetMapping("ChangeBook/{book_id}")
    public String changeBookPage(@PathVariable(value = "book_id") int Book_id, Model model){
    
        Book book=bookShopService.getBookById(Book_id);
        model.addAttribute("AllGenres",allGenre);
        model.addAttribute("book",book);
        //send all data to user and wait new value to change the book
        //changing the book with new values
        return "Book/ChangeBookPage";
    }
    @PostMapping("ChangeBook/{book_id}")
    public String changingBook(@Valid @ModelAttribute Book newbook,BindingResult bindingResult,@PathVariable(value = "book_id") int Book_id, Model model){
        Book oldbook=bookShopService.getBookById(Book_id);
        
        bookValidator.validate(newbook, bindingResult,oldbook);
    	if(bindingResult.hasErrors()) {
    		//should return our wrong user and error message to show
    		Map<String,String> FiledErrors=ControllerUtils.getErrors(bindingResult);
    		model.mergeAttributes(FiledErrors);
    		model.addAttribute("AllGenres",allGenre);
    		newbook.setId(Book_id);
    		model.addAttribute("book",newbook);
    		return "Book/ChangeBookPage";
    	}
    	//get old book due to ID,new book is POJO
        oldbook.setAuthors(newbook.getAuthors());
        oldbook.setGenre(newbook.getGenre());
        oldbook.setName(newbook.getName());
        oldbook.setInformation(newbook.getInformation());
        model.addAttribute("book_id",oldbook.getId());
        bookShopService.updateBook(oldbook);
        return "redirect:/getAll";
    }

    @GetMapping("/DeleteBook/{book_id}")
    public String deleteBookPage(@PathVariable(value = "book_id") int Book_id,Model model) {
        //should be here only to redirect POST request with id
        model.addAttribute("Book_id",Book_id);
        return "Book/DeleteBookPage";
    }

    @PostMapping("/DeleteBook/{book_id}")
    public String deletingBook(@RequestParam(value = "descition", required = false) String decision,
                              @PathVariable(value = "book_id") int Book_id) {
        //user have made decision delete or not book
        if(decision.equals("YES")){
            bookShopService.deleteBookById(Book_id);
            return "redirect:/getAll";
        }
        return "redirect:/getAll";
    }
}


