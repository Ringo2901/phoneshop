package com.es.core.model.phone;

import com.es.core.model.phone.color.Color;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    public Phone() {
    }

    public Phone(Long id, String brand, String model, BigDecimal price, BigDecimal displaySizeInches, Integer weightGr, BigDecimal lengthMm, BigDecimal widthMm, BigDecimal heightMm, Date announced, String deviceType, String os, Set<Color> colors, String displayResolution, Integer pixelDensity, String displayTechnology, BigDecimal backCameraMegapixels, BigDecimal frontCameraMegapixels, BigDecimal ramGb, BigDecimal internalStorageGb, Integer batteryCapacityMah, BigDecimal talkTimeHours, BigDecimal standByTimeHours, String bluetooth, String positioning, String imageUrl, String description) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.displaySizeInches = displaySizeInches;
        this.weightGr = weightGr;
        this.lengthMm = lengthMm;
        this.widthMm = widthMm;
        this.heightMm = heightMm;
        this.announced = announced;
        this.deviceType = deviceType;
        this.os = os;
        this.colors = colors;
        this.displayResolution = displayResolution;
        this.pixelDensity = pixelDensity;
        this.displayTechnology = displayTechnology;
        this.backCameraMegapixels = backCameraMegapixels;
        this.frontCameraMegapixels = frontCameraMegapixels;
        this.ramGb = ramGb;
        this.internalStorageGb = internalStorageGb;
        this.batteryCapacityMah = batteryCapacityMah;
        this.talkTimeHours = talkTimeHours;
        this.standByTimeHours = standByTimeHours;
        this.bluetooth = bluetooth;
        this.positioning = positioning;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Phone(Long id, String brand, String model, BigDecimal price, String imageUrl) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDisplaySizeInches() {
        return displaySizeInches;
    }

    public void setDisplaySizeInches(BigDecimal displaySizeInches) {
        this.displaySizeInches = displaySizeInches;
    }

    public Integer getWeightGr() {
        return weightGr;
    }

    public void setWeightGr(Integer weightGr) {
        this.weightGr = weightGr;
    }

    public BigDecimal getLengthMm() {
        return lengthMm;
    }

    public void setLengthMm(BigDecimal lengthMm) {
        this.lengthMm = lengthMm;
    }

    public BigDecimal getWidthMm() {
        return widthMm;
    }

    public void setWidthMm(BigDecimal widthMm) {
        this.widthMm = widthMm;
    }

    public BigDecimal getHeightMm() {
        return heightMm;
    }

    public void setHeightMm(BigDecimal heightMm) {
        this.heightMm = heightMm;
    }

    public Date getAnnounced() {
        return announced;
    }

    public void setAnnounced(Date announced) {
        this.announced = announced;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Set<Color> getColors() {
        return colors;
    }

    public void setColors(Set<Color> colors) {
        this.colors = colors;
    }

    public String getDisplayResolution() {
        return displayResolution;
    }

    public void setDisplayResolution(String displayResolution) {
        this.displayResolution = displayResolution;
    }

    public Integer getPixelDensity() {
        return pixelDensity;
    }

    public void setPixelDensity(Integer pixelDensity) {
        this.pixelDensity = pixelDensity;
    }

    public String getDisplayTechnology() {
        return displayTechnology;
    }

    public void setDisplayTechnology(String displayTechnology) {
        this.displayTechnology = displayTechnology;
    }

    public BigDecimal getBackCameraMegapixels() {
        return backCameraMegapixels;
    }

    public void setBackCameraMegapixels(BigDecimal backCameraMegapixels) {
        this.backCameraMegapixels = backCameraMegapixels;
    }

    public BigDecimal getFrontCameraMegapixels() {
        return frontCameraMegapixels;
    }

    public void setFrontCameraMegapixels(BigDecimal frontCameraMegapixels) {
        this.frontCameraMegapixels = frontCameraMegapixels;
    }

    public BigDecimal getRamGb() {
        return ramGb;
    }

    public void setRamGb(BigDecimal ramGb) {
        this.ramGb = ramGb;
    }

    public BigDecimal getInternalStorageGb() {
        return internalStorageGb;
    }

    public void setInternalStorageGb(BigDecimal internalStorageGb) {
        this.internalStorageGb = internalStorageGb;
    }

    public Integer getBatteryCapacityMah() {
        return batteryCapacityMah;
    }

    public void setBatteryCapacityMah(Integer batteryCapacityMah) {
        this.batteryCapacityMah = batteryCapacityMah;
    }

    public BigDecimal getTalkTimeHours() {
        return talkTimeHours;
    }

    public void setTalkTimeHours(BigDecimal talkTimeHours) {
        this.talkTimeHours = talkTimeHours;
    }

    public BigDecimal getStandByTimeHours() {
        return standByTimeHours;
    }

    public void setStandByTimeHours(BigDecimal standByTimeHours) {
        this.standByTimeHours = standByTimeHours;
    }

    public String getBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(String bluetooth) {
        this.bluetooth = bluetooth;
    }

    public String getPositioning() {
        return positioning;
    }

    public void setPositioning(String positioning) {
        this.positioning = positioning;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
