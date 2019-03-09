package org.launchcode.controllers;

import antlr.collections.AST;
import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.CheeseType;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("cheese")
public class CheeseController {

    @Autowired
    private CheeseDao cheeseDao;
    @Autowired
    //private AST categoryDao;
    private CategoryDao categoryDao;

    // Request path: /cheese
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "My Cheeses");

        return "cheese/index";
    }

//    @RequestMapping(value = "add", method = RequestMethod.GET)
//    public String displayAddCheeseForm(Model model) {
//        model.addAttribute("title", "Add Cheese");
//        model.addAttribute(new Cheese());
//        model.addAttribute("cheeseTypes", CheeseType.values());
//        return "cheese/add";

    /*** ADD ***/

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model model) {
        model.addAttribute("title", "Add Cheese");
        model.addAttribute(new Cheese());
        //model.addAttribute("categories", CategoryDao.findAll());
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(@ModelAttribute  @Valid Cheese newCheese,
                                       Errors errors, @RequestParam int categoryId, Model model) {

//
        //Category cat = CategoryDao.findOne(categoryId);
        //Move below
//        Category cat = categoryDao.findOne(categoryId);
//        newCheese.setCategory(cat);
//

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            return "cheese/add";
        }

        Category cat = categoryDao.findOne(categoryId);
        newCheese.setCategory(cat);
        cheeseDao.save(newCheese);

        return "redirect:";
    }

    /*** REMOVE ***/

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] cheeseIds) {

        for (int cheeseId : cheeseIds) {
            cheeseDao.delete(cheeseId);
        }

        return "redirect:";
    }
    // from one-to-many video approx 15:27 into it
    @RequestMapping(value = "category", method = RequestMethod.GET)
    public String category(Model model, @RequestParam int id) {
        //Category cat = CategoryDao.findOne(id);
        Category cat = categoryDao.findOne(id);
        List<Cheese> cheeses = cat.getCheeses();
        model.addAttribute("cheeses", cheeses);
        model.addAttribute("title", "Cheeses in Category: " + cat.getName());
        return "cheese/index";

    }

    /*** --- NOT WORKING AT THIS TIME --- PART 2 BONUS MISSION   ***/

    @RequestMapping(value = "cheese/category/{categoryId}", method = RequestMethod.GET)
    public String viewCategories(Model model, @PathVariable int id) {
        Category cat = categoryDao.findOne(id);
        List<Cheese> cheeses = cat.getCheeses();
        model.addAttribute("title", cat.getName());
        model.addAttribute("category", cat);
        model.addAttribute("categoryId", cat.getId());
        return "cheese/index";
    }




}