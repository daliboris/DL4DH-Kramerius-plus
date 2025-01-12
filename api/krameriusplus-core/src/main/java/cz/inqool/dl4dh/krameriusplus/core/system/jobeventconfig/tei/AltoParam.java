package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.tei;

import lombok.Getter;

@Getter
public enum AltoParam {
    height("height"),
    width("width"),
    vpos("vpos"),
    hpos("hpos");

    private final String name;

    AltoParam(String name) {
        this.name = name;
    }
}
