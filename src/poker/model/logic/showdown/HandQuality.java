package poker.model.logic.showdown;

import java.util.HashMap;

import poker.model.Card;

/**
 * This class provides a method to determine propabilities for different hands.
 * 
 * @author Becker
 */
public class HandQuality {

    private static final HashMap<String, double[]> qualities = new HashMap<String, double[]>();
    static {
        qualities.put("AA", new double[] { 85.3, 73.4, 63.9, 55.9, 49.2, 43.6, 38.8, 34.7, 31.1 });
        qualities.put("AKs", new double[] { 67.0, 50.7, 41.4, 35.4, 31.1, 27.7, 25.0, 22.7, 20.7 });
        qualities.put("AKo", new double[] { 65.4, 48.2, 38.6, 32.4, 27.9, 24.4, 21.6, 19.2, 17.2 });
        qualities.put("AQs", new double[] { 66.1, 49.4, 39.9, 33.7, 29.4, 26.0, 23.3, 21.1, 19.3 });
        qualities.put("AQo", new double[] { 64.5, 46.8, 36.9, 30.4, 25.9, 22.5, 19.7, 17.5, 15.5 });
        qualities.put("AJs", new double[] { 65.4, 48.2, 38.5, 32.2, 27.8, 24.5, 22.0, 19.9, 18.1 });
        qualities.put("AJo", new double[] { 63.6, 45.6, 35.4, 28.9, 24.4, 21.0, 18.3, 16.1, 14.3 });
        qualities.put("ATs", new double[] { 64.7, 47.1, 37.2, 31.0, 26.7, 23.5, 21.0, 18.9, 17.3 });
        qualities.put("ATo", new double[] { 62.9, 44.4, 34.1, 27.6, 23.1, 19.8, 17.2, 15.1, 13.4 });
        qualities.put("A9s", new double[] { 63.0, 44.8, 34.6, 28.4, 24.2, 21.1, 18.8, 16.9, 15.4 });
        qualities.put("A9o", new double[] { 60.9, 41.8, 31.2, 24.7, 20.3, 17.1, 14.7, 12.8, 11.2 });
        qualities.put("A8s", new double[] { 62.1, 43.7, 33.6, 27.4, 23.3, 20.3, 18.0, 16.2, 14.8 });
        qualities.put("A8o", new double[] { 60.1, 40.8, 30.1, 23.7, 19.4, 16.2, 13.9, 12.0, 10.6 });
        qualities.put("A7s", new double[] { 61.1, 42.6, 32.6, 26.5, 22.5, 19.6, 17.4, 15.7, 14.3 });
        qualities.put("A7o", new double[] { 59.1, 39.4, 28.9, 22.6, 18.4, 15.4, 13.2, 11.4, 10.1 });
        qualities.put("A6s", new double[] { 60.0, 41.3, 31.4, 25.6, 21.7, 19.0, 16.9, 15.3, 14.0 });
        qualities.put("A6o", new double[] { 57.8, 38.0, 27.6, 21.5, 17.5, 14.7, 12.6, 10.9, 9.6 });
        qualities.put("A5s", new double[] { 59.9, 41.4, 31.8, 26.0, 22.2, 19.6, 17.5, 15.9, 14.5 });
        qualities.put("A5o", new double[] { 57.7, 38.2, 27.9, 22.0, 18.0, 15.2, 13.1, 11.5, 10.1 });
        qualities.put("A4s", new double[] { 58.9, 40.4, 30.9, 25.3, 21.6, 19.0, 17.0, 15.5, 14.2 });
        qualities.put("A4o", new double[] { 56.4, 36.9, 26.9, 21.1, 17.3, 14.7, 12.6, 11.0, 9.8 });
        qualities.put("A3s", new double[] { 58.0, 39.4, 30.0, 24.6, 21.0, 18.5, 16.6, 15.1, 13.9 });
        qualities.put("A3o", new double[] { 55.6, 35.9, 26.1, 20.4, 16.7, 14.2, 12.2, 10.7, 9.5 });
        qualities.put("A2s", new double[] { 57.0, 38.5, 29.2, 23.9, 20.4, 18.0, 16.1, 14.6, 13.4 });
        qualities.put("A2o", new double[] { 54.6, 35.0, 25.2, 19.6, 16.1, 13.6, 11.7, 10.2, 9.1 });

        qualities.put("KK", new double[] { 82.4, 68.9, 58.2, 49.8, 43.0, 37.5, 32.9, 29.2, 26.1 });
        qualities.put("KQs", new double[] { 63.4, 47.1, 38.2, 32.5, 28.3, 25.1, 22.5, 20.4, 18.6 });
        qualities.put("KQo", new double[] { 61.4, 44.4, 35.2, 29.3, 25.1, 21.8, 19.1, 16.9, 15.1 });
        qualities.put("KJs", new double[] { 62.6, 45.9, 36.8, 31.1, 26.9, 23.8, 21.3, 19.3, 17.6 });
        qualities.put("KJo", new double[] { 60.6, 43.1, 33.6, 27.6, 23.5, 20.2, 17.7, 15.6, 13.9 });
        qualities.put("KTs", new double[] { 61.9, 44.9, 35.7, 29.9, 25.8, 22.8, 20.4, 18.5, 16.9 });
        qualities.put("KTo", new double[] { 59.9, 42.0, 32.5, 26.5, 22.3, 19.2, 16.7, 14.7, 13.1 });
        qualities.put("K9s", new double[] { 60.0, 42.4, 32.9, 27.2, 23.2, 20.3, 18.1, 16.3, 14.8 });
        qualities.put("K9o", new double[] { 58.0, 39.5, 29.6, 23.6, 19.5, 16.5, 14.1, 12.3, 10.8 });
        qualities.put("K8s", new double[] { 58.5, 40.2, 30.8, 25.1, 21.3, 18.6, 16.5, 14.8, 13.5 });
        qualities.put("K8o", new double[] { 56.3, 37.2, 27.3, 21.4, 17.4, 14.6, 12.5, 10.8, 9.4 });
        qualities.put("K7s", new double[] { 57.8, 39.4, 30.1, 24.5, 20.8, 18.1, 16.0, 14.5, 13.2 });
        qualities.put("K7o", new double[] { 55.4, 36.1, 26.3, 20.5, 16.7, 13.9, 11.8, 10.2, 9.0 });
        qualities.put("K6s", new double[] { 56.8, 38.4, 29.1, 23.7, 20.1, 17.5, 15.6, 14.0, 12.8 });
        qualities.put("K6o", new double[] { 54.3, 35.0, 25.3, 19.7, 16.0, 13.3, 11.3, 9.8, 8.6 });
        qualities.put("K5s", new double[] { 55.8, 37.4, 28.2, 23.0, 19.5, 17.0, 15.2, 13.7, 12.5 });
        qualities.put("K5o", new double[] { 53.3, 34.0, 24.5, 19.0, 15.4, 12.9, 11.0, 9.5, 8.3 });
        qualities.put("K4s", new double[] { 54.7, 36.4, 27.4, 22.3, 19.0, 16.6, 14.8, 13.4, 12.3 });
        qualities.put("K4o", new double[] { 52.1, 32.8, 23.4, 18.1, 14.7, 12.3, 10.5, 9.1, 8.0 });
        qualities.put("K3s", new double[] { 53.8, 35.5, 26.7, 21.7, 18.4, 16.2, 14.5, 13.1, 12.1 });
        qualities.put("K3o", new double[] { 51.2, 31.9, 22.7, 17.6, 14.2, 11.9, 10.2, 8.9, 7.8 });
        qualities.put("K2s", new double[] { 52.9, 34.6, 26.0, 21.2, 18.1, 15.9, 14.3, 13.0, 11.9 });
        qualities.put("K2o", new double[] { 50.2, 30.9, 21.8, 16.9, 13.7, 11.5, 9.8, 8.6, 7.6 });

        qualities.put("QQ", new double[] { 79.9, 64.9, 53.5, 44.7, 37.9, 32.5, 28.3, 24.9, 22.2 });
        qualities.put("QJs", new double[] { 60.3, 44.1, 35.6, 30.1, 26.1, 23.0, 20.7, 18.7, 17.1 });
        qualities.put("QJo", new double[] { 58.2, 41.4, 32.6, 26.9, 22.9, 19.8, 17.3, 15.3, 13.7 });
        qualities.put("QTs", new double[] { 59.5, 43.1, 34.6, 29.1, 25.2, 22.3, 19.9, 18.1, 16.6 });
        qualities.put("QTo", new double[] { 57.4, 40.2, 31.3, 25.7, 21.6, 18.6, 16.3, 14.4, 12.9 });
        qualities.put("Q9s", new double[] { 57.9, 40.7, 31.9, 26.4, 22.5, 19.7, 17.6, 15.9, 14.5 });
        qualities.put("Q9o", new double[] { 55.5, 37.6, 28.5, 22.9, 19.0, 16.1, 13.8, 12.1, 10.7 });
        qualities.put("Q8s", new double[] { 56.2, 38.6, 29.7, 24.4, 20.7, 18.0, 16.0, 14.4, 13.2 });
        qualities.put("Q8o", new double[] { 53.8, 35.4, 26.2, 20.6, 16.9, 14.1, 12.1, 10.5, 9.2 });
        qualities.put("Q7s", new double[] { 54.5, 36.7, 27.9, 22.7, 19.2, 16.7, 14.8, 13.3, 12.1 });
        qualities.put("Q7o", new double[] { 51.9, 33.2, 24.0, 18.6, 15.1, 12.5, 10.6, 9.2, 8.0 });
        qualities.put("Q6s", new double[] { 53.8, 35.8, 27.1, 21.9, 18.5, 16.1, 14.3, 12.9, 11.7 });
        qualities.put("Q6o", new double[] { 51.1, 32.3, 23.2, 17.9, 14.4, 12.0, 10.1, 8.8, 7.6 });
        qualities.put("Q5s", new double[] { 52.9, 34.9, 26.3, 21.4, 18.1, 15.8, 14.1, 12.7, 11.6 });
        qualities.put("Q5o", new double[] { 50.2, 31.3, 22.3, 17.3, 13.9, 11.6, 9.8, 8.5, 7.4 });
        qualities.put("Q4s", new double[] { 51.7, 33.9, 25.5, 20.7, 17.6, 15.4, 13.7, 12.4, 11.3 });
        qualities.put("Q4o", new double[] { 49.0, 30.2, 21.4, 16.4, 13.3, 11.0, 9.4, 8.1, 7.1 });
        qualities.put("Q3s", new double[] { 50.7, 33.0, 24.7, 20.1, 17.0, 14.9, 13.3, 12.1, 11.1 });
        qualities.put("Q3o", new double[] { 47.9, 29.2, 20.7, 15.9, 12.8, 10.7, 9.1, 7.9, 6.9 });
        qualities.put("Q2s", new double[] { 49.9, 32.2, 24.0, 19.5, 16.6, 14.6, 13.1, 11.9, 10.9 });
        qualities.put("Q2o", new double[] { 47.0, 28.4, 19.9, 15.3, 12.3, 10.3, 8.8, 7.7, 6.8 });

        qualities.put("JJ", new double[] { 77.5, 61.2, 49.2, 40.3, 33.6, 28.5, 24.6, 21.6, 19.3 });
        qualities.put("JTs", new double[] { 57.5, 41.9, 33.8, 28.5, 24.7, 21.9, 19.7, 17.9, 16.5 });
        qualities.put("JTo", new double[] { 55.4, 39.0, 30.7, 25.3, 21.5, 18.6, 16.3, 14.5, 13.1 });
        qualities.put("J9s", new double[] { 55.8, 39.6, 31.3, 26.1, 22.4, 19.7, 17.6, 15.9, 14.6 });
        qualities.put("J9o", new double[] { 53.4, 36.5, 27.9, 22.5, 18.7, 15.9, 13.8, 12.1, 10.8 });
        qualities.put("J8s", new double[] { 54.2, 37.5, 29.1, 24.0, 20.5, 17.9, 15.9, 14.4, 13.2 });
        qualities.put("J8o", new double[] { 51.7, 34.2, 25.6, 20.4, 16.8, 14.1, 12.2, 10.7, 9.5 });
        qualities.put("J7s", new double[] { 52.4, 35.4, 27.1, 22.2, 18.9, 16.4, 14.6, 13.2, 12.0 });
        qualities.put("J7o", new double[] { 49.9, 32.1, 23.5, 18.3, 14.9, 12.4, 10.6, 9.2, 8.1 });
        qualities.put("J6s", new double[] { 50.8, 33.6, 25.4, 20.6, 17.4, 15.2, 13.5, 12.1, 11.1 });
        qualities.put("J6o", new double[] { 47.9, 29.8, 21.4, 16.5, 13.2, 11.0, 9.3, 8.0, 7.0 });
        qualities.put("J5s", new double[] { 50.0, 32.8, 24.7, 20.0, 17.0, 14.7, 13.1, 11.8, 10.8 });
        qualities.put("J5o", new double[] { 47.1, 29.1, 20.7, 15.9, 12.8, 10.6, 8.9, 7.7, 6.7 });
        qualities.put("J4s", new double[] { 49.0, 31.8, 24.0, 19.4, 16.4, 14.3, 12.8, 11.5, 10.6 });
        qualities.put("J4o", new double[] { 46.1, 28.1, 19.9, 15.3, 12.3, 10.2, 8.6, 7.5, 6.5 });
        qualities.put("J3s", new double[] { 47.9, 30.9, 23.2, 18.8, 16.0, 14.0, 12.5, 11.3, 10.4 });
        qualities.put("J3o", new double[] { 45.0, 27.1, 19.1, 14.6, 11.7, 9.8, 8.3, 7.2, 6.3 });
        qualities.put("J2s", new double[] { 47.1, 30.1, 22.6, 18.3, 15.6, 13.7, 12.2, 11.1, 10.2 });
        qualities.put("J2o", new double[] { 44.0, 26.2, 18.4, 14.1, 11.3, 9.4, 8.0, 7.0, 6.2 });

        qualities.put("TT", new double[] { 75.1, 57.7, 45.2, 36.4, 30.0, 25.3, 21.8, 19.2, 17.2 });
        qualities.put("T9s", new double[] { 54.3, 38.9, 31.0, 26.0, 22.5, 19.8, 17.8, 16.2, 14.9 });
        qualities.put("T9o", new double[] { 51.7, 35.7, 27.7, 22.5, 18.9, 16.2, 14.1, 12.6, 11.3 });
        qualities.put("T8s", new double[] { 52.6, 36.9, 29.0, 24.0, 20.6, 18.1, 16.2, 14.8, 13.6 });
        qualities.put("T8o", new double[] { 50.0, 33.6, 25.4, 20.4, 16.9, 14.4, 12.5, 11.0, 9.9 });
        qualities.put("T7s", new double[] { 51.0, 34.9, 27.0, 22.2, 19.0, 16.6, 14.8, 13.5, 12.4 });
        qualities.put("T7o", new double[] { 48.2, 31.4, 23.4, 18.4, 15.1, 12.8, 11.0, 9.7, 8.6 });
        qualities.put("T6s", new double[] { 49.2, 32.8, 25.1, 20.5, 17.4, 15.2, 13.6, 12.3, 11.2 });
        qualities.put("T6o", new double[] { 46.3, 29.2, 21.2, 16.5, 13.4, 11.2, 9.5, 8.3, 7.3 });
        qualities.put("T5s", new double[] { 47.2, 30.8, 23.3, 18.9, 16.0, 13.9, 12.4, 11.2, 10.2 });
        qualities.put("T5o", new double[] { 44.2, 27.1, 19.3, 14.8, 11.9, 9.9, 8.4, 7.2, 6.4 });
        qualities.put("T4s", new double[] { 46.4, 30.1, 22.7, 18.4, 15.6, 13.6, 12.1, 11.0, 10.0 });
        qualities.put("T4o", new double[] { 43.4, 26.4, 18.7, 14.3, 11.5, 9.5, 8.1, 7.0, 6.2 });
        qualities.put("T3s", new double[] { 45.5, 29.3, 22.0, 17.8, 15.1, 13.2, 11.8, 10.7, 9.8 });
        qualities.put("T3o", new double[] { 42.4, 25.5, 18.0, 13.7, 11.0, 9.1, 7.8, 6.8, 6.0 });
        qualities.put("T2s", new double[] { 44.7, 28.5, 21.4, 17.4, 14.8, 13.0, 11.6, 10.5, 9.7 });
        qualities.put("T2o", new double[] { 41.5, 24.7, 17.3, 13.2, 10.6, 8.8, 7.5, 6.6, 5.8 });

        qualities.put("99", new double[] { 72.1, 53.5, 41.1, 32.6, 26.6, 22.4, 19.4, 17.2, 15.6 });
        qualities.put("98s", new double[] { 51.1, 36.0, 28.5, 23.6, 20.2, 17.8, 15.9, 14.5, 13.4 });
        qualities.put("98o", new double[] { 48.4, 32.9, 25.1, 20.1, 16.6, 14.2, 12.3, 10.9, 9.9 });
        qualities.put("97s", new double[] { 49.5, 34.2, 26.8, 22.1, 18.9, 16.6, 14.9, 13.6, 12.5 });
        qualities.put("97o", new double[] { 46.7, 30.9, 23.1, 18.4, 15.1, 12.8, 11.1, 9.8, 8.8 });
        qualities.put("96s", new double[] { 47.7, 32.3, 24.9, 20.4, 17.4, 15.3, 13.7, 12.4, 11.4 });
        qualities.put("96o", new double[] { 44.9, 28.8, 21.2, 16.6, 13.5, 11.4, 9.8, 8.7, 7.8 });
        qualities.put("95s", new double[] { 45.9, 30.4, 23.2, 18.8, 16.0, 13.9, 12.4, 11.3, 10.3 });
        qualities.put("95o", new double[] { 42.9, 26.7, 19.2, 14.8, 12.0, 10.0, 8.5, 7.4, 6.6 });
        qualities.put("94s", new double[] { 43.8, 28.4, 21.3, 17.3, 14.6, 12.7, 11.3, 10.3, 9.4 });
        qualities.put("94o", new double[] { 40.7, 24.6, 17.3, 13.2, 10.5, 8.7, 7.3, 6.4, 5.6 });
        qualities.put("93s", new double[] { 43.2, 27.8, 20.8, 16.8, 14.3, 12.5, 11.1, 10.1, 9.2 });
        qualities.put("93o", new double[] { 39.9, 23.9, 16.7, 12.7, 10.1, 8.3, 7.1, 6.1, 5.4 });
        qualities.put("92s", new double[] { 42.3, 27.0, 20.2, 16.4, 13.9, 12.2, 10.9, 9.9, 9.1 });
        qualities.put("92o", new double[] { 38.9, 22.9, 16.0, 12.1, 9.6, 8.0, 6.8, 5.9, 5.2 });

        qualities.put("88", new double[] { 69.1, 49.9, 37.5, 29.4, 24.0, 20.3, 17.7, 15.8, 14.4 });
        qualities.put("87s", new double[] { 48.2, 33.9, 26.6, 22.0, 18.9, 16.7, 15.0, 13.7, 12.7 });
        qualities.put("87o", new double[] { 45.5, 30.6, 23.2, 18.5, 15.4, 13.1, 11.5, 10.3, 9.3 });
        qualities.put("86s", new double[] { 46.5, 32.0, 25.0, 20.6, 17.6, 15.6, 14.1, 12.9, 11.9 });
        qualities.put("86o", new double[] { 43.6, 28.6, 21.3, 16.9, 13.9, 11.8, 10.4, 9.2, 8.3 });
        qualities.put("85s", new double[] { 44.8, 30.2, 23.2, 19.1, 16.3, 14.3, 12.9, 11.8, 10.9 });
        qualities.put("85o", new double[] { 41.7, 26.5, 19.4, 15.2, 12.4, 10.5, 9.1, 8.1, 7.3 });
        qualities.put("84s", new double[] { 42.7, 28.1, 21.4, 17.4, 14.8, 13.0, 11.7, 10.6, 9.8 });
        qualities.put("84o", new double[] { 39.6, 24.4, 17.5, 13.4, 10.8, 9.0, 7.8, 6.8, 6.1 });
        qualities.put("83s", new double[] { 40.8, 26.3, 19.8, 16.0, 13.6, 11.9, 10.7, 9.7, 8.9 });
        qualities.put("83o", new double[] { 37.5, 22.4, 15.7, 11.9, 9.5, 7.9, 6.7, 5.8, 5.1 });
        qualities.put("82s", new double[] { 40.3, 25.8, 19.4, 15.7, 13.3, 11.7, 10.5, 9.6, 8.8 });
        qualities.put("82o", new double[] { 36.8, 21.7, 15.1, 11.4, 9.1, 7.5, 6.4, 5.6, 4.9 });

        qualities.put("77", new double[] { 66.2, 46.4, 34.4, 26.8, 21.9, 18.6, 16.4, 14.8, 13.7 });
        qualities.put("76s", new double[] { 45.7, 32.0, 25.1, 20.8, 18.0, 15.9, 14.4, 13.2, 12.3 });
        qualities.put("76o", new double[] { 42.7, 28.5, 21.5, 17.1, 14.2, 12.2, 10.8, 9.6, 8.8 });
        qualities.put("75s", new double[] { 43.8, 30.1, 23.4, 19.4, 16.7, 14.8, 13.4, 12.3, 11.4 });
        qualities.put("75o", new double[] { 40.8, 26.5, 19.7, 15.5, 12.8, 11.0, 9.7, 8.7, 7.9 });
        qualities.put("74s", new double[] { 41.8, 28.2, 21.7, 17.9, 15.3, 13.5, 12.2, 11.2, 10.4 });
        qualities.put("74o", new double[] { 38.6, 24.5, 17.9, 13.9, 11.4, 9.7, 8.5, 7.6, 6.8 });
        qualities.put("73s", new double[] { 40.0, 26.3, 20.0, 16.4, 14.0, 12.3, 11.1, 10.1, 9.3 });
        qualities.put("73o", new double[] { 36.6, 22.4, 16.0, 12.3, 9.9, 8.4, 7.2, 6.4, 5.7 });
        qualities.put("72s", new double[] { 38.1, 24.5, 18.4, 15.0, 12.8, 11.2, 10.1, 9.2, 8.5 });
        qualities.put("72o", new double[] { 34.6, 20.4, 14.2, 10.7, 8.6, 7.2, 6.1, 5.4, 4.8 });

        qualities.put("66", new double[] { 63.3, 43.2, 31.5, 24.5, 20.1, 17.3, 15.4, 14.0, 13.1 });
        qualities.put("65s", new double[] { 43.2, 30.2, 23.7, 19.7, 17.0, 15.2, 13.8, 12.7, 11.9 });
        qualities.put("65o", new double[] { 40.1, 26.7, 20.0, 15.9, 13.3, 11.5, 10.2, 9.2, 8.5 });
        qualities.put("64s", new double[] { 41.4, 28.5, 22.1, 18.4, 15.9, 14.2, 12.9, 11.9, 11.1 });
        qualities.put("64o", new double[] { 38.0, 24.7, 18.2, 14.4, 12.0, 10.3, 9.2, 8.3, 7.6 });
        qualities.put("63s", new double[] { 39.4, 26.5, 20.4, 16.8, 14.5, 12.9, 11.7, 10.8, 10.0 });
        qualities.put("63o", new double[] { 35.9, 22.7, 16.4, 12.8, 10.6, 9.1, 8.0, 7.2, 6.5 });
        qualities.put("62s", new double[] { 37.5, 24.8, 18.8, 15.4, 13.3, 11.8, 10.7, 9.8, 9.1 });
        qualities.put("62o", new double[] { 34.0, 20.7, 14.6, 11.2, 9.1, 7.8, 6.8, 6.0, 5.4 });

        qualities.put("55", new double[] { 60.3, 40.1, 28.8, 22.4, 18.5, 16.0, 14.4, 13.2, 12.3 });
        qualities.put("54s", new double[] { 41.1, 28.8, 22.6, 18.9, 16.5, 14.8, 13.5, 12.5, 11.7 });
        qualities.put("54o", new double[] { 37.9, 25.2, 18.8, 15.0, 12.6, 11.0, 9.8, 8.9, 8.2 });
        qualities.put("53s", new double[] { 39.3, 27.1, 21.1, 17.5, 15.2, 13.7, 12.5, 11.6, 10.8 });
        qualities.put("53o", new double[] { 35.8, 23.3, 17.1, 13.6, 11.4, 9.9, 8.8, 8.0, 7.3 });
        qualities.put("52s", new double[] { 37.5, 25.3, 19.5, 16.1, 14.0, 12.5, 11.4, 10.6, 9.8 });
        qualities.put("52o", new double[] { 33.9, 21.3, 15.3, 12.0, 10.0, 8.6, 7.6, 6.8, 6.2 });

        qualities.put("44", new double[] { 57.0, 36.8, 26.3, 20.6, 17.3, 15.2, 13.9, 12.9, 12.1 });
        qualities.put("43s", new double[] { 38.0, 26.2, 20.3, 16.9, 14.7, 13.1, 12.0, 11.1, 10.3 });
        qualities.put("43o", new double[] { 34.4, 22.3, 16.3, 12.8, 10.7, 9.3, 8.3, 7.5, 6.8 });
        qualities.put("42s", new double[] { 36.3, 24.6, 18.8, 15.7, 13.7, 12.3, 11.2, 10.4, 9.6 });
        qualities.put("42o", new double[] { 32.5, 20.5, 14.7, 11.5, 9.5, 8.3, 7.3, 6.6, 6.0 });

        qualities.put("33", new double[] { 53.7, 33.5, 23.9, 19.0, 16.2, 14.6, 13.5, 12.6, 12.0 });
        qualities.put("32s", new double[] { 35.1, 23.6, 18.0, 14.9, 13.0, 11.7, 10.7, 9.9, 9.2 });
        qualities.put("32o", new double[] { 31.2, 19.5, 13.9, 10.8, 8.9, 7.7, 6.8, 6.1, 5.6 });

        qualities.put("22", new double[] { 50.3, 30.7, 22.0, 17.8, 15.5, 14.2, 13.3, 12.5, 12.0 });
    }

    /**
     * Returns the propability to win with the hand existing of the cards <code>holeCard1</code> and
     * <code>holeCard2</code> against the specific enemy count.
     * 
     * @param holeCard1
     *            The first Hole Card.
     * @param holeCard2
     *            The second Hole Card.
     * @param enemies
     *            The enemy count.
     * @return The propability to win with the hand.
     */
    public static double getHandQuality(Card holeCard1, Card holeCard2, int enemies) {
        if (enemies <= 0 || enemies > 9) throw new IllegalArgumentException("Wrong enemies count! (" + enemies + ")");

        String cardsString = Card.getCardsString(holeCard1, holeCard2);

        if (!qualities.containsKey(cardsString)) throw new IllegalStateException("There is no saved quality for the cards " + cardsString + "! (" + holeCard1.toString() + ", " + holeCard2.toString() + ")");

        double[] quality = qualities.get(cardsString);
        return quality[enemies - 1];
    }

}
