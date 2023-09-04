package com.es.core.model.phone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Phone {
    private Long id;
    private String brand;
    private String model;
    private BigDecimal price;

    private BigDecimal displaySizeInches;

    private Integer weightGr;

    private BigDecimal lengthMm;

    private BigDecimal widthMm;

    private BigDecimal heightMm;

    private Date announced;

    private String deviceType;

    private String os;

    private Set<Color> colors = new HashSet<>();

    private String displayResolution;

    private Integer pixelDensity;

    private String displayTechnology;

    private BigDecimal backCameraMegapixels;

    private BigDecimal frontCameraMegapixels;

    private BigDecimal ramGb;

    private BigDecimal internalStorageGb;

    private Integer batteryCapacityMah;

    private BigDecimal talkTimeHours;

    private BigDecimal standByTimeHours;

    private String bluetooth;

    private String positioning;

    private String imageUrl;

    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return id.equals(phone.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
