package com.example.G4_DailyReport.controller.admin;

import com.example.G4_DailyReport.model.Department;
import com.example.G4_DailyReport.model.User;
import com.example.G4_DailyReport.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("")
    public String index(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("sort") Optional<String> sort,
            @RequestParam("query") Optional<String> query
    ) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        String sortBy = sort.orElse("id");
        String queryStr = query.orElse("");
        PageRequest pageRequest = PageRequest.of(
                        currentPage - 1,
                        pageSize,
                        Sort.by(sortBy).ascending()
                );

        Page<Department> departmentPage;

        departmentPage = departmentService.findAll(
                queryStr,
                pageRequest
        );

        int totalPages = departmentPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("query", queryStr);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("page", departmentPage);
        model.addAttribute("departments", departmentPage.getContent());
        model.addAttribute("currentPage", currentPage);
        return "admin/departments/index";
    }

    @GetMapping("/new")
    public String add(Model model) {
        return "admin/departments/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable UUID id) {
        model.addAttribute("department", departmentService.findById(id));
        return "admin/departments/edit";
    }

    @GetMapping("/{id}/show")
    public String show(
            Model model,
            @PathVariable UUID id,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size
    ) {
        Pageable pageable = PageRequest.of(
                page.orElse(1) - 1,
                size.orElse(5),
                Sort.by("id").ascending()
        );
        Department department = departmentService.findById(id);
        if(department == null) {
            return "redirect:/admin/error";
        }

        Page<User> managers = departmentService.findManagers(id, pageable);

        model.addAttribute("department", department);
        model.addAttribute("managerPage", managers);
        return "admin/departments/show";
    }

    @RequestMapping(
            value = "/update",
            method = {RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.POST}
    )
    public String update(Model model, Department e) {
        departmentService.update(e);
        return "redirect:/admin/departments";
    }

//    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    @RequestMapping(value = "/{id}")
    public String delete(Model model, @PathVariable("id") UUID id) {
        departmentService.deleteById(id);
        return "redirect:/admin/departments";
    }

    @PostMapping("/create")
    public String create(Model model, Department o) {
        departmentService.save(o);
        return "redirect:/admin/departments";
    }
}
