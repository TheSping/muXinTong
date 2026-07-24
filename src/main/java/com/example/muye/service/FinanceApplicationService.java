package com.example.muye.service;

import com.example.muye.entity.FinanceApplication;
import com.example.muye.mapper.FinanceApplicationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 融资申请管理服务
 */
@Service
public class FinanceApplicationService {
    @Autowired
    private FinanceApplicationMapper financeMapper;

    public FinanceApplication insert(FinanceApplication app) {
        financeMapper.insert(app);
        return app;
    }

    public FinanceApplication getById(Long id) {
        return financeMapper.selectById(id);
    }

    public List<FinanceApplication> getAll() {
        return financeMapper.selectList(null);
    }

    public void update(FinanceApplication app) {
        financeMapper.updateById(app);
    }

    public void delete(Long id) {
        financeMapper.deleteById(id);
    }
}