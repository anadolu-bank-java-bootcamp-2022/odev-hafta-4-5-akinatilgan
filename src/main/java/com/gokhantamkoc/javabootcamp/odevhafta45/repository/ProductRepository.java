package com.gokhantamkoc.javabootcamp.odevhafta45.repository;

import com.gokhantamkoc.javabootcamp.odevhafta45.model.Product;
import com.gokhantamkoc.javabootcamp.odevhafta45.util.DatabaseConnection;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ProductRepository {

    DatabaseConnection databaseConnection;

    @Autowired
    public void setDatabaseConnection(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public List<Product> getAll() {
        // BU METHODU 1. GOREV ICIN DOLDURUNUZ
        final String SQL = "SELECT * FROM public.product";
        try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(SQL)) {
            final ResultSet rs = preparedStatement.executeQuery();
            final List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add (new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
            return products;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }

    }

    public Product get(long id) {
        // BU METHODU 1. GOREV ICIN DOLDURUNUZ
        final String SQL = "SELECT * FROM public.product where id = ? limit 1;";
        try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void save(Product product) throws RuntimeException {
        // BU METHODU 1. GOREV ICIN DOLDURUNUZ
        final String SQL = "INSERT INTO public.product(id, name, description) values(?, ?, ?)";
        try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(SQL)) {
            preparedStatement.setLong(1, product.getId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setString(3, product.getDescription());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows <= 0) {
                throw new RuntimeException(String.format("Could not save product %s!", product.getName()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void update(Product product) throws RuntimeException {
        // BU METHODU 1. GOREV ICIN DOLDURUNUZ
        Product foundProduct = this.get(product.getId());
        if (foundProduct != null) {
            final String SQL = "UPDATE public.product set name = ?, description = ? where id = ?";
            try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(SQL)) {
                preparedStatement.setString(1, product.getName());
                preparedStatement.setString(2, product.getDescription());
                preparedStatement.setLong(3, product.getId());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows <= 0) {
                    throw new RuntimeException(String.format("Could not update product %s!", product.getName()));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    // BU METHODU SILMEYINIZ YOKSA TESTLER CALISMAZ
    public void delete(long id) throws RuntimeException {
        Product foundProduct = this.get(id);
        if (foundProduct != null) {
            final String SQL = "delete from public.product where id = ?";
            try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(SQL)) {
                preparedStatement.setLong(1, id);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows <= 0) {
                    throw new RuntimeException(String.format("Could not delete product with id %d!", id));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex.getMessage());
            }
        }
    }
}
