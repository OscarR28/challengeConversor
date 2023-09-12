/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.proyectoconversor;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Scanner;
import javax.swing.JOptionPane;
import org.json.JSONObject;

/**
 *
 * @author Oscar Daniel Juarez Ortiz
 */
public class ProyectoConversor {
    
    static JSONObject jsonObject;
    static double amount;
    static BigDecimal finalAmount;
    static String fromCurrency;
    static String fromCurrencyName;
    static String toCurrency ;
    static String toCurrencyName;
    
    static double temperatureAmount;
    static String fromTemperature;
    static String toTemperature;

    public static void main(String[] args) {
        
        boolean flag = true;
        while (flag){
            String options = (JOptionPane.showInputDialog(null, "Seleccione una opción de conversión", "Menu", JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Conversor de Moneda", "Conversor de Temperatura"}, "Selection")).toString();

            switch (options) {

                case "Conversor de Moneda" -> {
                    String currencyOptions = (JOptionPane.showInputDialog(null, "Seleccione la moneda a la que desee convertir", "Menu", JOptionPane.QUESTION_MESSAGE, null, GetTranslatedCurrenciesArray(), "Selection")).toString();
                    SetCurrenciesData(currencyOptions);

                    String input = JOptionPane.showInputDialog("Ingrese la cantidad de " + fromCurrencyName + " que desea convertir a " + toCurrencyName);
                    if (!ValidateNum(input)) {
                        JOptionPane.showMessageDialog(null, "Valor Invalido!");
                    } else {
                        amount = Double.parseDouble(input);
                        ConnectToAPI();
                        JOptionPane.showMessageDialog(null, amount + " " + fromCurrencyName + " equivalen a " + finalAmount + " " + toCurrencyName);
                    }
                }
                case "Conversor de Temperatura" -> {
                    String temperatureOptions = (JOptionPane.showInputDialog(null, "Seleccione el tipo de conversion", "Menu", JOptionPane.QUESTION_MESSAGE, null, GetTemperatureOptions(), "Seleccion")).toString();
                    SetTemperatureData(temperatureOptions);
                    
                    String input = JOptionPane.showInputDialog("Ingrese la cantidad de " + fromTemperature + " que desea convertir a " + toTemperature);
                    if (!ValidateNum(input)) {
                        JOptionPane.showMessageDialog(null, "Valor Invalido!");
                    } else {
                        Double validation = Double.valueOf(input);
                        temperatureAmount = ConvertTemperature(temperatureOptions, validation);
                        JOptionPane.showMessageDialog(null, validation + " " + fromTemperature + " equivalen a " + temperatureAmount + " " + toTemperature);
                    }
                }
            }
            
            int answer = JOptionPane.showConfirmDialog(null, "¿Desea hacer otra conversión?");
            if (JOptionPane.OK_OPTION != answer){
                flag = false;
                JOptionPane.showMessageDialog(null, "Terminando programa.");
            }
        }
        
        

        System.out.println("DevCheck!");
    }

    private static Object[] GetTranslatedCurrenciesArray() {
        return new Object[]{
            "Pesos Mexicanos a Dolares Australianos",
            "Pesos Mexicanos a Dolares Canadienses",
            "Pesos Mexicanos a Euros",
            "Pesos Mexicanos a Yenes",
            "Pesos Mexicanos a Dolares Estadounidenses",
            "Dolares Australianos a Pesos Mexianos",
            "Dolares Canadienses a Pesos Mexianos",
            "Euros a Pesos Mexianos",
            "Yenes a Pesos Mexianos",
            "Dolares Estadounidenses a Pesos Mexianos"
        };
    }
    
    private static void SetCurrenciesData(String dataValue){
        switch (dataValue){
            case "Pesos Mexicanos a Dolares Australianos" -> {
                fromCurrency = "MXN";
                fromCurrencyName = "Pesos Mexicanos";
                toCurrency = "AUD";
                toCurrencyName = "Dolares Australianos";
            }
            case "Pesos Mexicanos a Dolares Canadienses" -> {
                fromCurrency = "MXN";
                fromCurrencyName = "Pesos Mexicanos";
                toCurrency = "CAD";
                toCurrencyName = "Dolares Canadienses";
            }
            case "Pesos Mexicano a Euros" -> {
                fromCurrency = "MXN";
                fromCurrencyName = "Pesos Mexicanos";
                toCurrency = "EUR";
                toCurrencyName = "Euros";
            }
            case "Pesos Mexicanos a Yenes" -> {
                fromCurrency = "MXN";
                fromCurrencyName = "Pesos Mexicanos";
                toCurrency = "JPY";
                toCurrencyName = "Yenes";
            }
            case "Pesos Mexicanos a Dolares Estadounidenses" -> {
                fromCurrency = "MXN";
                fromCurrencyName = "Pesos Mexicanos";
                toCurrency = "USD";
                toCurrencyName = "Dolares Estadounidenses";
            }
            case "Dolares Australianos a Pesos Mexianos" -> {
                fromCurrency = "AUD";
                fromCurrencyName = "Dolares Australianos";
                toCurrency = "MXN";
                toCurrencyName = "Pesos Mexicanos";
            }
            case "Dolares Canadienses a Pesos Mexianos" -> {
                fromCurrency = "CAD";
                fromCurrencyName = "Dolares Canadienses";
                toCurrency = "MXN";
                toCurrencyName = "Pesos Mexicanos";
            }
            case "Euros a Pesos Mexianos" -> {
                fromCurrency = "EUR";
                fromCurrencyName = "Euros";
                toCurrency = "MXN";
                toCurrencyName = "Pesos Mexicanos";
            }
            case "Yenes a Pesos Mexianos" -> {
                fromCurrency = "JPY";
                fromCurrencyName = "Yenes";
                toCurrency = "MXN";
                toCurrencyName = "Pesos Mexicanos";
            }
            case "Dolares Estadounidenses a Pesos Mexianos" -> {
                fromCurrency = "USD";
                fromCurrencyName = "Dolares Estadounidenses";
                toCurrency = "MXN";
                toCurrencyName = "Pesos Mexicanos";
            }
        }
    }
    
    private static void ConnectToAPI(){
        try {
            URL url = new URL("https://api.frankfurter.app/latest?amount=" + amount + "&from=" + fromCurrency + "&to=" + toCurrency);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Ocurrio un error: " + conn.getResponseCode());
            } else {
                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }

                scanner.close();

                //JSONArray jsonArray = new JSONArray(informationString.toString());
                jsonObject = new JSONObject(informationString.toString());
                finalAmount = jsonObject.getJSONObject("rates").getBigDecimal(toCurrency);
                System.out.println(jsonObject.getJSONObject("rates").getBigDecimal(toCurrency));
            }

        } catch (IOException e) {
        }

    }

