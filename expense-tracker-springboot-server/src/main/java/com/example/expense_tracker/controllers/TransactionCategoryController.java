package com.example.expense_tracker.controllers;


import com.example.expense_tracker.entities.TransactionCategory;
import com.example.expense_tracker.services.TransactionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/transaction-category")
public class TransactionCategoryController {

    private static final Logger logger = Logger.getLogger(TransactionCategoryController.class.getName());

    @Autowired
    private TransactionCategoryService transactionCategoryService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionCategory>> getAllTransactionCategoriesByUserId(@PathVariable int userId) {
        logger.info("Getting all transaction categories from user: " + userId);
        List<TransactionCategory> transactionCategories = transactionCategoryService.getAllTransactionCategoriesByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(transactionCategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionCategory> getTransactionCategoryById(@PathVariable int id) {
        logger.info("Getting transaction category by id: " + id);

        Optional<TransactionCategory> transactionCategory = transactionCategoryService.getTransactionCategoryById(id);
        if(transactionCategory.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(transactionCategory.get());
    }

    @PostMapping
    public ResponseEntity<TransactionCategory> createTransactionCategory(@RequestBody TransactionCategory transactionCategory){
        logger.info("Create Transaction Category for: " + transactionCategory.getCategoryName());
        transactionCategoryService.createTransactionCategory(
                transactionCategory.getUser().getId(),
                transactionCategory.getCategoryName(),
                transactionCategory.getCategoryColor()
        );

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionCategory> updateTransactionCategoryById(@PathVariable int id,
                                                                             @RequestParam String newCategoryName, @RequestParam String newCategoryColor){
        logger.info("Updating transaction category with id: " + id);

        TransactionCategory updatedTransactionCategory = transactionCategoryService.updateTransactionCategoryById(id,
                newCategoryName, newCategoryColor);
        if(updatedTransactionCategory == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(updatedTransactionCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TransactionCategory> deleteTransactionCategoryById(@PathVariable int id){
        logger.info("Deleting transaction category with id: " + id);

        if(!transactionCategoryService.deleteTransactionCategoryById(id)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
