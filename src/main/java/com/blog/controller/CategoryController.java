package com.blog.controller;

import com.blog.dto.ApiResponse;
import com.blog.dto.CategoryDto;
import com.blog.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid  @RequestBody CategoryDto categoryDto){
        return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Integer id){
        return ResponseEntity.ok(categoryService.updateCategory(categoryDto,id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<ApiResponse>(new ApiResponse("category is deleted sucesful",true),HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategory(){
        return  ResponseEntity.ok(categoryService.getAllCategory());
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer id){
        return new ResponseEntity<CategoryDto>(categoryService.getCategoryById(id), HttpStatus.OK);


    }


}
