/**
 * 
 */
package forkjoin.action;

import java.util.ArrayList;
import java.util.List;

public class ProductFactory {

	public static List<Product> create(int size) {
		List<Product> products = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			products.add(new Product("P_" + i, 10));
		}
		return products;
	}
}
