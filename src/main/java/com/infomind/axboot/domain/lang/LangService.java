package com.infomind.axboot.domain.lang;

import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Service;
import com.infomind.axboot.domain.BaseService;
import javax.inject.Inject;
import com.chequer.axboot.core.parameter.RequestParams;
import java.util.List;

@Service
public class LangService extends BaseService<Lang, Long> {
    private LangRepository langRepository;

    @Inject
    public LangService(LangRepository langRepository) {
        super(langRepository);
        this.langRepository = langRepository;
    }

    public List<Lang> findByLangKey(Long langKey) {
        return langRepository.findByLangKey(langKey);
    }


    public List<Lang> gets(RequestParams requestParams) {
        String langKey = requestParams.getString("langKey", "");
        String filter = requestParams.getString("filter", "");

        BooleanBuilder builder = new BooleanBuilder();

        if (isNotEmpty(langKey)) {
            builder.and(qLang.langKey.eq(Long.valueOf(langKey)));
        }

        List<Lang> list = select().from(qLang).where(builder).orderBy(qLang.langKey.asc()).fetch();

        if (isNotEmpty(filter)) {
            list = filter(list, filter);
        }

        return list;
    }

}