    private static boolean ValidateNum(String input) {
        try {
            double x = Double.parseDouble(input);
            if (x >= 0 || x < 0);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private static Object[] GetTemperatureOptions(){
        return new Object[]{
            "Celsius a Fahrenheit",
            "Celsius a Kelvin",
            "Farenheit a Celsius",
            "Ferenheit a Kelvin",
            "Kelvin a Celsius",
            "Kelvin a Farenheit"
        };
    }
    
    private static void SetTemperatureData(String temperatureData){
        switch (temperatureData){
            case "Celsius a Fahrenheit" -> {
                fromTemperature = "Celsius";
                toTemperature = "Fahrenheit";
            }
            case "Celsius a Kelvin" -> {
                fromTemperature = "Celsius";
                toTemperature = "Kelvin";
            }
            case "Farenheit a Celsius" -> {
                fromTemperature = "Farenheit";
                toTemperature = "Celsius";
            }
            case "Ferenheit a Kelvin" -> {
                fromTemperature = "Ferenheit";
                toTemperature = "Kelvin";
            }
            case "Kelvin a Celsius" -> {
                fromTemperature = "Kelvin";
                toTemperature = "Celsius";
            }
            case "Kelvin a Farenheit" -> {
                fromTemperature = "Kelvin";
                toTemperature = "Farenheit";
            }
        }
    }
    private static double ConvertTemperature(String temperatureData, double amount){
        switch (temperatureData){
            case "Celsius a Fahrenheit" -> {
                return amount * 1.8 + 32;
            }
            case "Celsius a Kelvin" -> {
                return amount + 273.15;
            }
            case "Farenheit a Celsius" -> {
                return (amount - 32) / 1.8;
            }
            case "Ferenheit a Kelvin" -> {
                return 273.5 + ((amount - 32.0) * (5.0/9.0));
            }
            case "Kelvin a Celsius" -> {
                return amount - 273.15;
            }
            case "Kelvin a Farenheit" -> {
                return 1.8*(amount - 273.15) + 32;
            }
        }
        return 0;
    }
}
