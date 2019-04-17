package com.autohome_api.service;

import com.autohome_api.dto.RequestBean;
import com.autohome_api.dto.RequestParams;
import com.autohome_api.dto.ResponseParam;

public interface RealNameService {
    ResponseParam getResponse(RequestBean requestBean);

    ResponseParam getResponse(RequestParams requestParams);
}
