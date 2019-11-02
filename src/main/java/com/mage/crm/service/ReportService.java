package com.mage.crm.service;

import com.mage.crm.dao.ReportDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ReportService {
    @Resource
    private ReportDao reportDao;
}
