package com.springboot.blog.controllers;

import com.springboot.blog.payloads.CategoryDto;
import com.springboot.blog.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //Build add category rest Api
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto){
        CategoryDto savedCategoryDto = categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(savedCategoryDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable(value = "id") Long categoryId){
        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(categoryDto);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,@PathVariable(value = "id") Long categoryId){
        return ResponseEntity.ok(categoryService.updateCategory(categoryDto,categoryId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long categoryId){
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>("Category is deleted successfully",HttpStatus.OK);
    }



}
