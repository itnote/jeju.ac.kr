package com.infomind.axboot.domain.excel;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface CommonExcelService {
    List excelFileRead(File destFile);
    List<Map<String, String>> setReadOption(File destFile);
}
