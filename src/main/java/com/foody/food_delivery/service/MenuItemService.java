package com.foody.food_delivery.service;

import com.foody.food_delivery.domain.MenuItem;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MenuItemService {

    public void updateMenuItemAvailability(MenuItem menuItem) {
        LocalTime currentTime = LocalTime.now();
        String openingHours = menuItem.getRestaurant().getOpeningHours();
        
        boolean restaurantOpen = isRestaurantOpen(currentTime, openingHours);
        menuItem.setAvailable(restaurantOpen);
    }
    
    private boolean isRestaurantOpen(LocalTime currentTime, String openingHours) {
        if (openingHours == null || openingHours.isEmpty()) {
            return true;
        }
        
        try {
            Pattern pattern = Pattern.compile("(\\d{1,2}):(\\d{2})\\s*(AM|PM)\\s*-\\s*(\\d{1,2}):(\\d{2})\\s*(AM|PM)");
            Matcher matcher = pattern.matcher(openingHours.toUpperCase());
            
            if (matcher.find()) {
                int openHour = Integer.parseInt(matcher.group(1));
                int openMinute = Integer.parseInt(matcher.group(2));
                String openAmPm = matcher.group(3);
                int closeHour = Integer.parseInt(matcher.group(4));
                int closeMinute = Integer.parseInt(matcher.group(5));
                String closeAmPm = matcher.group(6);
                
                LocalTime openTime = convertTo24Hour(openHour, openMinute, openAmPm);
                LocalTime closeTime = convertTo24Hour(closeHour, closeMinute, closeAmPm);
                
                return !currentTime.isBefore(openTime) && !currentTime.isAfter(closeTime);
            }
        } catch (Exception e) {
            return true;
        }
        return true;
    }
    
    private LocalTime convertTo24Hour(int hour, int minute, String amPm) {
        if (amPm.equals("AM")) {
            if (hour == 12) hour = 0;
        } else {
            if (hour != 12) hour += 12;
        }
        return LocalTime.of(hour, minute);
    }
}