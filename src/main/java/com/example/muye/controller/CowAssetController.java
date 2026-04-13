package com.example.muye.controller;

import com.example.muye.entity.CowAsset;
import com.example.muye.service.CowAssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cow") // 前端访问的路径前缀
@CrossOrigin // 允许跨域，方便前端兄弟调用
public class CowAssetController {
    @Autowired
    private CowAssetService cowService;

    // 增
    @PostMapping("/add")
    public String add(@RequestBody CowAsset cow) {
        cowService.addNewCow(cow);
        return "添加成功！";
    }

    // 查单条
    @GetMapping("/{id}")
    public CowAsset getOne(@PathVariable Long id) {
        return cowService.getCowById(id);
    }

    // 查全部
    @GetMapping("/list")
    public List<CowAsset> list() {
        return cowService.getAllCows();
    }

    // 改
    @PutMapping("/update")
    public String update(@RequestBody CowAsset cow) {
        cowService.updateCow(cow);
        return "更新成功！";
    }

    // 删
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        cowService.deleteCow(id);
        return "删除成功！";
    }
}
