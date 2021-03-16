package cn.stapx.domain;

import lombok.Data;

/**
 * @Version: 1.0
 * @Date: 2021/3/11 下午 02:39
 * @ClassName: Phome
 * @Author: Stapxs
 * @Description TO DO
 **/

@Data
public class Phone {
    private String brand;
    private Integer price;

    public Phone() {
    }

    public Phone(String brand, Integer price) {
        this.brand = brand;
        this.price = price;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "brand='" + brand + '\'' +
                ", price=" + price +
                '}';
    }
}
