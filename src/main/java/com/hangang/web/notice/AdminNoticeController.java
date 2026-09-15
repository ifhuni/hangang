package com.hangang.web.notice;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/notices")
public class AdminNoticeController {

    private final NoticeService noticeService;

    public AdminNoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("notices", noticeService.listForAdmin());
        return "admin/notice-list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("noticeForm", new NoticeForm());
        return "admin/notice-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute NoticeForm noticeForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/notice-form";
        }
        noticeService.createNotice(noticeForm);
        return "redirect:/admin/notices";
    }

    @GetMapping("/{noticeId}/edit")
    public String editForm(@PathVariable Long noticeId, Model model) {
        Notice notice = noticeService.getNotice(noticeId);

        NoticeForm form = new NoticeForm();
        form.setTitle(notice.getTitle());
        form.setContent(notice.getContent());
        form.setPinned(notice.isPinned());
        form.setPublishDate(notice.getPublishDate());

        model.addAttribute("noticeForm", form);
        model.addAttribute("noticeId", noticeId);
        return "admin/notice-form";
    }

    @PostMapping("/{noticeId}")
    public String update(@PathVariable Long noticeId,
                          @Valid @ModelAttribute NoticeForm noticeForm,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("noticeId", noticeId);
            return "admin/notice-form";
        }
        noticeService.updateNotice(noticeId, noticeForm);
        return "redirect:/admin/notices";
    }

    @PostMapping("/{noticeId}/hide")
    public String hide(@PathVariable Long noticeId) {
        noticeService.setHidden(noticeId, true);
        return "redirect:/admin/notices";
    }

    @PostMapping("/{noticeId}/show")
    public String show(@PathVariable Long noticeId) {
        noticeService.setHidden(noticeId, false);
        return "redirect:/admin/notices";
    }
}
