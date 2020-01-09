package com.infomind.axboot.domain.user.auth.menu;

import com.infomind.axboot.domain.program.Program;
import lombok.Data;

import java.util.List;

@Data
public class AuthGroupMenuVO {

    private List<AuthGroupMenu> list;

    private Program program;
}
