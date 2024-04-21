package com.example.highhopes.shortlink;

import com.example.highhopes.user.User;
import com.example.highhopes.user.UserRepository;
import com.example.highhopes.utils.CustomCollectors;
import com.example.highhopes.utils.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/shortLinks")
public class ShortLinkController {

    private final ShortLinkService shortLinkService;
    private final UserRepository userRepository;

    public ShortLinkController(final ShortLinkService shortLinkService,
            final UserRepository userRepository) {
        this.shortLinkService = shortLinkService;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userValues", userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getUsername)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("shortLinks", shortLinkService.findAll());
        return "shortLink/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("shortLink") final ShortLinkDTO shortLinkDTO) {
        return "shortLink/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("shortLink") @Valid final ShortLinkCreateRequestDTO shortLinkCreateRequestDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "shortLink/add";
        }
        shortLinkService.create(shortLinkCreateRequestDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("shortLink.create.success"));
        return "redirect:/shortLinks";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("shortLink", shortLinkService.get(id));
        return "shortLink/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("shortLink") @Valid final ShortLinkDTO shortLinkDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "shortLink/edit";
        }
        shortLinkService.update(id, shortLinkDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("shortLink.update.success"));
        return "redirect:/shortLinks";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        shortLinkService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("shortLink.delete.success"));
        return "redirect:/shortLinks";
    }

}
