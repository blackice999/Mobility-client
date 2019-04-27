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
        Product product = new Product();
        product.setId(10);
        product.setDescription("First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description" +
                "First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description First product description");
        product.setName("First product");
        product.setCategoryId(categoryId);

        List<String> imagesUrlList = new ArrayList<>();
        imagesUrlList.add("https://www.xda-developers.com/files/2018/12/Galaxy-S10-new-renders.jpg");
        imagesUrlList.add("https://www.bhphotovideo.com/images/images1000x1000/google_ga00458_us_pixel_3_64gb_smartphone_1448919.jpg");
        product.setImagesUrl(imagesUrlList);
        product.setPrice(200);
        return product;
    }

    public static List<Product> createDummyProductsList(int categoryId) {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setId(10);
        product.setDescription("First product description");
        product.setName("First product");
        product.setCategoryId(categoryId);

        List<String> imagesUrlList = new ArrayList<>();
        imagesUrlList.add("https://www.xda-developers.com/files/2018/12/Galaxy-S10-new-renders.jpg");
        product.setImagesUrl(imagesUrlList);
        product.setPrice(200);
        products.add(product);
        return products;
    }

    public static List<Category> createDummyCategoryList() {
        List<Category> dummyItems = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            dummyItems.add(new Category(i, "Name " + i, "Image " + i));
        }

        return dummyItems;
    }

}
