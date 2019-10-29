package com.codegym.controller;

import com.codegym.model.Cart;
import com.codegym.model.Product;
import com.codegym.model.ProductForm;
import com.codegym.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/product")
@PropertySource("classpath:config_app.properties")
public class ProductController {
    @Autowired
    Environment environment;
    @Autowired
    private ProductService productService;

    @RequestMapping("/list")
    public ModelAndView getAll(@ModelAttribute Pageable pageable) {
        Page<Product> productList = productService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView("/product/list");
        modelAndView.addObject("products", productList);
        return modelAndView;

    }

    @GetMapping("/create")
    public ModelAndView createForm() {
        ModelAndView modelAndView = new ModelAndView("/product/create");
        modelAndView.addObject("productForm", new ProductForm());
        return modelAndView;
    }

    @PostMapping("/save-product")
    public ModelAndView saveProduct(@ModelAttribute ProductForm productForm, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println("Result Error" + result.getAllErrors());
        }
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = environment.getProperty("file_upload").toString();

        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product productObject = new Product(productForm.getName(), productForm.getPrice(), fileName);


        productService.save(productObject);
        ModelAndView modelAndView = new ModelAndView("/product/create");
        modelAndView.addObject("product", new Product());
        return modelAndView;
    }

    @GetMapping("/update/{id}")
    public ModelAndView editForm(@PathVariable Long id) {
        Product product = productService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/product/update");
        modelAndView.addObject("product", product);
        return modelAndView;
    }

    @PostMapping("/update")
    public ModelAndView updateProduct(@ModelAttribute("product") ProductForm productForm) {
        Long id = productForm.getId();
        String name = productForm.getName();
        Double price = productForm.getPrice();
        String fileName = productForm.getImage().getOriginalFilename();
        Product product = new Product(id, name, price, fileName);
        productService.save(product);
        ModelAndView modelAndView = new ModelAndView("/product/update");
        modelAndView.addObject("product", productForm);
        modelAndView.addObject("message", "Updated");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteForm(@PathVariable Long id) {
        Product product = productService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/product/delete");
        modelAndView.addObject("product", product);
        return modelAndView;
    }
    @PostMapping("/delete")
    public ModelAndView deleteProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes){
        productService.remove(product.getId());
        ModelAndView modelAndView = new ModelAndView("redirect:product/list");
        redirectAttributes.addFlashAttribute("message", "deleted");
        return modelAndView;
    }
    @ModelAttribute("cart")
    public Cart cart(){
        return new Cart();
    }
    @GetMapping("/add/{id}")
    public String add(@PathVariable Long id, @ModelAttribute Cart cart,@RequestParam("action") String action){
        Product product = productService.findById(id);
        if(action.equals("show")){
            cart.addItem(product);
            return "redirect:/cart";
        }
        cart.addItem(product);
        return "redirect:/shop";
    }
    @GetMapping("/remove/{id}")
    public String removeItem(@PathVariable Long id,@ModelAttribute Cart cart,@RequestParam("action") String action){
        Product product = productService.findById(id);
        if(action.equals("show")){
            cart.removeItem(product);
            return "redirect:/cart";
        }
        cart.removeItem(product);
        return "redirect:/list";
    }
}
