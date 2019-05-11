package com.project.mobility.util.server;

import com.project.mobility.model.onboarding.category.Category;
import com.project.mobility.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class ServerUtil {
    public static boolean isServerOnline() {
        return true;
    }

    public static Product createDummyProduct(int categoryId) {
        return createDummyProduct(categoryId, 10);
    }

    public static Product createDummyProduct(int categoryId, int id) {
        Product product = new Product();
        product.setId(id);
        product.setDescription("First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description" +
                "First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description");
        product.setName("First product " + id);
        product.setCategoryId(categoryId);

        List<String> imagesUrlList = new ArrayList<>();
        imagesUrlList.add("https://www.xda-developers.com/files/2018/12/Galaxy-S10-new-renders.jpg");
        imagesUrlList.add("https://www.bhphotovideo.com/images/images1000x1000/google_ga00458_us_pixel_3_64gb_smartphone_1448919.jpg");
        product.setImagesUrl(imagesUrlList);
        product.setPrice(200 + id);
        return product;
    }


    public static List<Product> getDummyProductsList(int categoryId) {
        List<Product> products = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            products.add(createDummyProduct(categoryId, i));
        }

        return products;
    }

    public static List<Category> createDummyCategoryList() {
        List<Category> dummyItems = new ArrayList<>();

        dummyItems.add(new Category(0, "Batteries", "ic_battery"));
        dummyItems.add(new Category(1, "Chargers", "ic_charger"));
        dummyItems.add(new Category(2, "Headsets", "ic_headset"));
        dummyItems.add(new Category(3, "Phones", "ic_phone"));
        return dummyItems;
    }
}
