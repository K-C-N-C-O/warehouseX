package com.dyson.warehouseX.manager.controller;


import com.dyson.model.dto.product.ProductDto;
import com.dyson.model.entity.product.Product;
import com.dyson.model.vo.common.Result;
import com.dyson.model.vo.common.ResultCodeEnum;
import com.dyson.warehouseX.manager.service.ProductService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/product/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    //条件分页查询
    @GetMapping(value = "/{page}/{limit}")
    public Result list(@PathVariable("page") Integer page,
                       @PathVariable("limit") Integer limit,
                       ProductDto productDto){
        PageInfo<Product> pageInfo=productService.findByPage(page,limit,productDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    @PostMapping(value = "/save")
    public Result save(@RequestBody Product product){
        productService.save(product);
        return Result.build(null,ResultCodeEnum.SUCCESS);

    }

    //根据货物id查信息
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable Long id){
        Product product=productService.getById(id);
        return Result.build(product,ResultCodeEnum.SUCCESS);

    }

    //保存修改的数据
    @PostMapping(value = "/updateById")
    public Result update(@RequestBody Product product){
        productService.update(product);
        return Result.build(null,ResultCodeEnum.SUCCESS);

    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Long id) {
        productService.deleteById(id);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    @GetMapping("/updateAuditStatus/{id}/{auditStatus}")
    public Result updateAuditStatus(@PathVariable Long id, @PathVariable Integer auditStatus) {
        productService.updateAuditStatus(id, auditStatus);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        productService.updateStatus(id, status);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }




}
