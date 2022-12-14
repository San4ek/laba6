package com.example.laba6;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;

public class Car implements Serializable {
    private String Name;
    private String Model;
    private LocalDate Date;
    private String Color;
    private String VIN;

    public void setName(String name) {
        Name = name;
    }

    public void setModel(String model) {
        Model = model;
    }

    public void setDate(LocalDate date) {
        Date = date;
    }

    public void setColor(String color) {
        Color = color;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public Car(String name, String model, LocalDate date, String color, String VIN) {
        Name = name;
        Model = model;
        Date = date;
        Color = color;
        this.VIN = VIN;
    }

    public String getName() {
        return Name;
    }

    public LocalDate getDate() {
        return Date;
    }

    public String getModel() {
        return Model;
    }

    public String getColor() {
        return Color;
    }

    public String getVIN() {
        return VIN;
    }

    void writeToFile() {
        try (FileWriter out = new FileWriter("DataBase6.txt", true)) {
            String str=Name+"|"+Model+"|"+Date+"|"+Color+"|"+VIN+"\n";
            out.write(str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObservableList<Car> readFromFile() {
        ObservableList<Car> data = FXCollections.observableArrayList();

        File file = new File("DataBase6.txt");
        if (file.exists()) {
            try (BufferedReader bf=new BufferedReader(new FileReader("DataBase6.txt"))) {
                String line;
                while ((line = bf.readLine())!=null) {
                    String[] arr=line.split("\\|");
                    data.add(new Car(arr[0], arr[1], LocalDate.parse(arr[2]), arr[3], arr[4]));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return data;
    }

    void deleteFromFile(ObservableList<Car> data) {
        data.remove(this);
        try {
            Files.newBufferedWriter(Path.of("DataBase6.txt"), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Car car : data) {
            car.writeToFile();
        }
    }

    static void updateFile(ObservableList<Car> data) {
        try {
            Files.newBufferedWriter(Path.of("DataBase6.txt"), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Car car : data) {
            car.writeToFile();
        }
    }
}