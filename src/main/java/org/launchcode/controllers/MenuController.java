package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")
public class MenuController {


    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    //private Menu menu;

    @RequestMapping(value = "")
    public String index(Model model){
        model.addAttribute("title", "Menus");
        model.addAttribute("menus", menuDao.findAll());

        return "menu/index";
    }
// changed to match video code
//    @RequestMapping(value = "add", method = RequestMethod.GET)
//    public String displayAddMenuForm(Model model){
//        model.addAttribute("title", "Add Menu");
//        model.addAttribute(new Menu());
//
//        return "menu/add";
//    }
    // changed to match video code
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model){
        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());

        return "menu/add";
    }

    // changed to match video code
//    @RequestMapping(value = "add", method = RequestMethod.POST)
//    public String processAddMenuForm(@ModelAttribute @Valid Menu menu, Errors errors, Model model){
//        if (errors.hasErrors()){
//            model.addAttribute("title", "Add Menu");
//            return "menu/add";
//        }
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@ModelAttribute @Valid Menu menu, Errors errors, Model model){
        if (errors.hasErrors()){
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(menu);
        return "redirect:view/" + menu.getId();

        //return "redirect:" ;
    }


    @RequestMapping(value = "view/{menuId}", method = RequestMethod.GET)
    public String viewMenu(@PathVariable("menuId") int menuId, Model model){
        Menu menu = menuDao.findOne(menuId);
        model.addAttribute("title", menu.getName() ); // in temp 20190212 10:32
        model.addAttribute("menu", menu);
        model.addAttribute("menuId", menu.getId());

        return "menu/view";
        //return "redirect:menu/view/" + menu.getId();
    }
    @RequestMapping(value = "add-item/{menuId}", method = RequestMethod.GET)
    //public String addItem(@PathVariable("menuId") int menuId, Model model){
    public String addItem(Model model, @PathVariable int menuId){
        Menu menu = menuDao.findOne(menuId);
        AddMenuItemForm form = new AddMenuItemForm(menu, cheeseDao.findAll());
        model.addAttribute("form", form);
        model.addAttribute("title", "Add item to " + menu.getName());
        return "menu/add-item";
    }

    @RequestMapping(value = "add-item", method = RequestMethod.POST)
    //public String addItem(@ModelAttribute @Valid AddMenuItemForm form, Errors errors, Model model){
    public String addItem(Model model, @ModelAttribute @Valid AddMenuItemForm form, Errors errors){
        if(errors.hasErrors()){
            model.addAttribute("form", form);
            model.addAttribute("title", "Add item to " + form.getMenu().getName());
            return"menu/add-item";
        }
        Cheese cheese = cheeseDao.findOne(form.getCheeseId());
        Menu menu = menuDao.findOne(form.getMenuId());
        //Cheese cheese = cheeseDao.findOne(form.getCheeseId());
        menu.addItem(cheese);
        menuDao.save(menu);
        return "redirect:/menu/view/" + menu.getId();
        //return "redirect:/menu/view/1";
        //return "redirect:/";
    }

}