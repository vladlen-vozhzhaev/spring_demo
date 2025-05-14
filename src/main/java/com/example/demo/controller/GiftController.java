package com.example.demo.controller;

import com.example.demo.entity.Gift;
import com.example.demo.entity.User;
import com.example.demo.repository.GiftRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class GiftController {
    private final GiftRepository giftRepository;
    private final UserRepository userRepository;

    public GiftController(GiftRepository giftRepository, UserRepository userRepository) {
        this.giftRepository = giftRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/hello")
    public String getHello(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            model.addAttribute("username", auth.getName());
        }
        return "hello";
    }

    @GetMapping("/")
    public String showMainPage(){
        return "index";
    }
    @GetMapping("/gifts")
    public String showAllGifts(Model model, Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        List<Gift> gifts = giftRepository.findByUser(user);
        model.addAttribute("gift", new Gift());
        model.addAttribute("gifts", gifts);
        return "gifts-list";
    }
    @GetMapping("/add")
    public String showAddGift(Model model){
        model.addAttribute("gift", new Gift());
        return "add-gift";
    }
    @PostMapping("/add")
    public String addGift(@ModelAttribute Gift gift, Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        gift.setUser(user);
        giftRepository.save(gift);
        return "redirect:/";
    }
    @GetMapping("/gift/{id}")
    public String showSingleGift(@PathVariable Long id, Model model){
        Gift gift = giftRepository.findById(id).orElseThrow();
        model.addAttribute("gift", gift);
        return "gift-details";
    }
    @GetMapping("/edit/{id}")
    public String showEditGift(@PathVariable Long id, Model model){
        Gift gift = giftRepository.findById(id).orElseThrow();
        model.addAttribute("gift", gift);
        return "edit-gift";
    }
    @PostMapping("/edit/{id}")
    public String updateGift(@PathVariable Long id, @ModelAttribute Gift uploadedGift){
        Gift gift = giftRepository.findById(id).orElseThrow();
        gift.setName(uploadedGift.getName());
        gift.setDescription(uploadedGift.getDescription());
        giftRepository.save(gift);
        return "redirect:/gift/"+id;
    }
    @GetMapping("/delete/{id}")
    public String deleteGift(@PathVariable Long id){
        giftRepository.deleteById(id);
        return "redirect:/";
    }
}