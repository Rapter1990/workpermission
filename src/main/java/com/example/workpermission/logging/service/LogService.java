package com.example.workpermission.logging.service;

import com.example.workpermission.logging.entity.LogEntity;

public interface LogService {

    void saveLogToDatabase(final LogEntity logEntity);

}
