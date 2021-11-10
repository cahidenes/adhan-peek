package com.cenes.adhanpeek;

import com.batoulapps.adhan.*;
import com.batoulapps.adhan.data.DateComponents;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RemainingCalculator {
    private CalculationParameters calculationParameters;
    private Coordinates coordinates;
    private int maxNegativeRemaining;

    public RemainingCalculator(String method_path, Coordinates coordinates, int maxNegativeRemaining) throws IOException {
        this.coordinates = coordinates;
        this.maxNegativeRemaining = maxNegativeRemaining;

        if (method_path.equals("Turkey")) {
            calculationParameters = new CalculationParameters(19.0, 17.0);
            calculationParameters.adjustments = new PrayerAdjustments(4, -8, 4, 4, 6, 0);
            calculationParameters.madhab = Madhab.SHAFI;
        } else if (method_path.equals("MuslimWorldLeague")) {
            calculationParameters = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
        } else if (method_path.equals("Egyptian")) {
            calculationParameters = CalculationMethod.EGYPTIAN.getParameters();
        } else if (method_path.equals("UmmAlQura")) {
            calculationParameters = CalculationMethod.UMM_AL_QURA.getParameters();
        } else if (method_path.equals("Dubai")) {
            calculationParameters = CalculationMethod.DUBAI.getParameters();
        } else if (method_path.equals("Qatar")) {
            calculationParameters = CalculationMethod.QATAR.getParameters();
        } else if (method_path.equals("Kuwait")) {
            calculationParameters = CalculationMethod.KUWAIT.getParameters();
        } else if (method_path.equals("MoonsightingComittee")) {
            calculationParameters = CalculationMethod.MOON_SIGHTING_COMMITTEE.getParameters();
        } else if (method_path.equals("Singapore")) {
            calculationParameters = CalculationMethod.SINGAPORE.getParameters();
        } else if (method_path.equals("NorthAmerica")) {
            calculationParameters = CalculationMethod.NORTH_AMERICA.getParameters();
        } else {
            // Extract parameters from json
            List<String> params = Files.readAllLines(Paths.get(method_path));

            // Clear comments
            for (int i = params.size()-1; i >= 0; i--) {
                if (params.get(i).contains("#")) {
                    params.set(i, params.get(i).split("#")[0].trim());
                } 
                if (params.get(i).length() == 0) {
                    params.remove(i);
                }
            }

            // Construct CalculationParameters object
            calculationParameters = new CalculationParameters(
                    Double.parseDouble(params.get(0)),
                    Double.parseDouble(params.get(1))
            );
            calculationParameters.highLatitudeRule = getHighLatitudeMode(Integer.parseInt(params.get(3)));
            calculationParameters.madhab = (params.get(4).equals("DOUBLE") ? Madhab.HANAFI : Madhab.SHAFI);
            calculationParameters.ishaInterval = Integer.parseInt(params.get(2));
            calculationParameters.adjustments.fajr = Integer.parseInt(params.get(5));
            calculationParameters.adjustments.sunrise = Integer.parseInt(params.get(6));
            calculationParameters.adjustments.dhuhr = Integer.parseInt(params.get(7));
            calculationParameters.adjustments.asr = Integer.parseInt(params.get(8));
            calculationParameters.adjustments.maghrib = Integer.parseInt(params.get(9));
            calculationParameters.adjustments.isha = Integer.parseInt(params.get(10));
        }
    }

    private HighLatitudeRule getHighLatitudeMode(int a) {
        switch (a) {
            case 0: return HighLatitudeRule.MIDDLE_OF_THE_NIGHT;
            case 1: return HighLatitudeRule.SEVENTH_OF_THE_NIGHT;
            case 2: return HighLatitudeRule.TWILIGHT_ANGLE;
        }
        return null;
    }

    public int calculateRemainingTime() {
        PrayerTimes prayerTimes = new PrayerTimes(coordinates, DateComponents.from(new Date()), calculationParameters);
        long diff;
        try {
            diff = prayerTimes.timeForPrayer(prayerTimes.nextPrayer()).getTime() - new Date().getTime();
        } catch (Exception e) {
            diff = prayerTimes.fajr.getTime() + 86400000L - new Date().getTime();
        }
        long kalan_dakika = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
        diff = new Date().getTime() - prayerTimes.timeForPrayer(prayerTimes.currentPrayer()).getTime();
        long gecen_dakika = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
        if (gecen_dakika <= maxNegativeRemaining) return -(int)gecen_dakika;
        return (int) kalan_dakika;
    }
}
