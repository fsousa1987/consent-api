package com.sensedia.sample.consents.infra.util;

public class LogUtils {

    private LogUtils() {
        // Ei, vc não pode instanciar esse treco =)
    }

    public static String maskCpf(String cpf) {
        if (cpf == null || cpf.length() != 14) return "***";
        return cpf.replaceAll("(\\d{3})\\.(\\d{3})\\.(\\d{3})-(\\d{2})", "***.$2.***-$4");
    }

}
