package com.blog.service.impl;

import com.blog.dto.CategoryDto;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.model.Category;
import com.blog.repository.CategoryRepository;
import com.blog.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatogoryServiceImpl  implements CategoryService {

    private final CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    public CatogoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto){
        Category category = dtoToCategory(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryToDto(savedCategory);
    }


    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category", "Category id",categoryId));
        category.setCategoryTitle(categoryDto.getCategoryTitle());
        category.setCategoryDescription(categoryDto.getCategoryDescription());

        Category save = categoryRepository.save(category);
        return modelMapper.map(save,CategoryDto.class);

    }

    @Override
    public CategoryDto getCategoryById(Integer categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category", "Category id",categoryId));
        return modelMapper.map(category,CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategory(){
        List <Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = categories.stream()
                .map(category -> modelMapper.map(category,CategoryDto.class)).collect(Collectors.toList());
        return categoryDtos;
    }

    @Override
    public void deleteCategory(Integer categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category", "Category id",categoryId));
        categoryRepository.delete(category);
    }

    private Category dtoToCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto ,Category.class);
        return category;
    }

    private CategoryDto categoryToDto(Category category){
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        return categoryDto;


    }

}
